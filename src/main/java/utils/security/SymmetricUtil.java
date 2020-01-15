package utils.security;

import utils.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author 90604
 * @project utils
 * @date 2020/1/12
 **/
public class SymmetricUtil {

    public static final String TYPE_AES = "AES";
    public static final int DES_LENGTH = 56;
    public static final String TYPE_DES = "DES";
    public static final String TYPE_DES_3 = "DESEDE";
    public static final String DES_3_MODE = "DESEDE/CBC/PKCS5Padding";
    private final static String iv = "01234567";

    private static final String ENCODING = "UTF-8";


    public static byte[] getSecret(String encryptType,int length) {
        byte [] key = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(encryptType);
            keyGenerator.init(length, new SecureRandom()); //根据算法生成指定大小
            SecretKey secretKey = keyGenerator.generateKey();
            key = secretKey.getEncoded();
        }catch (Exception e){
            e.printStackTrace();
        }
        return key;
    }

    public static String desEncrypt(byte[] key, String plain) {
        String result = null;
        try {
            result = StringUtils.byte2String(crypt(key,plain.getBytes(),TYPE_DES,Cipher.ENCRYPT_MODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String desDecrypt(byte[] key, String cipherText){
        String result = null;
        try {
            result = new String(crypt(key,StringUtils.string2Byte(cipherText),TYPE_DES, Cipher.DECRYPT_MODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String des3EnCrypt(byte key[], String plain){
        try{
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(TYPE_DES_3);
            Key desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance(DES_3_MODE);
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ips);
            byte[] encryptData = cipher.doFinal(plain.getBytes(ENCODING));
            return  StringUtils.byte2String(encryptData);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String des3DeCrypt(byte[] key, String encryptString){
        try{
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            byte[] decryptData = cipher.doFinal(StringUtils.string2Byte(encryptString));
            return new String(decryptData, ENCODING);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] crypt(byte []key, byte[] data,String encryptType, int mode) throws Exception{
        Cipher cipher = Cipher.getInstance(encryptType);
        SecretKey secretKey = new SecretKeySpec(key,encryptType);
        cipher.init(mode,secretKey,new SecureRandom());
        byte res[] = cipher.doFinal(data);
        return res;
    }

    public static String aesEncrypt(byte[] key, String plain){
        String result = null;
        try {
            result = StringUtils.byte2String(crypt(key,plain.getBytes(),TYPE_AES, Cipher.ENCRYPT_MODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String aesDecrypt(byte[] key,String cipherText){
        String result = null;
        try {
            result = new String(crypt(key,StringUtils.string2Byte(cipherText),TYPE_AES, Cipher.DECRYPT_MODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        byte[] secret = getSecret(TYPE_DES_3,112);
        String helloworld = des3EnCrypt(secret, "你好");
        System.out.println("【aes加密后】--"+helloworld);
        String s = des3DeCrypt(secret, helloworld);
        System.out.println("【aes解密后】--"+s);
    }
}
