package com.example.GemSkillAssessment.service.templateservice;

import com.example.GemSkillAssessment.dao.*;
import com.example.GemSkillAssessment.enumerted.EBaseItemType;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.enumerted.EKeyFunction;
import com.example.GemSkillAssessment.enumerted.EPermission;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.error.NotImplementedException;
import com.example.GemSkillAssessment.model.*;
import com.example.GemSkillAssessment.model.dto.FormBaseItemDTO;
import com.example.GemSkillAssessment.model.dto.FormDTO;
import com.example.GemSkillAssessment.model.dto.SumPointCategoryDTO;
import com.example.GemSkillAssessment.model.dto.SumPointSubDTO;
import com.example.GemSkillAssessment.service.period.PeriodService;
import com.example.GemSkillAssessment.service.permission.PermissionService;
import com.example.GemSkillAssessment.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FormService {
    private @Autowired
    BaseItemRepository baseItemRepository;
    private @Autowired
    FormRepository formRepository;
    private @Autowired
    UserRepository userRepository;
    private @Autowired
    FormBaseItemRepository formBaseItemRepository;
    private @Autowired
    UserService userService;
    private @Autowired
    TemplateService templateService;
    private @Autowired
    CategoryRepository categoryRepository;
    private @Autowired
    PeriodService periodService;
    private @Autowired
    PermissionService permissionService;
    private @Autowired
    TemplateRepository templateRepository;


    public void createdForm(Period period) {
        if (period != null) {
            Form form;
            Date date = new Date();
            List<User> userList = userRepository.findByIsAdminFalseAndIsLeftFalse();
            List<Form> formList = new ArrayList<>();
            Set<Template> templateSet = templateRepository.findAll();
            for (User user : userList) {
                Template template = templateService.findByEJobTitleAndPeriod(templateSet, user.getVaEJobTitle(), period.getId());
                form = formRepository.findByEnableIsTrueAndPeriodAndUser(period, user);
                if (form == null && template != null)
                    form = new Form(true, date, user, period, template);
                if (form != null) formList.add(form);
            }
            formRepository.saveAll(formList);
        }
    }

    public void save(List<FormBaseItemDTO> formBaseItemDTOs, User user, EKeyFunction eKeyFunction) {
        Form form = this.getFormOfUser(user, eKeyFunction);
        Set<FormBaseItem> formBaseItems = new HashSet<>();
        for (FormBaseItemDTO formBaseItemDTO : formBaseItemDTOs) {
            BaseItem baseItem;
            this.checkPoint(formBaseItemDTO.getPoint());
            this.checkPoint(formBaseItemDTO.getPointReview());
            if (formBaseItemDTO.getBaseItemId() == null) {
                formRepository.delete(form);
                throw new NotFoundException(EError.ID_BASE_IS_MISSING.getLabel());
            } else {
                baseItem = baseItemRepository.findById(formBaseItemDTO.getBaseItemId());
                if (baseItem == null) {
                    formRepository.delete(form);
                    throw new NotFoundException(EError.ID_BASEITEM_NOT_FOUND.getLabel());
                }
            }
            if (formBaseItemDTO.getComment() != null || formBaseItemDTO.getCommentReview() != null || formBaseItemDTO.getPoint() != null || formBaseItemDTO.getPointReview() != null)
                formBaseItems.add(this.saveFormBaseItem(formBaseItemDTO, form, baseItem, eKeyFunction));
        }
        formBaseItemRepository.saveAll(formBaseItems);
    }

    public FormDTO findFormByUser(Period period, User user, EKeyFunction eKeyFunction, boolean getFormSelf) {
        Form form = formRepository.findByEnableIsTrueAndPeriodAndUser(period, user);
        FormDTO formDTO = new FormDTO();
        if (form == null) return formDTO;
        else {
            if (form.getTemplate() == null) throw new NotImplementedException(EError.TEMPLATE_NOT_EXIST.getLabel());
//            {
//                Template template = templateService.getByPeriodFormEJobTitle(templateRepository.findByEJobTitle(user.getVaEJobTitle()),period.getId());
//                if (template == null) throw new NotImplementedException(EError.TEMPLATE_NOT_EXIST.getLabel());
//                form.setTemplate(template);
//                form.setUser(user);
//                form.setPeriod(period);
//            }
            this.setFormDTOFillEdit(user, formDTO, eKeyFunction);
            Set<Category> categorySet = form.getTemplate().getCategories();
            if (categorySet != null)
                formDTO.setCategorySet(form.getTemplate().getCategories());
            else
                throw new NotImplementedException(EError.FORM_NOT_HAS_CATE.getLabel());
            return this.findValueBaseItem(formDTO, form, eKeyFunction, user, categorySet, getFormSelf);
        }
    }

    public void checkFinish(User user, EKeyFunction eKeyFunction) {
        Form form = formRepository.findByEnableIsTrueAndPeriodAndUser(periodService.checkPeriodStart(null), user);
        if (form == null) throw new NotFoundException(EError.FORM_IS_NOT_FINISH.getLabel());
        for (Category category : form.getTemplate().getCategories()) {
            if (category == null) throw new NotImplementedException(EError.FORM_NOT_HAS_CATE.getLabel());
            for (SubCategory subCategory : category.getSubCategories()) {
                if (subCategory == null) throw new NotImplementedException(EError.CATE_NOT_HAS_SUB.getLabel());
                for (BaseItem baseItem : subCategory.getBaseItems()) {
                    if (baseItem == null) throw new NotImplementedException(EError.SUB_NOT_HAS_BASE.getLabel());
                    this.checkFinishForm(form, baseItem, category.getName(), eKeyFunction);
                }
            }
        }
        Date date = new Date();
        if (eKeyFunction == EKeyFunction.FILL_EDIT) {
            permissionService.lockEdit(user);
            permissionService.lockFill(user);
            user.setPermissions(new HashSet<>());
            userRepository.save(user);
            form.setFinish(true);
            form.setFinishDate(date);
        } else {
            form.setFinishReview(true);
            form.setReviewDate(date);
        }
        formRepository.save(form);
    }

    public List<FormDTO> findAllCate(Integer idPeriod, EKeyFunction eKeyFunction) {
        Period period = periodService.checkPeriodStart(idPeriod);
        if (period.getFinishDate() == null) period.setFinishDate(new Date());
        List<User> users = userRepository.findByIsAdminFalse();
        List<FormDTO> formDTOList = new ArrayList<>();
        Set<Template> templateSet = templateRepository.findAll();
        for (User user : users) {
            Template template = templateService.findByEJobTitleAndPeriod(templateSet, user.getVaEJobTitle(), idPeriod);
            if (template != null) {
                FormDTO formDTO = this.findFormByUser(period, user, eKeyFunction, false);
                if (formDTO.getCategorySet() != null) formDTOList.add(formDTO);
            }
        }
        return formDTOList;
    }

    public SumPointCategoryDTO sumPointCate(Integer idPeriod, Integer idCate) {
        Period period = periodService.checkPeriodStart(idPeriod);
        List<Form> forms = formRepository.findByPeriod(period);
        List<SumPointSubDTO> sumPointSubDTOList = new ArrayList<>();
        Category category = categoryRepository.findById(idCate).get();
        int sumCateSelfAssessment = 0;
        int sumCateSupervisorAssessment = 0;
        for (SubCategory subCategory : category.getSubCategories()) {
            int sumSubSelfAssessment = 0;
            int sumSubSupervisorAssessment = 0;
            for (BaseItem baseItem : subCategory.getBaseItems()) {
                for (Form form : forms) {
                    FormBaseItem formBaseItem = formBaseItemRepository.findByFormAndBaseItemForm(form, baseItem);
                    if (formBaseItem != null) {
                        if (formBaseItem.getPoint() != null) sumSubSelfAssessment += formBaseItem.getPoint();
                        if (formBaseItem.getPointReview() != null)
                            sumSubSupervisorAssessment += formBaseItem.getPointReview();
                    }
                }
            }
            if (sumSubSelfAssessment != 0) {
                sumCateSelfAssessment += sumSubSelfAssessment;
                sumCateSupervisorAssessment += sumSubSupervisorAssessment;
                sumPointSubDTOList.add(new SumPointSubDTO(null, subCategory.getName(), sumSubSelfAssessment, sumCateSupervisorAssessment));
            }
        }
        SumPointCategoryDTO sumPointCategoryDTO = new SumPointCategoryDTO(category.getId(), category.getName(), sumCateSelfAssessment, sumCateSupervisorAssessment, sumPointSubDTOList);
        return sumPointCategoryDTO;
    }

    private FormBaseItem saveFormBaseItem(FormBaseItemDTO formBaseItemDTO, Form form, BaseItem baseItem, EKeyFunction eKeyFunction) {
        FormBaseItem formBaseItem = formBaseItemRepository.findByFormAndBaseItemForm(form, baseItem);
        if (formBaseItem == null) formBaseItem = new FormBaseItem();

        if (eKeyFunction == EKeyFunction.FILL_EDIT) {
            if (baseItem.getType().equals(EBaseItemType.POINT) && formBaseItemDTO.getPoint() != null)
                formBaseItem.setPoint(formBaseItemDTO.getPoint());
            if (baseItem.getType().equals(EBaseItemType.COMMENT) && formBaseItemDTO.getComment() != null)
                formBaseItem.setComment(formBaseItemDTO.getComment());

        } else {
            if (baseItem.getType().equals(EBaseItemType.COMMENTREVIEW) && formBaseItemDTO.getCommentReview() != null)
                formBaseItem.setCommentReview(formBaseItemDTO.getCommentReview());

            if (baseItem.getType().equals(EBaseItemType.POINT) && formBaseItemDTO.getPointReview() != null)
                formBaseItem.setPointReview(formBaseItemDTO.getPointReview());
        }
        formBaseItem.setForm(form);
        formBaseItem.setBaseItemForm(baseItem);
        return formBaseItem;
    }

    private Form getFormOfUser(User user, EKeyFunction eKeyFunction) {
        Date date = new Date();
        Period period = periodService.checkPeriodStart(null);
        Form form = formRepository.findByEnableIsTrueAndPeriodAndUser(period, user);
        if (form == null) {
            throw new NotFoundException(EError.ID_NOT_FOUND.getLabel());
        } else {
            if (form.getTemplate() == null) form.setTemplate(templateService.findByEJobTitle(user.getVaEJobTitle()));
            if (eKeyFunction == EKeyFunction.REVIEW) {
                if (form.getFinishReview() != null && form.getFinishReview())
                    throw new NotFoundException(EError.FORM_REVIEW_IS_FINISH.getLabel());
                if (form.getFinish() == null || !form.getFinish())
                    throw new NotFoundException(EError.FORM_IS_NOT_FINISH.getLabel());
                form.setReviewDate(date);
            } else {
                if (!userService.checkFillEdit(user.getPermissions(), EPermission.FILL) && !userService.checkFillEdit(user.getPermissions(), EPermission.EDIT))
                    throw new NotFoundException(EError.FORM_IS_FINISH.getLabel());
                form.setEditDate(date);
            }
        }
        formRepository.save(form);
        return form;
    }

    private void checkPoint(Integer point) {
        if (point != null) {
            if (point > 10 || point < 0) throw new NotFoundException(EError.VALIDATE_POINT.getLabel());
        }
    }

    private FormDTO findValueBaseItem(FormDTO formDTO, Form form, EKeyFunction eKeyFunction, User user, Set<Category> categorySet, boolean getFormSelf) {
        List<FormBaseItemDTO> formBaseItemComment = new ArrayList<>();
        List<SumPointCategoryDTO> sumPointCategoryDTOList = new ArrayList<>();
        List<SumPointSubDTO> sumPointSubDTOList = new ArrayList<>();

        for (Category category : categorySet) {
            List<SubCategory> subCategoriesRemove = new ArrayList<>();
            int sumCateSelfAssessment = 0;
            int sumCateSupervisorAssessment = 0;
            for (SubCategory subCategory : category.getSubCategories()) {
                int sumSubSelfAssessment = 0;
                int sumSubSupervisorAssessment = 0;
                if (subCategory == null) throw new NotImplementedException(EError.CATE_NOT_HAS_SUB.getLabel());
                Set<BaseItem> baseItemComment = new HashSet<>();
                for (BaseItem baseItem : subCategory.getBaseItems()) {
                    FormBaseItem formBaseItem = this.searchFormBaseItem(baseItem, form, getFormSelf);
                    FormBaseItemDTO formBaseItemDTO = new FormBaseItemDTO();
                    if (baseItem.getType() != EBaseItemType.POINT) {
                        baseItemComment.add(baseItem);
                        formBaseItemDTO.setBaseItemId(baseItem.getId());
                        formBaseItemDTO.setBaseItemName(baseItem.getName());
                        formBaseItemDTO.setType(baseItem.getType());
                        formBaseItemDTO.setComment(formBaseItem.getComment());
                        formBaseItemDTO.setCommentReview(formBaseItem.getCommentReview());
                        formBaseItemComment.add(formBaseItemDTO);
                    }
                    if (eKeyFunction != EKeyFunction.VIEW_REPORT) {
                        formBaseItem.setForm(form);
                        formBaseItem.setBaseItemForm(baseItem);
                        baseItem.setFormBaseItem(formBaseItem);
                    }
                    if (eKeyFunction != EKeyFunction.GET_POINT_OF_FORM) {
                        if (formBaseItem.getPoint() != null)
                            sumSubSelfAssessment += formBaseItem.getPoint();
                        if (formBaseItem.getPointReview() != null)
                            sumSubSupervisorAssessment += formBaseItem.getPointReview();
                    }
                }
                if (eKeyFunction == EKeyFunction.GET_POINT_OF_FORM)
                    subCategory.getBaseItems().removeAll(baseItemComment);
                if (subCategory.getBaseItems().size() == 0) subCategoriesRemove.add(subCategory);
                if (eKeyFunction != EKeyFunction.VIEW_REPORT) subCategory.getBaseItems().removeAll(baseItemComment);
                if (eKeyFunction != EKeyFunction.GET_POINT_OF_FORM) {
                    sumCateSelfAssessment += sumSubSelfAssessment;
                    sumCateSupervisorAssessment += sumSubSupervisorAssessment;
                    if (sumSubSelfAssessment != 0)
                        sumPointSubDTOList.add(new SumPointSubDTO(category.getId(), category.getName(), sumSubSelfAssessment, sumSubSupervisorAssessment));
                }
            }
            category.getSubCategories().removeAll(subCategoriesRemove);
            if (eKeyFunction != EKeyFunction.GET_POINT_OF_FORM)
                sumPointCategoryDTOList.add(new SumPointCategoryDTO(category.getId(), category.getName(), sumCateSelfAssessment, sumCateSupervisorAssessment, null));
        }
        return this.setValueForFormDTO(formDTO, formBaseItemComment, sumPointCategoryDTOList, sumPointSubDTOList, user, form, eKeyFunction);
    }

    private FormDTO setValueForFormDTO(FormDTO formDTO, List<FormBaseItemDTO> formBaseItemComment, List<SumPointCategoryDTO> sumPointCategoryDTOList, List<SumPointSubDTO> sumPointSubDTOList, User user, Form form, EKeyFunction eKeyFunction) {
        if (eKeyFunction != EKeyFunction.GET_POINT_OF_FORM) {
            formDTO.setCategorySet(new HashSet<>());
            formDTO.setFormBaseItemDTOs(formBaseItemComment);
            formDTO.setSumPointCategoryDTOList(sumPointCategoryDTOList);
            formDTO.setSumPointSubDTOList(sumPointSubDTOList);
        }
        if (eKeyFunction == EKeyFunction.VIEW_REPORT) {
            formDTO.setIdEmployee(user.getIdEmployee());
            formDTO.setNameUser(user.getName());
            formDTO.setNameSupervisor(user.getSupervisedName());
            formDTO.seteJobTitle(user.getVaEJobTitle());
//            formDTO.setSumPointCategoryDTOList(this.reportSumPointAllCate(sumPointCategoryDTOList));
            if (form.getRemindReviewSelf() != null) formDTO.setRemindReviewSelf(form.getRemindReviewSelf());
            if (form.getRemindReviewUser() != null) formDTO.setRemindReviewUser(form.getRemindReviewUser());
            formDTO.setIdUser(user.getId());
        }
        if (form.getFinishReview() != null) formDTO.setFinishReview(form.getFinishReview());
        if (form.getFinish() != null) formDTO.setFinish(form.getFinish());
        return formDTO;
    }

    private FormBaseItem searchFormBaseItem(BaseItem baseItem, Form form, boolean getFormSelf) {
        if (baseItem == null) throw new NotImplementedException(EError.SUB_NOT_HAS_BASE.getLabel());
        FormBaseItem formBaseItem;
        try {
            formBaseItem = formBaseItemRepository.findByFormAndBaseItemForm(form, baseItem);
            if (formBaseItem == null) formBaseItem = new FormBaseItem();
        } catch (Exception e) {
            formBaseItem = new FormBaseItem();
        }
        if (getFormSelf) {
            formBaseItem.setCommentReview(null);
            formBaseItem.setPointReview(null);
        }
        return formBaseItem;
    }

    private FormDTO setFormDTOFillEdit(User user, FormDTO formDTO, EKeyFunction eKeyFunction) {
        if (user.getPermissions() != null && eKeyFunction != EKeyFunction.VIEW_REPORT) {
            if (userService.checkFillEdit(user.getPermissions(), EPermission.EDIT))
                formDTO.setEdit(true);
            if (userService.checkFillEdit(user.getPermissions(), EPermission.FILL))
                formDTO.setFill(true);
        }
        return formDTO;
    }

    private void checkFinishForm(Form form, BaseItem baseItem, String categoryName, EKeyFunction eKeyFunction) {
        FormBaseItem formBaseItem = formBaseItemRepository.findByFormAndBaseItemForm(form, baseItem);
        if (formBaseItem == null) {
            formBaseItem = new FormBaseItem();
            formBaseItem.setForm(form);
            formBaseItem.setBaseItemForm(baseItem);
            formBaseItemRepository.save(formBaseItem);
        }
        if (eKeyFunction == EKeyFunction.FILL_EDIT) {
            if (baseItem.getType() == EBaseItemType.POINT) {
                if (formBaseItem.getPoint() == null)
                    throw new NotImplementedException(EError.UNFINISH_FORM.getLabel() + baseItem.getName() + " in " + categoryName);
            } else if (baseItem.getType() == EBaseItemType.COMMENT) {
                if (formBaseItem.getComment() == null)
                    throw new NotImplementedException(EError.UNFINISH_FORM.getLabel() + "type-" + baseItem.getType().toString() + " ,name: " + baseItem.getName());
            }
        } else {
            if (baseItem.getType() == EBaseItemType.POINT) {
                if (formBaseItem.getPointReview() == null)
                    throw new NotImplementedException(EError.UNFINISH_FORM.getLabel() + baseItem.getName() + " in " + categoryName);
            } else if (baseItem.getType() == EBaseItemType.COMMENTREVIEW) {
                if (formBaseItem.getCommentReview() == null)
                    throw new NotImplementedException(EError.UNFINISH_FORM.getLabel() + "type-" + baseItem.getType().toString() + " ,name: " + baseItem.getName());
            }
        }
    }
}

//    private List<SumPointCategoryDTO> reportSumPointAllCate(List<SumPointCategoryDTO> sumPointCategoryDTOList) {
//        List<SumPointCategoryDTO> sumPointCategoryDTOList1 = new ArrayList<>();
//        Set<Category> categoryList = categoryRepository.findAll();
//        for (Category category : categoryList)
//            for (SumPointCategoryDTO sumPointSubDTO : sumPointCategoryDTOList){
//                if(category.getId()==sumPointSubDTO.getId()) sumPointCategoryDTOList1.add(sumPointSubDTO);
//                else sumPointCategoryDTOList1.add(new SumPointCategoryDTO());
//            }
//        return sumPointCategoryDTOList1;
//    }

