package com.example.GemSkillAssessment.service.period;

import com.example.GemSkillAssessment.dao.FormBaseItemRepository;
import com.example.GemSkillAssessment.dao.FormRepository;
import com.example.GemSkillAssessment.dao.PeriodRepository;
import com.example.GemSkillAssessment.dao.TemplateRepository;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.model.Period;
import com.example.GemSkillAssessment.model.Template;
import com.example.GemSkillAssessment.model.dto.TemplatePeriodDTO;
import com.example.GemSkillAssessment.service.templateservice.FormService;
import com.example.GemSkillAssessment.service.templateservice.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PeriodService {
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FormBaseItemRepository formBaseItemRepository;
    @Autowired
    private FormService formService;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private TemplateService templateService;
    public void addPeriod(Period period) {


        List<Period> periods = (List<Period>) periodRepository.findAll();
        for (Period period1 : periods) {
            if (period1.isEnabled()) throw new NotAcceptableException(EError.PERIODSTILLOPEN.getLabel());
        }
        periodRepository.save(period);
        //this.addPeriodIdToTemp(period.getId());
        formService.createdForm(period);
    }

   /* public void addPeriodIdToTemp(Integer idTemp){
        Set<Template> templates = templateRepository.findByPeriodIsNull();
        for(Template template : templates){
            template.setPeriod(periodRepository.findById(idTemp).get());
        }
    }*/

    public void finishPeriod(Integer id) throws IOException, ClassNotFoundException {
        int count = 0;
        Optional<Period> period = periodRepository.findById(id);
        List<Period> periodList = (List<Period>) periodRepository.findAll();
        if (!this.checkLastPeriod(id)) throw new NotAcceptableException("Please choose lastest period");
        if (!period.get().isEnabled()) {
            for (Period period1 : periodList) {
                if (period1.isEnabled()) throw new NotAcceptableException("Period still open, can't enable");
            }
            period.get().setEnabled(true);
            period.get().setFinishDate(null);
         //   this.addPeriodIdToTemp(period.get().getId());
        } else {
            period.get().setEnabled(false);
            period.get().setFinishDate(new Date());
        }
        Set<Template> templates = templateRepository.findAll();
        Set<TemplatePeriodDTO> templatePeriodDTOS = templateService.readFile();
        TemplatePeriodDTO templatePeriod;
        for(TemplatePeriodDTO templatePeriodDTO : templatePeriodDTOS){
            if (templatePeriodDTO.getPeriodId()==id) {
                templatePeriodDTO.setTemplates(templates);
                PrintWriter writer = new PrintWriter("TemplateStorage.txt");
                writer.print("");
                writer.close();
                count ++;
            }

        }
        if( count == 0) {
            templatePeriod = new TemplatePeriodDTO(id,templates);
            templatePeriodDTOS.add(templatePeriod);
        }

        templateService.writeTemplateToFile(templatePeriodDTOS);
        periodRepository.save(period.get());
    }


    public Period checkPeriodStart(Integer idPeriod) {
        Period period;
        if (idPeriod != null) period = periodRepository.findById(idPeriod.intValue());
        else period = periodRepository.findByEnabledTrue();
        if (period == null) throw new NotAcceptableException(EError.NOT_HAS_PERIOD_NBALE.getLabel());
        return period;
    }

    public Period findByEnabledTrue() {
        return periodRepository.findByEnabledTrue();
    }

    public void updatePeriod(Integer id, Period period) {
        Optional<Period> periodUpdate = periodRepository.findById(id);
        List<Period> periodList = (List<Period>) periodRepository.findAll();
        if (period.getStartDate() == null) throw new NotAcceptableException("Start date mustn't be null!");
        if ("".equals(period.getName())) throw new NotAcceptableException("Name mustn't be empty!");
        periodUpdate.get().setName(period.getName());
        periodUpdate.get().setStartDate(period.getStartDate());
        if (period.isEnabled()) {
            for (Period period1 : periodList) {
                if (period1.isEnabled()) throw new NotAcceptableException("Period still open, can't update");
            }
        }
        periodUpdate.get().setEnabled(period.isEnabled());
        periodUpdate.get().setFinishDate(period.getFinishDate());
        periodRepository.save(periodUpdate.get());
    }

    @Transactional
    public void deletePeriod(Integer id) {
        Optional<Period> periodDelete = periodRepository.findById(id);
        if (periodDelete.get().isEnabled()) throw new NotAcceptableException("Can't delete period! Period is enable");
        /*for(Form form : formRepository.findAll()){
            if(form.getPeriod().getId() == id) throw new NotAcceptableException("Can't delete period! Period have data");
        }*/
        System.out.println(periodDelete.get().getListForm().size());
        if (periodDelete.get().getListForm().size() > 0)
            throw new NotAcceptableException("Can't delete period! Period has data");

        periodRepository.delete(periodDelete.get());

    }

    public boolean checkLastPeriod(Integer idPeriod) {
        List<Period> periodSet = (List<Period>) periodRepository.findAll();
        Period periodTemp = new Period();
        for (Period period : periodSet) {
            periodTemp = period;
        }
        return periodTemp.getId() == idPeriod;
    }

    public int findLastPeriod(){
        List<Period> periodSet = (List<Period>) periodRepository.findAll();
        Period periodTemp = new Period();
        for (Period period : periodSet) {
            periodTemp = period;
        }
        return periodTemp.getId();
    }

    public Period findTopPeriod(){
        return periodRepository.findTopByOrderByStartDateDesc();
    }

   /* public void updateStatusPeriod(Integer id){
        Optional<Period> periodUpdate = periodRepository.findById(id);
        if(!periodUpdate.get().isEnabled()) periodUpdate.get().setEnabled(true);
        periodRepository.save(periodUpdate.get());
    }*/

}
