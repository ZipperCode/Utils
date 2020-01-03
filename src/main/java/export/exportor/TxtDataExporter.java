package export.exportor;

import export.base.DataExporter;
import export.base.DataFieldMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class TxtDataExporter<T> extends DataExporter<T> {

    private String split = ",";
    private StringBuilder stringBuilder ;
    private File file;
    private FileWriter fileWriter;

    public TxtDataExporter(List<T> dataList,OutputStream outputStream) {
        super(dataList,outputStream);
    }

    public TxtDataExporter(List<T> dataList, OutputStream outputStream, String split, String fileName) {
        super(dataList, outputStream);
        this.split = split;
        initFile(fileName);
    }
    public TxtDataExporter(List<T> dataList, OutputStream outputStream, String fileName) {
        super(dataList, outputStream);
        initFile(fileName);
    }

    public void initFile(String fileName){
        this.file = new File(fileName);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            this.fileWriter = new FileWriter(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OutputStream export() throws IOException {
        outputHead();
        outputBody();
        return outputStream;
    }

    public void outputBody() throws IOException {
        int count = 0;
        for (T t : dataList) {
            stringBuilder.append(outputLine(t)).append("\r\n");
            count++;
            if(count == 100){
                writeString();
                count = 0;
            }
        }
    }

    public String outputLine(T t){
        StringBuilder sb = new StringBuilder();
        if(hasIndex){
            sb.append(++currentRow).append(split);
        }
        for(Field f : fieldList){
            sb.append(f.getName()).append(split);
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }

    @Override
    public void outputHead() {
        stringBuilder = new StringBuilder();
        Iterator<String> iterator = headList.iterator();
        while (iterator.hasNext()){
            stringBuilder.append(iterator.next()).append(split);
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1).append("\r\n");
        try {
            writeString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeString() throws IOException{
//        outputStream.write(stringBuilder.toString().getBytes("utf8"));
        fileWriter.write(stringBuilder.toString());
        stringBuilder.delete(0,stringBuilder.length());
    }

    public void setSplit(String split) { this.split = split;}

    public String getSplit() { return split;}

}
