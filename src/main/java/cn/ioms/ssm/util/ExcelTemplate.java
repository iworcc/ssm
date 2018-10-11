package cn.ioms.ssm.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.util.StringUtils;

/**
 * Excel 动态模板
 * @author IOMS
 *
 */
public class ExcelTemplate {
	
	
	private static HSSFWorkbook wb;

	/**
     * @Title: createExcelTemplate 
     * @Description: 生成Excel导入模板  
     * @param response  
     * @param handers   Excel列标题(数组)
     * @param downData  下拉框数据(数组)
     * @param downRows  下拉列的序号(数组,序号从0开始)
     * @param dateRows  时间列的序号(数组,序号从0开始) 若无传null即可
     * @throws
     */
    public static void createExcelTemplate(HttpServletResponse response, String[] handers, 
            List<String[]> downData, Integer[] downRows,Integer[] dateRows){
        wb = new HSSFWorkbook();
        //新建sheet
        HSSFSheet sheet1 = wb.createSheet("sheet1");
        //生成sheet1内容
        HSSFRow rowFirst = sheet1.createRow(0);//第一个sheet的第一行为标题
        //写标题
        for(int i=0;i<handers.length;i++){
        	sheet1.setDefaultColumnStyle(i, defaultStyle());//设置每列默认样式
        	sheet1.setColumnWidth(i, 4000);//设置每列宽度
        	HSSFCell cell = rowFirst.createCell(i); //获取第一行的每个单元格
            cell.setCellValue(handers[i]); //往单元格里写数据
        }
        //设置时间列
        if(dateRows != null) {
        	for (int num : dateRows) {
        		sheet1.setDefaultColumnStyle(num, dateStyle());
			}
        }
        //动态往excel内填写下拉框数据
        setDataCellValue(sheet1,handers,downData,downRows);
        //参数分别是：作用的sheet、起始行、终止行、作用列
        setDataValidationOnly(sheet1,3);
        try {
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();  
            out.close(); 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @Description 将下拉框数据写入excel
     * @param sheet1 sheet页
     * @param handers 头标题
     * @param downData 下拉框数据
     * @param downRows 结束行
     */
    private static void setDataCellValue(HSSFSheet sheet1,String[] handers,List<String[]> downData, Integer[] downRows) {
    	//设置下拉框数据坐标系
        String[] arr = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        //记录下拉框数据填写个数
        int index = 0;
        HSSFRow row = null;
        for(int i=0;i<downRows.length;i++){
        	//下拉框数据开始存放列
        	int hiddentColumn = handers.length+index;
        	//获取下拉对象
            String[] dlData = downData.get(i);
            int rownum = downRows[i];
            //将数组转化为字符串 并获取字符长度  replaceAll替换掉两边括号以及空格
            //Arrays.toString(dlData).replaceAll("[\\[\\]\\s]","")
            if(Arrays.toString(dlData).replaceAll("[\\[\\]\\s]","").length()<255){ //255以内的下拉
                //255以内的下拉,参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
                setDataValidation(sheet1, dlData,rownum); //超过255个报错 
            }else { //255以上的下拉，即下拉列表元素很多的情况
                //1、设置有效性
                //例：String strFormula = "Sheet1!$A$1:$A$5000" ; //Sheet1第A1到A5000作为下拉列表来源数据
                String strFormula = "Sheet1!$"+arr[hiddentColumn]+"$2:$"+arr[hiddentColumn]+"$"+dlData.length; //Sheet1第A1到A dlData.length长度作为下拉列表来源数据
                //设置数据有效性加载在哪个单元格上,参数分别是：从sheet1获取A1到A5000作为一个下拉的数据、起始行、终止行、起始列、终止列
                setDataValidation(sheet1,strFormula,rownum); //下拉列表元素很多的情况
                //2、生成sheet1下拉框内容
                int rowCount = sheet1.getLastRowNum();//获取当前已经创建的行数
                for(int j=0;j<dlData.length;j++){
                	//判断当前行是否创建 若已创建则获取当前行 未创建则创建该行（j+1）避开表头列，在表头列下方加载数据
                	row = (j+1)<=rowCount==true?sheet1.getRow(j+1):sheet1.createRow(j+1);
                    row.createCell(hiddentColumn).setCellValue(dlData[j]);//设置对应单元格的值
                }
                sheet1.setColumnHidden(hiddentColumn, true);//隐藏Excel列
                index++;
            }
        }
    }
    
    /**
     * 
     * @Title: SetDataValidation 
     * @Description: 下拉列表元素很多的情况 (255以上的下拉)
     * @param strFormula
     * @param effectColumn  作用列
     * @throws
     */
    private static void setDataValidation(HSSFSheet sheet,String strFormula,int effectColumn) {
        
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(1, 15536, effectColumn, effectColumn);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions,constraint);
        dataValidation.createPromptBox("温馨提醒", "该列数据为下拉选择，请勿手动填写。");
        sheet.addValidationData(dataValidation);
    }
    
    /**
     * 
     * @Title: setDataValidation 
     * @Description: 下拉列表元素不多的情况(255以内的下拉)
     * @param sheet
     * @param textList
     * @param effectColumn
     * @throws
     */
    private static void setDataValidation(HSSFSheet sheet, String[] textList, int effectColumn) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        //加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        //DVConstraint constraint = new DVConstraint();
        constraint.setExplicitListValues(textList);
        //设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(1,15536,effectColumn,effectColumn);
        //数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
        data_validation.createPromptBox("温馨提醒", "该列数据为下拉选择，请勿手动填写。");
        sheet.addValidationData(data_validation);
    }

    /**
     * @Description  数据列的唯一性验证
     * @param sheet
     * @param effectColumn
     */
    public static void setDataValidationOnly(HSSFSheet sheet, int effectColumn) {
    	String[] arr = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    	String formula = "COUNTIF(A:A,INDIRECT(\""+arr[effectColumn]+"\"&ROW()))=1"; 
    	//设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
    	CellRangeAddressList regions = new CellRangeAddressList(1, 15536, effectColumn, effectColumn);
        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint(formula);
        HSSFDataValidation validation = new HSSFDataValidation(regions,constraint);
        validation.createErrorBox("重复警告", "该列存在重复数据，请仔细检查");
        validation.setSuppressDropDownArrow(true);
        validation.createPromptBox("温馨提醒", "该列数据唯一，请知悉~");
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        sheet.addValidationData(validation);
    }
    
    /**
     * 
     * @Title: encodeChineseDownloadFileName 
     * @Description: TODO   判断下载的浏览器，进行文件名称转码
     * @param request
     * @param pFileName
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName)
            throws UnsupportedEncodingException {
        
        String filename = null;
        String agent = request.getHeader("USER-AGENT");
        
        if (null != agent) {
        	if(-1 != agent.indexOf("Edge")){//IE7+  
                filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
                filename = StringUtils.replace(filename, "+", "%20");//替换空格 
            }else if (-1 != agent.indexOf("Firefox")) {//Firefox  
                filename = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8")))) + "?=";
            }else if (-1 != agent.indexOf("Chrome")) {//Chrome  
                filename = new String(pFileName.getBytes(), "ISO8859-1");
            }
        } else {
            filename = pFileName;
        }
        
        return filename;
    }
 
    /**
     * @Description:设置Excel每列默认样式
     * @return
     */
    private static HSSFCellStyle defaultStyle() {
    	//默认样式
        HSSFCellStyle defaultStyle = wb.createCellStyle();  
        commonStyle(defaultStyle);
        DataFormat  format = wb.createDataFormat();
        defaultStyle.setDataFormat(format.getFormat("@"));
    	
        return defaultStyle;
    }
    
    /**
     *@Description: 设置时间列样式
     * @return
     */
    private static HSSFCellStyle dateStyle() {
    	//默认样式
        HSSFCellStyle dateStyle = wb.createCellStyle();  
        commonStyle(dateStyle);
        DataFormat  format = wb.createDataFormat();
        dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
    	
        return dateStyle;
    }
    
    /**
     * @Description:公用样式
     * @param style
     */
    private static void commonStyle(HSSFCellStyle style) {
    	style.setAlignment(HorizontalAlignment .CENTER); // 创建一个居中格式  
        //字体样式
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short)12); 
        style.setFont(fontStyle);
    }
}
