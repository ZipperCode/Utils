package export.base;

import export.exception.BaseExportException;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DataExporter<T> {
    // 头字段别名
    protected List<String> headList;
    // 对象字段
    protected List<Field> fieldList;
    // 数据
    protected List<T> dataList;
    // 页大小
    protected int pageSize = 1000;
    // 当前行
    protected int currentRow = 0;
    // 输出流
    protected OutputStream outputStream;
    // 是否生成下标
    protected boolean hasIndex = false;
    // 默认使用注解的字段
    protected boolean useNeedColumn = true;
    // 输出文件名
    protected String fileName;

    public DataExporter(List<T> dataList, OutputStream outputStream, String fileName) {
        this.headList = new ArrayList<>();
        this.fieldList = new ArrayList<>();
        this.dataList = dataList;
        this.outputStream = outputStream;
        this.fileName = fileName;
        init();
    }

    public void init(){
        try {
            initHead();
        } catch (BaseExportException e) {
            e.printStackTrace();
        }
    }

    private void initHead() throws BaseExportException{
        T t = this.dataList.get(0);
        if(t == null){
            throw new BaseExportException("数据导出前错误，【initHead】 t == null");
        }
        if(useNeedColumn){
            this.headList.addAll(DataFieldMapper.mapperNeedHead(t.getClass()));
            this.fieldList.addAll(DataFieldMapper.getNeedField(t.getClass()));
        }else{
            this.headList.addAll(DataFieldMapper.mapperHead(t.getClass()));
            this.fieldList.addAll(Arrays.asList(t.getClass().getDeclaredFields()));
        }
        // 读取是否使用序号列
        DataName dataName = t.getClass().getAnnotation(DataName.class);
        if(dataName != null){
            this.hasIndex = dataName.hasIndex();
        }
    }

    public Object getValue(T t, String field){
        if(t == null)
            return null;
        try {
            Method fieldMethod = DataFieldMapper.getFieldMethod(t.getClass(), field);
            if(fieldMethod == null){
                return null;
            }
            return fieldMethod.invoke(t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            Field declaredField = t.getClass().getDeclaredField(field);
            if(declaredField == null){
                return null;
            }
            return declaredField.get(t);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract OutputStream export() throws Exception;

    public abstract void outputHead();

    public boolean isHasIndex() { return hasIndex;  }

    public void setHasIndex(boolean hasIndex) { this.hasIndex = hasIndex; }
}
