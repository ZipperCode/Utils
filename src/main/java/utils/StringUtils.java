package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static String chars = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    public static  String randomString(int len){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < len; i++) {
            stringBuffer.append(chars.charAt((int)(Math.random()*(chars.length()-1))));
        }
        return stringBuffer.toString();
    }

    public static boolean checkEquals(String str1,String str2){
        if(str1 == null || str1.equals("") || str2 == null || str2.equals("")){
            return false;
        }
        return str1.equals(str2);
    }

    public static boolean isEmpty(String string){
        if(string == null || "".equals(string)){
            return true;
        }
        return false;
    }

    public static String getFileExt(String fileName){
        if (!isEmpty(fileName))
            return "";
        int start = fileName.lastIndexOf(".");
        if(start == -1)
            return "";
        return fileName.substring(start);
    }

    public static String getFirstUpperString(String str){
        if(isEmpty(str)){
            return "";
        }
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }

    public static String getNoRepeatString(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String getUuid(){
        return UUID.randomUUID().toString();
    }

    public static String trim(String string){
        if(ValidUtils.isEmpty(string))
            return "";
        return string.replaceAll(" ",string);
    }

    public static String byte2String(byte []data){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            if((0xFF & data[i]) < 0x10)
                stringBuffer.append("0"+Integer.toHexString(data[i]&0xFF));
            else
                stringBuffer.append(Integer.toHexString(data[i]&0xFF));
        }
        return stringBuffer.toString();
    }

    public static byte[] string2Byte(String string){
        byte result[] = new byte[string.length()/2];
        for (int i = 0; i < result.length; i++) {
            String s = string.substring(i*2,i*2+2);
            int num = Integer.parseInt(s, 16);
            result[i] = (byte)num;
        }
        return result;
    }

    public static int checkPasswordStrong(String password){
        if(StringUtils.isEmpty(password)){
            return 0;
        }
        int score = 0;
        // 简单只包含数字或者字母小写或者大写
        String simpleRegex = "^(?:\\d+|[a_z]+|[A_Z]+)";
        // 中等包含大小写和数字或者只包含大小写
        String middleRegex = "^(?:\\w+)";
        // 复杂包含数字字母大小写和一些符号
        String complexRegex = "^(?:([\\w!@#$%^&*]+))$";
        // 连续重复匹配
        String repeatRegex = "(\\w)\\1+";

        if(password.length() < 6){
            return password.length();
        }else{
            if(password.matches(simpleRegex)){
                score = 30;
            }
            if(password.matches(middleRegex)){
                score = 60;
            }
            if(password.matches(complexRegex)){
                score = 90;
            }
            Matcher repeatMatcher = Pattern.compile(repeatRegex).matcher(password);
            while (repeatMatcher.find()){
                score -= 5;
            }
        }
        return score;
    }

    public static void main(String[] args) {
        byte[] b = new byte[]{12,34,56,78,-10};
        String s1 = byte2String(b);
        System.out.println(s1);
        System.out.println(Arrays.toString(string2Byte(s1)));

        System.out.println(checkPasswordStrong("aA11A!!vds"));
    }
}
