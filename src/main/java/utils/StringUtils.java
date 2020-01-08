package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

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
            return false;
        }
        return true;
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
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }

    public static String getNoRepeatString(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String getUuid(){
        return UUID.randomUUID().toString();
    }

    public static String md5Crypt(String srcString){
        return enCrypt(srcString,"MD5");
    }

    public static String shaCrypt(String srcString){
        return enCrypt(srcString,"SHA");
    }

    public static String enCrypt(String srcString,String encryptType){
        if(srcString == null || srcString.length() ==0){
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance(encryptType);
            md.update(srcString.getBytes());
            byte [] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if((0xFF & hash[i]) < 0x10)
                    stringBuffer.append("0"+Integer.toHexString(hash[i]&0xFF));
                else
                    stringBuffer.append(Integer.toHexString(hash[i]&0xFF));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return  stringBuffer.toString();
    }
}
