package utils;

public class StringUtils {

    public static String getFirstUpperString(String str){
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
