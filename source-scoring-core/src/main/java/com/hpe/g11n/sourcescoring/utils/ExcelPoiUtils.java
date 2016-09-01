package com.hpe.g11n.sourcescoring.utils;

import java.io.FileOutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.hpe.g11n.sourcescoring.pojo.Excel;

/**
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年9月1日
 * @Time: 上午10:25:39
 *
 */
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
	@SuppressWarnings("unchecked")
	public static WritableWorkbook exportExcel(List<Excel> lstExcel,
			String filePath) throws Exception {
		FileOutputStream fos = new FileOutputStream(filePath);
		WritableWorkbook workbook =  Workbook.createWorkbook(fos);
		WritableFont color = new WritableFont(jxl.write.WritableFont.ARIAL);//选择字体
		color.setColour(Colour.BLACK);//设置字体颜色为黑色
		
		WritableFont bold = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);//黑体
		for(int i = 0; i < lstExcel.size();i++){
			WritableSheet sheet = workbook.createSheet(lstExcel.get(i).getName(), i);
			
	       
	        List<String> lstHeader = lstExcel.get(i).getHeader();
	        int columnBestWidth[] = new int[lstHeader.size()];
	        for(int j=0; j<lstHeader.size();j++){
	        	jxl.write.WritableCellFormat colorFormat = new jxl.write.WritableCellFormat(bold);
	        	colorFormat.setBackground(Colour.AQUA);
	        	colorFormat.setAlignment(Alignment.CENTRE);
	        	colorFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		        Label header = new Label(j,0,lstHeader.get(j),colorFormat);
		        sheet.addCell(header);
		        columnBestWidth[j] = lstHeader.get(j).length();
	        }
	        
	        List<List<String>> lstValue = lstExcel.get(i).getValue();
	        for(int k=0;k<lstValue.size();k++){
	        	List<String> lstObj = lstValue.get(k);
	        	for(int m=0;m<lstObj.size();m++){
	        		jxl.write.WritableCellFormat colorFormat = null;
	        		if(i==1 && m==0){
	        			colorFormat = new jxl.write.WritableCellFormat(bold);
	        		}else{
	        			colorFormat = new jxl.write.WritableCellFormat(color);
	        		}
	        		colorFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
	        		Label value = new Label(m,k+1,lstObj.get(m),colorFormat);
			        sheet.addCell(value);
			        if(lstObj.get(m).length() > columnBestWidth[m]){
			        	columnBestWidth[m] = lstObj.get(m).length();
			        }
	        	}
	        }
	        for(int n=0;n<columnBestWidth.length;n++){
	        	sheet.setColumnView(n, columnBestWidth[n]);
	        }
		}
		//把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        fos.close();
		return workbook;
	}
}
