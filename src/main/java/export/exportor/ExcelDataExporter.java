package export.exportor;

import export.base.DataExporter;
import export.base.DataFieldMapper;
import export.exception.BaseExportException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class ExcelDataExporter<T> extends DataExporter<T> {
    private static final String DEFAULT_SHEET_NAME = "defaultSheet";

    // 工作簿
    private HSSFWorkbook hssfWorkbook;
    // 表格
    private HSSFSheet hssfSheet ;
    // 表头
    private HSSFRow hssfRowHead;
    // 表头单元格样式
    private HSSFCellStyle headStyle;
    // 数据单元格样式
    private HSSFCellStyle rowStyle;
    // 表格名
    private String sheetName;
    // 文件输出流
    private FileOutputStream fileOutputStream;
    // 列宽
    private int rowWidth = 20;
    // 表标题
    private String tableTitle ;
    // 是否使用title
    private boolean hasTitle = false;
    // 是否初始化
    private boolean hasInit = false;

    public ExcelDataExporter(List<T> dataList, OutputStream outputStream,String fileName) {
        super(dataList, outputStream,fileName);
    }

    public void initExcel(){
        try {
            initExcelFile();
        } catch (BaseExportException e) {
            e.printStackTrace();
        }
        hssfWorkbook = new HSSFWorkbook();
        hssfSheet = hssfWorkbook.createSheet(sheetName != null ? sheetName : DEFAULT_SHEET_NAME);
        if(hasTitle){
            tableTitle = DataFieldMapper.mapperName(dataList.get(0).getClass());
            initExcelTitle();
        }
        initExcelHead(); // 初始化表头信息
        initDefaultBodyStyle();
        hasInit = true;
    }

    public void initExcelFile() throws BaseExportException {
        try {
            File file = new File(fileName);
            File parentFile = file.getParentFile();
            if(!parentFile.exists())
                parentFile.mkdirs();
            if((!file.exists()))
                file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new BaseExportException("文件被占用，无法访问");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initExcelTitle(){
        // 表格标题
        HSSFRow rowTitle = hssfSheet.createRow(0);
        HSSFFont titleFont = hssfWorkbook.createFont();
        HSSFCellStyle titleStyle = hssfWorkbook.createCellStyle();
        // 设置表格名
        HSSFCell cell = rowTitle.createCell(0);
        cell.setCellValue(tableTitle);
//        titleFont.setBold(true); // 加粗
        titleFont.setFontName("黑体");
        titleFont.setFontHeightInPoints((short)20);
        titleStyle.setFont(titleFont);
        cell.setCellStyle(titleStyle);
        // 合并单元格
        mergeRegion(hssfSheet,0,0,0,headList.size()-1);
    }


    public void initExcelHead() {
        hssfRowHead = hssfSheet.createRow(hasTitle ? 1 : 0);
        HSSFFont headFont = hssfWorkbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 12); // 字体高度
        headFont.setBold(true); // 设置粗体
        headStyle = hssfWorkbook.createCellStyle();
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setFont(headFont);
        headStyle.setWrapText(true); // 超出换行
    }

    public void initDefaultBodyStyle(){
        // 设置字体 (上方标题用 填写内容)
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色
        font.setFontName("宋体"); //字体
        rowStyle = hssfWorkbook.createCellStyle();
        rowStyle.setFont(font);
        rowStyle.setAlignment(HorizontalAlignment.CENTER); //水平布局：居中
        rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);//单元格垂直居中
        rowStyle.setWrapText(true);//换行
    }

    @Override
    public OutputStream export() throws IOException, BaseExportException {
        if(!hasInit)
            throw new BaseExportException("导出前需要调用init 方法");
        outputHead();
        outputBody();
        hssfWorkbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return outputStream;
    }

    @Override
    public void outputHead() {
        for (int i = 0; i < headList.size(); i++) {
            HSSFCell cell = hssfRowHead.createCell(i);
            cell.setCellValue(headList.get(i));
            cell.setCellStyle(headStyle);
        }
    }

    public void outputBody(){
        for (int i = hasTitle? 2 : 1; i < dataList.size(); i++) {
            HSSFRow row = hssfSheet.createRow(i);
            outputLine(dataList.get(i), row,i);
        }
        for (int i = 1; i < headList.size(); i++) {
            hssfSheet.autoSizeColumn(i);
        }
    }

    private void outputLine(T t, HSSFRow hssfRow,int line){
        hssfRow.setRowStyle(rowStyle);
        HSSFCell indexCell = hssfRow.createCell(0);
        indexCell.setCellStyle(rowStyle);
        indexCell.setCellValue(++currentRow);
        for (int i = 0; i < fieldList.size(); i++) {
            HSSFCell cell = hssfRow.createCell(i+1);
            cell.setCellValue(getValue(t,fieldList.get(i).getName()).toString());
            cell.setCellStyle(rowStyle);
        }
    }

    /**
     * 创建一个空的样式
     * @return
     */
    private HSSFCellStyle getStyle(){
        return hssfWorkbook.createCellStyle();
    }

    /**
     * 日期格式化
     * @return
     */
    private HSSFCellStyle getCalendarFormatStyle(){
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        HSSFDataFormat dataFormat = hssfWorkbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("yyyy/mm/dd"));
        // 设置居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    /**
     * 日期时间格式化
     * @return
     */
    private HSSFCellStyle getDateFormatStyle(){
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        HSSFDataFormat dataFormat = hssfWorkbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("yyyy/mm/dd hh:mm"));
        // 设置居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    public HSSFCellStyle getCenterStyle(){
        HSSFCellStyle centerStyle = hssfWorkbook.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return centerStyle;
    }

    /**
     * 设置自动换行
     * @param cellStyle
     */
    private void setAutoWrap(CellStyle cellStyle){
        cellStyle.setWrapText(true);
    }

    private HSSFRow getRow(int rowNum){
        return hssfSheet.createRow(rowNum);
    }


    /**
     * 合并单元格
     * @param sheet 表格
     * @param startRow 开始行
     * @param endRow 结束行
     * @param startColumn 开始列
     * @param endColumn 结束列
     */
    public void mergeRegion(HSSFSheet sheet, int startRow, int endRow, int startColumn, int endColumn){
        sheet.addMergedRegion(new CellRangeAddress(startRow,endRow,startColumn,endColumn));
    }

    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
        this.hasTitle = true;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    private DataFormat getDataFormat(Field field){
        HSSFDataFormat dataFormat = hssfWorkbook.createDataFormat();
        if(int.class.isAssignableFrom(field.getType()) || Integer.TYPE.isAssignableFrom(field.getType())){
            dataFormat.getFormat("#,##0");
        }else if(double.class.isAssignableFrom(field.getType()) || Double.TYPE.isAssignableFrom(field.getType())){
            dataFormat.getFormat(HSSFDataFormat.getBuiltinFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00")));
        }else if(float.class.isAssignableFrom(field.getType()) || Float.TYPE.isAssignableFrom(field.getType())){
            dataFormat.getFormat(HSSFDataFormat.getBuiltinFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00")));
        }else if(boolean.class.isAssignableFrom(field.getType()) || Boolean.TYPE.isAssignableFrom(field.getType())){

        }else if(Date.class.isAssignableFrom(field.getType())){
            dataFormat.getFormat("yyyy/mm/dd hh:mm");
        }else{
            dataFormat.getFormat("text");
        }
        return dataFormat;
    }
}
