package com.example.GemSkillAssessment.service.manage;

import com.example.GemSkillAssessment.dao.BaseItemRepository;
import com.example.GemSkillAssessment.dao.CategoryRepository;
import com.example.GemSkillAssessment.dao.SubCategoryRepository;
import com.example.GemSkillAssessment.enumerted.EBaseItemType;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.enumerted.EKeyFunction;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.model.*;
import com.example.GemSkillAssessment.model.dto.FormBaseItemDTO;
import com.example.GemSkillAssessment.model.dto.FormDTO;
import com.example.GemSkillAssessment.model.dto.SumPointCategoryDTO;
import com.example.GemSkillAssessment.service.period.PeriodService;
import com.example.GemSkillAssessment.service.templateservice.FormService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class ExcelService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private BaseItemRepository baseItemRepository;
    @Autowired
    private FormService formService;
    @Autowired
    private PeriodService periodService;

    public void importCategory(MultipartFile multipartFile) {
        XSSFWorkbook workbook = this.convertFileToByte(multipartFile);
        Iterator<Sheet> sheetIterator = workbook.iterator();
        while (sheetIterator.hasNext()) {
            XSSFSheet sheet = (XSSFSheet) sheetIterator.next();
            Category category = this.checkCategory(sheet);
            Iterator<Row> rowIterator = sheet.iterator();
            int i = 0;
            SubCategory subCategory = new SubCategory();
            Set<SubCategory> subCategories = new HashSet<>();
            while (rowIterator.hasNext()) {
                if (i == 0) {
                    rowIterator.next();
                    i++;
                }
                Row row = rowIterator.next();
                String nameBase = row.getCell(1).toString();
                if (nameBase.equals("")) throw new NotFoundException(EError.BASE_IS_NULL.getLabel());
                System.out.println(nameBase);
                BaseItem baseItem = baseItemRepository.findByName(nameBase);
                if (baseItem == null) {
                    baseItem = new BaseItem();
                    baseItem.setType(EBaseItemType.POINT);
                }
                baseItem.setName(nameBase);
                baseItemRepository.save(baseItem);
                String nameSub = row.getCell(0).toString();
                if (nameSub.equals("")) throw new NotFoundException(EError.SUB_IS_NULL.getLabel());
                subCategory = subCategoryRepository.findByName(nameSub);
                Set<BaseItem> baseItems = new HashSet<>();
                if (subCategory == null)
                    subCategory = new SubCategory();
                else
                    baseItems = subCategory.getBaseItems();
                subCategory.setName(nameSub);
                baseItems.add(baseItem);
                subCategory.setBaseItems(baseItems);
                subCategoryRepository.save(subCategory);
                subCategories.add(subCategory);
            }
            category.setSubCategories(subCategories);
            categoryRepository.save(category);
        }

    }

    public InputStreamResource exportReport(Integer idPeriod) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report period");
        XSSFCellStyle poinReviewStyple = this.createdStyle(workbook, IndexedColors.LIGHT_GREEN);
        XSSFCellStyle txtStyle = this.createdStyle(workbook, IndexedColors.WHITE);
        XSSFCellStyle styleHeader = this.styleHeader(workbook);
        List<FormDTO> formDTOList = formService.findAllCate(idPeriod, EKeyFunction.VIEW_REPORT);
        Set<Category> categorySet = categoryRepository.findAll();
        int rowNum = 1, cellCateNum = 5;
        Cell cell;
        Row row;
        this.creatHeaderReport(sheet, categorySet, styleHeader, cellCateNum);

        for (FormDTO formDTO : formDTOList) {
            cellCateNum = 6;
            int totalPointSefl = 0;
            int totalPointSupervisor = 0;
            rowNum++;
            row = sheet.createRow(rowNum);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(rowNum - 1);
            cell.setCellStyle(txtStyle);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(formDTO.getNameUser());
            cell.setCellStyle(txtStyle);
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(formDTO.getIdEmployee());
            cell.setCellStyle(txtStyle);
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(formDTO.geteJobTitle().getName());
            cell.setCellStyle(txtStyle);
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue(String.valueOf(formDTO.getNameSupervisor()));
            cell.setCellStyle(txtStyle);
            for (Category category : categorySet) {
                cell = row.createCell(cellCateNum - 1, CellType.STRING);
                cell.setCellValue(0);
                cell.setCellStyle(txtStyle);
                cell = row.createCell(cellCateNum, CellType.STRING);
                cell.setCellValue(0);
                cell.setCellStyle(txtStyle);
                for (SumPointCategoryDTO sumPointCategoryDTO : formDTO.getSumPointCategoryDTOList()) {
                    if (category.getId() == sumPointCategoryDTO.getId()) {
                        cell = row.createCell(cellCateNum - 1, CellType.STRING);
                        cell.setCellValue(sumPointCategoryDTO.getSelfAssessment());
                        cell.setCellStyle(txtStyle);
                        cell = row.createCell(cellCateNum, CellType.STRING);
                        cell.setCellValue(sumPointCategoryDTO.getSupervisorAssessment());
                        cell.setCellStyle(txtStyle);
                        totalPointSefl += sumPointCategoryDTO.getSelfAssessment();
                        totalPointSupervisor += sumPointCategoryDTO.getSupervisorAssessment();
                    }
                }
                cellCateNum += 2;
            }
            cell = row.createCell(cellCateNum - 1, CellType.STRING);
            cell.setCellValue(totalPointSefl);
            cell.setCellStyle(poinReviewStyple);
            cell = row.createCell(cellCateNum++, CellType.STRING);
            cell.setCellValue(String.valueOf(totalPointSupervisor));
            cell.setCellStyle(poinReviewStyple);
            cell = row.createCell(cellCateNum, CellType.STRING);
            String commentReview = this.getCommentReview(formDTO.getFormBaseItemDTOs()).toString();
            cell.setCellValue(commentReview);
        }
        return this.exportExcel(this.convertWorkbookToByte(workbook));
    }

    public InputStreamResource exportFormUser(Integer idPeriod, User user) {
        Period period = periodService.checkPeriodStart(idPeriod);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle subStyple = this.createdStyle(workbook, IndexedColors.LIGHT_YELLOW);
        XSSFCellStyle poinReviewStyple = this.createdStyle(workbook, IndexedColors.LIGHT_GREEN);
        XSSFCellStyle txtStyle = this.createdStyle(workbook, IndexedColors.WHITE);
        XSSFCellStyle styleHeader = this.styleHeader(workbook);

        FormDTO formDTO = formService.findFormByUser(period, user, EKeyFunction.GET_POINT_OF_FORM, false);
        for (Category category : formDTO.getCategorySet()) {
            int pointCate = 0, pointCateReview = 0, rowNumTotal = 1, rowNum = category.getSubCategories().size() + 5;
            Row row;
            Cell cell;
            XSSFSheet sheet = workbook.createSheet(category.getName());
            this.creatHeaderFormUser(sheet, styleHeader, rowNum);
            for (SubCategory subCategory : category.getSubCategories()) {
                if (subCategory.getBaseItems().size() == 0) continue;
                int creatSub = 0;
                int subPoint = 0;
                int subPointReview = 0;
                for (BaseItem baseItem : subCategory.getBaseItems()) {
                    int point = 0, pointReview = 0;
                    row = sheet.createRow(++rowNum);
                    if (creatSub == 0) {
                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellValue(subCategory.getName());
                        cell.setCellStyle(subStyple);
                    }
                    if (baseItem.getFormBaseItem().getPoint() != null) point = baseItem.getFormBaseItem().getPoint();
                    if (baseItem.getFormBaseItem().getPointReview() != null)
                        pointReview = baseItem.getFormBaseItem().getPointReview();
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue(baseItem.getName());
                    cell.setCellStyle(subStyple);
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue(point);
                    cell.setCellStyle(txtStyle);
                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue(pointReview);
                    cell.setCellStyle(poinReviewStyple);
                    subPoint += point;
                    subPointReview += pointReview;
                    creatSub = 1;
                }
                int rowSub = rowNum - subCategory.getBaseItems().size() + 1;
                if (rowNum - rowSub != 0) sheet.addMergedRegion(new CellRangeAddress(rowSub, rowNum, 0, 0));
                row = sheet.createRow(rowNumTotal);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(subCategory.getName());
                cell.setCellStyle(subStyple);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(subPointReview);
                cell.setCellStyle(poinReviewStyple);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(subPoint);
                cell.setCellStyle(txtStyle);
                pointCate += subPoint;
                pointCateReview += subPointReview;
                rowNumTotal++;
            }
            row = sheet.createRow(rowNumTotal);
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(pointCateReview);
            cell.setCellStyle(txtStyle);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Total");
            cell.setCellStyle(subStyple);
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(pointCate);
            cell.setCellStyle(poinReviewStyple);
        }
        return this.exportExcel(this.convertWorkbookToByte(workbook));
    }

    private void creatHeaderFormUser(Sheet sheet, XSSFCellStyle styleHeader, int rowNum) {
        Row row;
        Cell cell;
        row = sheet.createRow(0);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellStyle(styleHeader);
        cell.setCellValue("Skill Category");
        cell = row.createCell(2, CellType.STRING);
        cell.setCellStyle(styleHeader);
        cell.setCellValue("Employee");
        cell = row.createCell(3, CellType.STRING);
        cell.setCellStyle(styleHeader);
        cell.setCellValue("Supervisor");
        cell.setCellStyle(styleHeader);

        row = sheet.createRow(rowNum);
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Supervisor");
        cell.setCellStyle(styleHeader);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Skill Category");
        cell.setCellStyle(styleHeader);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Soft Skill");
        cell.setCellStyle(styleHeader);
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Employee");
        cell.setCellStyle(styleHeader);
    }

    private void creatHeaderReport(XSSFSheet sheet, Set<Category> categorySet, XSSFCellStyle styleHeader, Integer cellCateNum) {
        Cell cell;
        Row row;
        row = sheet.createRow(0);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("STT");
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Name");
        cell.setCellStyle(styleHeader);
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Job Title");
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Id");
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Supervised by");
        for (Category category : categorySet) {
            cell = row.createCell(cellCateNum++, CellType.STRING);
            cell.setCellValue("Self " + category.getName() + " Assessment");
            cell = row.createCell(cellCateNum++, CellType.STRING);
            cell.setCellValue("Supervisor " + category.getName() + " Assessment");
        }
        cell = row.createCell(cellCateNum, CellType.STRING);
        cell.setCellValue("Total Self-Assessment Score ");
        cellCateNum++;
        cell = row.createCell(cellCateNum, CellType.STRING);
        cell.setCellValue("Total Supervisor-Assessment Score ");
        cellCateNum++;
        cell = row.createCell(cellCateNum, CellType.STRING);
        cell.setCellValue("Manager expectation ");
        Iterator<Cell> cellIterator = row.iterator();
        int i = 0;
        while (cellIterator.hasNext()) {
            XSSFCell cellHeader = (XSSFCell) cellIterator.next();
            cellHeader.setCellStyle(styleHeader);
            sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i++));
        }

    }

    private Category checkCategory(XSSFSheet sheet) {
        if (sheet.getSheetName() == null) throw new NotFoundException(EError.CATE_IS_NULL.getLabel());
        Category category = categoryRepository.findByName(sheet.getSheetName());
        if (category == null) {
            category = new Category();
            category.setName(sheet.getSheetName());
        }
        return category;
    }

    private XSSFWorkbook convertFileToByte(MultipartFile multipartFile) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            byte[] byteArr = multipartFile.getBytes();
            InputStream inputStream = new ByteArrayInputStream(byteArr);
            workbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    private InputStreamResource exportExcel(byte[] data) {
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return inputStreamResource;
    }

    private byte[] convertWorkbookToByte(Workbook workbook) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    private StringBuilder getCommentReview(List<FormBaseItemDTO> formBaseItemDTOs) {
        StringBuilder commentReview = new StringBuilder();
        for (FormBaseItemDTO formBaseItemDTO : formBaseItemDTOs) {
            if (formBaseItemDTO.getType() == EBaseItemType.COMMENTREVIEW && formBaseItemDTO.getCommentReview() != null)
                commentReview.append(formBaseItemDTO.getBaseItemName() + " : " + formBaseItemDTO.getCommentReview() + "\n");
        }
        return commentReview;
    }

    private XSSFCellStyle createdStyle(XSSFWorkbook workbook, IndexedColors indexedColors) {
        XSSFFont txtFont = workbook.createFont();
        txtFont.setFontHeightInPoints((short) 14);
        XSSFCellStyle txtStyle = workbook.createCellStyle();

        txtStyle.setFillForegroundColor(indexedColors.getIndex());
        txtStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        txtStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        txtStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        txtStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        txtStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        txtStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        txtStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        txtStyle.setFont(txtFont);
        return txtStyle;
    }

    private XSSFCellStyle styleHeader(XSSFWorkbook workbook) {
        XSSFFont txtFont = workbook.createFont();
        txtFont.setFontHeightInPoints((short) 14);
        txtFont.setBold(true);
        txtFont.setColor(IndexedColors.WHITE.getIndex());

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.WHITE.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.WHITE.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.WHITE.getIndex());
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFont(txtFont);
        style.setWrapText(true);
        return style;
    }
}
