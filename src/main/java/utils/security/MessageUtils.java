package utils.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 90604
 * @project utils
 * @date 2020/1/12
 **/
public class MessageUtils {
    public static final String TYPE_MD5 = "MD5";
    public static final String TYPE_SHA_1 = "SHA";
    public static final String TYPE_SHA_256 = "SHA-256";

    public static String md5Crypt(String srcString){
        return enCrypt(srcString,TYPE_MD5);
    }

    public static String shaCrypt(String srcString){
        return enCrypt(srcString,TYPE_SHA_1);
    }

    public static String sha256Crypt(String srcString){return enCrypt(srcString,TYPE_SHA_256);}

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
