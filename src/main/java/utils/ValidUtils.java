package utils;

import annotation.NonNull;
import reflect.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 90604
 * @project utils
 * @date 2020/1/7
 **/
public class ValidUtils {

    public static boolean isEmpty(int value){
        return value == 0;
    }

    public static boolean isEmpty(String value){
        return (value == null || "".equals(value)) ? true : false;
    }

    public static boolean isEmpty(Collection<?> collection){
        return (collection == null || collection.isEmpty());
    }

    public static boolean isEmpty(Map map){
        return (map == null || map.isEmpty());
    }

    public static <T> boolean isEmpty(T t, boolean isValidAttribute){
        if(t == null){
            return true;
        }
        if(!isValidAttribute){ // 是否验证类中属性
            return false;
        }
        boolean result = false;
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(NonNull.class)){
                NonNull nonNull = field.getAnnotation(NonNull.class);
                if(nonNull == null){
                    result =  true;
                    break;
                }
                try {
                    // String methodName = StringUtils.getFirstUpperString(field.getName());
                    // Method getMethod = t.getClass().getMethod(methodName);
                    // if(getMethod == null){
                    //     result = true; // get方法不存在
                    // }
                    // Object value = getMethod.invoke(t);
                    field.setAccessible(true);
                    Object value = field.get(t);
                    // 数据类都使用包装类,不使用基本数据类型
                    if(value == null){
                        result =  true;
                        break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static boolean isDigest(String value){
        return isPattern(value,"^[+-]?[1-9]\\d*\\.\\d+$");
    }

    public static boolean isEmail(String value){
        return isPattern(value,"^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    }

    public static boolean isIp(String value){
        return isPattern(value,"\\d+\\.\\d+\\.\\d+\\.\\d+");
    }

    public static boolean matchIp(String ip){
        return isPattern(ip,"((?:(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d)\\\\.){3}(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d))");
    }

    public static boolean isPattern(String value, String pattern){
        if(isEmpty(value) || isEmpty(pattern))
            return false;
        Matcher matcher = Pattern.compile(pattern).matcher(value);
        return matcher.matches();
//        return value.matches(pattern);
    }

    public static boolean isChinese(String value)
    {
        return isPattern(value, "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
    }

    public static boolean isAscii(String value)
    {
        return isPattern(value, "^[\\x00-\\xFF]+$");
    }

    public static boolean isUrl(String value){
        return isPattern(value, "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$");
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.setString("sss");
        System.out.println(isEmpty(test,true));
    }
}
