package export.base;

import export.ExportBeanChild;
import export.exception.DataNameNullPointException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DataFieldMapper {

    public static String mapperName(Class<?> targetClass){
        DataName dataName = targetClass.getAnnotation(DataName.class);
        String name = dataName == null || dataName.value().equals("")? targetClass.getSimpleName():dataName.value();
        return name;
    }

    public static  List<String> mapperHead(Class<?> targetClass){
        List<String> strings = new ArrayList<>();
        DataName dataName = targetClass.getAnnotation(DataName.class);
        if(dataName != null){
            if(dataName.hasIndex()){
                strings.add("序号");
            }
        }
        Field[] fields = targetClass.getDeclaredFields();
        for(Field field : fields){
            System.out.println("字段:"+field);
            DataFieldName dataFieldName = field.getAnnotation(DataFieldName.class);
            if(dataFieldName == null || dataFieldName.value().equals("")){
                strings.add(field.getName());
            }else{
                strings.add(dataFieldName.value());
            }
        }
        System.out.println(strings);
        return strings;
    }

    public static List<String> mapperNeedHead(Class<?> targetClass){
        List<String> fieldString = new ArrayList<>();
        DataName dataName = targetClass.getAnnotation(DataName.class);
        if(dataName != null && dataName.hasIndex()){
            fieldString.add("序号");
        }
        Field[] fields = targetClass.getDeclaredFields();
        for(Field field : fields){
            DataFieldName dataFieldName = field.getAnnotation(DataFieldName.class);
            if(dataFieldName != null){
                fieldString.add(dataFieldName.value());
            }
        }
        return fieldString;
    }

    public static List<String> getFieldString(Class<?> targetClass){
        List<String> fieldString = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field f : fields) {
            fieldString.add(f.getName());
        }
        return fieldString;
    }


    public static List<Field> getNeedField(Class<?> targetClass){
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        for(Field field : fields){
            DataFieldName dataFieldName = field.getAnnotation(DataFieldName.class);
            if(dataFieldName != null)
                fieldList.add(field);
        }
        return fieldList;
    }



    public static <T> Method[] getFieldMethods(Class<T> targetClass){
        Field[] fields = targetClass.getDeclaredFields();
        Method[] methods = new Method[fields.length];
        try {
            for (int i = 0; i < methods.length; i++) {
                if(Boolean.class.isAssignableFrom(fields[i].getType()) || boolean.class.isAssignableFrom(fields[i].getType())){
                    methods[i] = targetClass.getMethod("is"+getMethodName(fields[i].getName()));
                } else{
                    methods[i] = targetClass.getMethod("get"+getMethodName(fields[i].getName()));
                }

            }
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }
        return methods;
    }

    public static <T> Method getFieldMethod(Class<?> targetClass, String field){
        Method method = null;
        try {
            Field declaredField = targetClass.getDeclaredField(field);
            if(declaredField != null){
                if(Boolean.class.isAssignableFrom(declaredField.getType())
                        || boolean.class.isAssignableFrom(declaredField.getType())){
                    method = targetClass.getMethod("is"+getMethodName(field));
                } else{
                    method = targetClass.getMethod("get"+getMethodName(field));
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static String getMethodName(String fieldName){
        return fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
    }

    public static void main(String[] args) {
       mapperHead(ExportBeanChild.class);
    }
}
