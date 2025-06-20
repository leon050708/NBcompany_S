package org.example.nbcompany.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.nbcompany.dto.CourseDto.CourseListItemDTO;
import org.example.nbcompany.service.ExcelExportService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel导出服务实现类
 */
@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Override
    public byte[] exportCourseListToExcel(List<CourseListItemDTO> courseList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("课程列表");
            
            // 创建标题行样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "课程ID", "课程名称", "课程封面", "课程简介", 
                "作者名称", "企业名称", "审核状态", "状态描述", 
                "观看次数", "创建时间"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 创建数据行样式
            CellStyle dataStyle = createDataStyle(workbook);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            // 填充数据
            for (int i = 0; i < courseList.size(); i++) {
                CourseListItemDTO course = courseList.get(i);
                Row dataRow = sheet.createRow(i + 1);
                
                int colIndex = 0;
                dataRow.createCell(colIndex++).setCellValue(course.getId() != null ? course.getId() : 0);
                dataRow.createCell(colIndex++).setCellValue(course.getCourseName() != null ? course.getCourseName() : "");
                dataRow.createCell(colIndex++).setCellValue(course.getCoverImageUrl() != null ? course.getCoverImageUrl() : "");
                dataRow.createCell(colIndex++).setCellValue(course.getSummary() != null ? course.getSummary() : "");
                dataRow.createCell(colIndex++).setCellValue(course.getAuthorName() != null ? course.getAuthorName() : "");
                dataRow.createCell(colIndex++).setCellValue(course.getCompanyName() != null ? course.getCompanyName() : "");
                dataRow.createCell(colIndex++).setCellValue(course.getStatus() != null ? course.getStatus() : 0);
                dataRow.createCell(colIndex++).setCellValue(course.getStatusDesc() != null ? course.getStatusDesc() : "");
                dataRow.createCell(colIndex++).setCellValue(course.getViewCount() != null ? course.getViewCount() : 0);
                dataRow.createCell(colIndex++).setCellValue(
                    course.getCreatedAt() != null ? course.getCreatedAt().format(formatter) : "");
                
                // 应用数据样式
                for (int j = 0; j < headers.length; j++) {
                    dataRow.getCell(j).setCellStyle(dataStyle);
                }
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // 设置最大列宽，避免过宽
                if (sheet.getColumnWidth(i) > 15000) {
                    sheet.setColumnWidth(i, 15000);
                }
            }
            
            // 将工作簿写入字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (IOException e) {
            throw new RuntimeException("导出Excel文件失败", e);
        }
    }
    
    /**
     * 创建标题行样式
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    /**
     * 创建数据行样式
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true); // 自动换行
        return style;
    }
} 