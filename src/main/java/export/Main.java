package export;

import export.base.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Test test = new Test("1","2","3");
        Test test1 = new Test();
        System.out.println(System.getProperty("java.version"));
        Class<?> clazz = Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            try {
                method.setAccessible(true);
                method.invoke(test);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Arrays.toString(methods));
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            Object o = null;
            try {
                field.setAccessible(true);
                field.set(test1,"haha");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Arrays.toString(fields));
        System.out.println(test1);
    }
}
