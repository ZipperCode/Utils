package export.exportor;

import export.ExportBean;
import export.exception.BaseExportException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<ExportBean> export = new ArrayList();
        for (int i = 0; i < 200; i++) {
            export.add(new ExportBean(""+i,"haha-"+i,i));
        }
//        TxtDataExporter<ExportBean> txtDataExporter = new TxtDataExporter(export,null, "D://111//1.txt");
//        try {
//            txtDataExporter.export();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ExcelDataExporter<ExportBean> excelDataExporter = new ExcelDataExporter(export,null,"D://111//1.xls");
        excelDataExporter.initExcel();
        try {
            excelDataExporter.export();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BaseExportException e) {
            e.printStackTrace();
        }
    }
}
