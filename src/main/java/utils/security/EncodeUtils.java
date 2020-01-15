package utils.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @author 90604
 * @project utils
 * @date 2020/1/7
 **/
public class EncodeUtils {

    private static final String DEFAULT_ENCODE = "UTF-8";

    /**
     * base64 编码
     * @param input
     * @return
     */
    public static String base64Encode(byte[] input){
        return new String(Base64.getEncoder().encode(input));
    }

    /**
     * base64 解码
     * @param input
     * @return
     */
    public static String base64Decode(byte[] input){
        return new String(Base64.getDecoder().decode(input));
    }

    /**
     * url 编码 使用默认UTF-8编码
     * @param string
     * @return
     */
    public static String urlEncode(String string){
        String encodeString = null;
        try {
            encodeString = URLEncoder.encode(string,DEFAULT_ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    /**
     * url 解码 使用默认UTF-8编码
     * @param string
     * @return
     */
    public static String urlDecode(String string){
        String decodeString = null;
        try {
            decodeString = URLDecoder.decode(string,DEFAULT_ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodeString;
    }
}
