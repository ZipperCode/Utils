package utils;

import annotation.NonNull;
import reflect.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
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
        return value == null || "".equals(value);
    }


    public static boolean isEmpty(Collection<?> collection){
        return (collection == null || collection.isEmpty());
    }

    public static boolean isEmpty(Map map){
        return (map == null || map.isEmpty());
    }

    /**
     * 判断某个对象是否为空，如果isValidAttribute为true表示也验证对象包含NonNull注解的属性
     * @param t
     * @param isValidAttribute
     * @param <T>
     * @return
     */
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

    public static boolean isPattern(String value, String pattern){
        if(isEmpty(value) || isEmpty(pattern))
            return false;
        Matcher matcher = Pattern.compile(pattern).matcher(value);
        return matcher.matches();
    }

    public static boolean matchIp(String ip){
        if(ValidUtils.isEmpty(ip)){
            return false;
        }
        return isPattern(ip,"((?:(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d)\\\\.){3}(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d))");
    }


    public static boolean isDigest(String value){
        if(ValidUtils.isEmpty(value)){
            return false;
        }
        return isPattern(value,"^[+-]?[1-9]\\d*\\.\\d+$");
    }

    public static boolean isEmail(String email){
        if(ValidUtils.isEmpty(email)){
            return false;
        }
        return isPattern(email,"^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    }

    public static boolean isIp(String ip){
        if(ValidUtils.isEmpty(ip)){
            return false;
        }
        return isPattern(ip,"\\d+\\.\\d+\\.\\d+\\.\\d+");
    }

    public static boolean isChinese(String value) {
        if(ValidUtils.isEmpty(value)){
            return false;
        }
        return isPattern(value, "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
    }

    public static boolean isAscii(String value) {
        if(ValidUtils.isEmpty(value)){
            return false;
        }
        return isPattern(value, "^[\\x00-\\xFF]+$");
    }

    public static boolean isUrl(String value){
        if(ValidUtils.isEmpty(value)){
            return false;
        }
        return isPattern(value, "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$");
    }

    public static boolean isPhone(String value){
        if(ValidUtils.isEmpty(value)){
            return false;
        }
        return isPattern(value,"^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$");
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.setString("sss");
        System.out.println(isEmpty(test,true));
    }
}
