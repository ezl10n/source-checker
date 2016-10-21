package com.hpe.g11n.sourcechecker.utils;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hpe.g11n.sourcechecker.pojo.Excel;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年9月1日
 * @Time: 上午10:25:39
 *
 */
@SuppressWarnings("deprecation")
public class ExcelPoiUtils {
	/**
	 * @功能描述 设置excel文档(多表单)
	 * @param lstExcel
	 *            excel
	 * @param filePath
	 *            excel文件地址
	 * @throws Exception
	 *             异常往上抛出
	 */
	public static Workbook exportExcel(List<Excel> lstExcel,
			String filePath) throws Exception {
		Excel summary = lstExcel.get(0);
		Excel count = lstExcel.get(1);
		Excel detail = lstExcel.get(2);
		Excel duplicatedDetail = lstExcel.get(3);
		FileOutputStream fos = new FileOutputStream(filePath);
		
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
	    Workbook wb = null;
	    if (fileType.equals("xls")) {
	      wb = new HSSFWorkbook();
	    } else if (fileType.equals("xlsx")) {
	      wb = new XSSFWorkbook();
	    }
	    
	    Font black = getFontBlack(wb);
	    Font red = getFontRed(wb);
	    CellStyle redCellStyle = getCellStyleRed(wb,black,red);
	    CellStyle blackCellStyle = getDetailValueStyle(wb);
	    
	    Sheet summarySheet = wb.createSheet(summary.getName());
	    CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 9);
	    summarySheet.addMergedRegion(cellRangeAddress);

	    Row row = (Row) summarySheet.createRow(0);
	    Cell cell = row.createCell(0);
        cell.setCellValue("HPE SourceChecker Result");
        row.setHeight((short) 1000);
        cell.setCellStyle(getMergeStyle(wb));
        
        for(int j=0;j<summary.getValue().size();j++){
        	List<String> lstObj = summary.getValue().get(j);
        	for(int m=0;m<lstObj.size()-1;m++){
        		Row summaryRow = null;
        		if(m>2){
        			summaryRow = (Row) summarySheet.createRow(m+3);
        		}else{
        			summaryRow = (Row) summarySheet.createRow(m+2);
        		}
        		//write summary header
        		Cell headCell = summaryRow.createCell(0);
            	headCell.setCellValue(summary.getHeader().get(m));
                headCell.setCellStyle(getHeaderStyle(wb));
                
        		// write summary value
            	Cell valueCell = summaryRow.createCell(1);
            	valueCell.setCellValue(lstObj.get(m));
            	CellStyle valueStyle =wb.createCellStyle();
            	valueStyle.setAlignment(CellStyle.ALIGN_LEFT); 
            	valueStyle.setVerticalAlignment(CellStyle.ALIGN_LEFT);
        		// 设置单元格字体
        		Font valueFont = wb.createFont(); // 字体
        		valueFont.setFontHeightInPoints((short)12);
        		valueFont.setColor(HSSFColor.BLACK.index);
        		valueFont.setFontName("Arial");
        		valueStyle.setFont(valueFont);
        		valueStyle.setWrapText(true);
        		
        	    // 设置单元格边框及颜色
        		valueStyle.setBorderBottom((short)1);
        		valueStyle.setBorderLeft((short)1);
        		valueStyle.setBorderRight((short)1);
        		valueStyle.setBorderTop((short)1);
        		if(m==2){
        			valueStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
        			valueStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
        		}
        		valueStyle.setWrapText(true);
        		valueCell.setCellStyle(valueStyle);
        	}
        }
        
      //write count header
        Row countRow= summarySheet.createRow(10);
        for(int k=0;k<count.getHeader().size();k++){
        	Cell headCell = countRow.createCell(k);
        	headCell.setCellValue(count.getHeader().get(k));
            headCell.setCellStyle(getHeaderStyle(wb));
        }
        
        //write count value
        for(int i=0;i<count.getValue().size();i++){
        	List<String> lstObj = (List<String>)count.getValue().get(i);
        	Row valueRow= summarySheet.createRow(i+11);
        	for(int n =0;n<lstObj.size();n++){
        		Cell valueCell = valueRow.createCell(n);
        		valueCell.setCellValue(lstObj.get(n));
        		if(n==0){
        			valueCell.setCellStyle(getValueStyle(wb,true));
        		}else{
        			valueCell.setCellStyle(getValueStyle(wb,false));
        		}
        		
        	}
        }
        
        //write detail
        Sheet detailSheet = wb.createSheet(detail.getName());
        //write detail header
        Row detailRow = detailSheet.createRow(0);
        for(int k=0;k<detail.getHeader().size();k++){
        	Cell headCell = detailRow.createCell(k);
        	headCell.setCellValue(detail.getHeader().get(k));
            headCell.setCellStyle(getHeaderStyle(wb));
        }
        
        //write detail value
        for(int i=0;i<detail.getValue().size();i++){
        	List<String> lstObj = (List<String>)detail.getValue().get(i);
        	Row valueRow= detailSheet.createRow(i+1);
    		for(int n =0;n<lstObj.size();n++){
        		Cell valueCell = valueRow.createCell(n);
        		if(n==lstObj.size()-1 && lstObj.get(n).contains("\"")){
        			//设置红色高亮
        			HSSFRichTextString ts= new HSSFRichTextString(lstObj.get(n));
        			String[] str =lstObj.get(n).split("\"");
        			int index = lstObj.get(n).lastIndexOf("\"");
         		    ts.applyFont(0,str[0].length()+1,black);
         		    ts.applyFont(str[0].length()+1,index,red);
         		    
         		    valueCell.setCellValue(ts);
       			    valueCell.setCellStyle(redCellStyle);
        		}else{
        			valueCell.setCellValue(lstObj.get(n));
        			valueCell.setCellStyle(blackCellStyle);
        		}
        	}	
        	
        }
        
        //write duplicated detail
        Sheet duplicatedDetailSheet = wb.createSheet(duplicatedDetail.getName());
        //write duplicated header
        Row duplicatedDetailRow = duplicatedDetailSheet.createRow(0);
        for(int k=0;k<duplicatedDetail.getHeader().size();k++){
        	Cell headCell = duplicatedDetailRow.createCell(k);
        	headCell.setCellValue(duplicatedDetail.getHeader().get(k));
        	headCell.setCellStyle(getHeaderStyle(wb));
        }
        
        //write duplicated value
        for(int i=0;i<duplicatedDetail.getValue().size();i++){
        	List<String> lstObj = (List<String>)duplicatedDetail.getValue().get(i);
        	Row valueRow= duplicatedDetailSheet.createRow(i+1);
        	for(int n =0;n<lstObj.size();n++){
        		Cell valueCell = valueRow.createCell(n);
        		if(n==lstObj.size()-1 && lstObj.get(n).contains("\"")){
        			//设置红色高亮
        			HSSFRichTextString ts= new HSSFRichTextString(lstObj.get(n));
        			String[] str =lstObj.get(n).split("\"");
        			int index = lstObj.get(n).lastIndexOf("\"");
        			ts.applyFont(0,str[0].length()+1,black);
        			ts.applyFont(str[0].length()+1,index,red);
        			
        			valueCell.setCellValue(ts);
        			valueCell.setCellStyle(redCellStyle);
        		}else{
        			valueCell.setCellValue(lstObj.get(n));
        			valueCell.setCellStyle(blackCellStyle);
        		}
        	}	
        	
        }
        
        for(int i=0;i<10;i++){
	    	summarySheet.autoSizeColumn((short)i);
	    }
        for(int i=0;i<6;i++){
        	if(i !=3){
        		detailSheet.setColumnWidth(i, 20*256);
            	duplicatedDetailSheet.setColumnWidth(i, 20*256);
        	}else{
            	detailSheet.setColumnWidth(i, 60*256);
            	duplicatedDetailSheet.setColumnWidth(i, 60*256);
        	}
	    }
        // 写入数据
        wb.write(fos);
        // 关闭文件流
        fos.close();
		return wb;
	}
	private static CellStyle getMergeStyle(Workbook workbook){
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER); 
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置单元格字体
		Font headerFont = workbook.createFont(); // 字体
		headerFont.setFontHeightInPoints((short)18);
		headerFont.setColor(HSSFColor.BLACK.index);
		headerFont.setFontName("Arial");
		headerFont.setBold(true);
		style.setFont(headerFont);
		style.setWrapText(true);
		
	    // 设置单元格边框及颜色
		style.setBorderBottom((short)1);
		style.setBorderLeft((short)1);
		style.setBorderRight((short)1);
		style.setBorderTop((short)1);
		style.setWrapText(true);
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return style;
		}
	
	private static CellStyle getHeaderStyle(Workbook workbook){
		CellStyle headstyle =workbook.createCellStyle();
        headstyle.setAlignment(CellStyle.ALIGN_CENTER); 
        headstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置单元格字体
		Font headerFont = workbook.createFont(); // 字体
		headerFont.setFontHeightInPoints((short)12);
		headerFont.setColor(HSSFColor.WHITE.index);
		headerFont.setFontName("Arial");
		headstyle.setFont(headerFont);
		headstyle.setWrapText(true);
		
	    // 设置单元格边框及颜色
		headstyle.setBorderBottom((short)1);
		headstyle.setBorderLeft((short)1);
		headstyle.setBorderRight((short)1);
		headstyle.setBorderTop((short)1);
		headstyle.setWrapText(true);
		headstyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
		headstyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
		return headstyle;
	}
	
	private static CellStyle getValueStyle(Workbook workbook,boolean flag){
		CellStyle valueStyle =workbook.createCellStyle();
    	valueStyle.setAlignment(CellStyle.ALIGN_CENTER); 
    	valueStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		// 设置单元格字体
		Font valueFont = workbook.createFont(); // 字体
		valueFont.setFontHeightInPoints((short)12);
		valueFont.setColor(HSSFColor.BLACK.index);
		valueFont.setFontName("Arial");
		valueFont.setBold(flag);
		valueStyle.setFont(valueFont);
		valueStyle.setWrapText(true);
		
	    // 设置单元格边框及颜色
		valueStyle.setBorderBottom((short)1);
		valueStyle.setBorderLeft((short)1);
		valueStyle.setBorderRight((short)1);
		valueStyle.setBorderTop((short)1);
		valueStyle.setWrapText(true);
		return valueStyle;
	}
	
	private static CellStyle getDetailValueStyle(Workbook workbook){
		CellStyle valueStyle =workbook.createCellStyle();
		valueStyle.setAlignment(CellStyle.ALIGN_LEFT); 
		valueStyle.setVerticalAlignment(CellStyle.ALIGN_LEFT);
		// 设置单元格字体
		Font valueFont = workbook.createFont(); // 字体
		valueFont.setFontHeightInPoints((short)10);
		valueFont.setColor(HSSFColor.BLACK.index);
		valueFont.setFontName("Arial");
		valueStyle.setFont(valueFont);
		valueStyle.setWrapText(true);
		
		// 设置单元格边框及颜色
		valueStyle.setBorderBottom((short)1);
		valueStyle.setBorderLeft((short)1);
		valueStyle.setBorderRight((short)1);
		valueStyle.setBorderTop((short)1);
		valueStyle.setWrapText(true);
		return valueStyle;
	}
	
	private static Font getFontBlack(Workbook wb){
		Font valueFont = wb.createFont(); // 字体
		valueFont.setFontHeightInPoints((short)10);
		valueFont.setColor(HSSFColor.BLACK.index);
		valueFont.setFontName("Arial");
		return valueFont;
	}
	private static Font getFontRed(Workbook wb){
		// 设置单元格字体
		Font valueFont1 = wb.createFont(); // 字体
		valueFont1.setFontHeightInPoints((short)10);
		valueFont1.setColor(HSSFColor.RED.index);
		return valueFont1;
	}
	private static CellStyle getCellStyleRed(Workbook wb,Font valueFont,Font valueFont1){
		CellStyle valueStyle =wb.createCellStyle();
		valueStyle.setAlignment(CellStyle.ALIGN_LEFT); 
		valueStyle.setVerticalAlignment(CellStyle.ALIGN_LEFT);
		// 设置单元格字体
		valueFont.setFontHeightInPoints((short)10);
		valueFont.setColor(HSSFColor.BLACK.index);
		valueFont.setFontName("Arial");
		valueStyle.setFont(valueFont);
		valueStyle.setWrapText(true);
		
		// 设置单元格字体
		valueFont1.setFontHeightInPoints((short)10);
		valueFont1.setColor(HSSFColor.RED.index);
		valueFont1.setFontName("Arial");
		valueStyle.setFont(valueFont1);
		valueStyle.setWrapText(true);
		
		// 设置单元格边框及颜色
		valueStyle.setBorderBottom((short)1);
		valueStyle.setBorderLeft((short)1);
		valueStyle.setBorderRight((short)1);
		valueStyle.setBorderTop((short)1);
		valueStyle.setWrapText(true);
		return valueStyle;
	}
	
}
