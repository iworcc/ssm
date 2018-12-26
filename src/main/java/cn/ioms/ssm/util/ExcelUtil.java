package cn.ioms.ssm.util;



import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;


import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {

    private static Workbook wb;
    private static Sheet sheet;
    private static Row row;

    /**
     * 读取Excel表格表头的内容
     * @param is
     * @return String 表头内容的数组
     */
    public static String[] readExcelTitle(InputStream is) {

        try {
            wb = WorkbookFactory.create(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        //得到首行的row
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getCellFormatValue(row.getCell(i));
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     * 所有参数均为必传，若无需跳过行数则传入0（countNum）
     * @param is
     * @param countNum 跳过行数（表头行数）
     * @param dataStr 数据库入库字段（Excel表头对应的数据库字段）
     * @return List<Map> 返回List<Map> 数组 用以存入数据库
     */
    public static List<Map> readExcelContent(InputStream is, int countNum, String[] dataStr) {
        if(countNum < 0){
            countNum = 0;
        }
        List<Map> list = new ArrayList<>();
        Map<String,Object> map = null;
        try {
            wb = WorkbookFactory.create(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        //得到总行数
        int rowNum = sheet.getLastRowNum();
        //获取表头行
        row = sheet.getRow(0);
        //获取该行的列
        int colNum = row.getPhysicalNumberOfCells();
        //正文内容应该从第countNum行开始前countNum行为表头的标题
        for (int i = countNum; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            map =  new HashMap<>();
            while (j < colNum) {
                map.put(dataStr[j],getCellFormatValue(row.getCell(j)).trim());
                j++;
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(Cell cell) {
        String strCell = "";
        switch (cell.getCellTypeEnum()) {
            case STRING:
                strCell = cell.getStringCellValue();
                break;
            case NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 根据Cell类型设置数据
     * @param cell
     * @return
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellTypeEnum()) {
                // 如果当前Cell的Type为NUMERIC
                case NUMERIC:
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                    break;
                case FORMULA:
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                // 如果当前Cell的Type为STRIN
                case STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

}