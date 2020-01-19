package utils.http.persistent;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.http.cookie.Cookie;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class CookieSerializable implements Serializable {
    /** Cookie name */
    private String name;

    /** Cookie attributes as specified by the origin server */
    private Map<String, String> attribs;

    /** Cookie value */
    private String value;

    /** Comment attribute. */
    private String  cookieComment;

    /** Domain attribute. */
    private String  cookieDomain;

    /** Expiration {@link Date}. */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date cookieExpiryDate;

    /** Path attribute. */
    private String cookiePath;

    /** My secure flag. */
    private boolean isSecure;

    /** The version of the cookie specification I was created from. */
    private int cookieVersion;

    private Date creationDate;

    public CookieSerializable() {
    }

    public CookieSerializable(Cookie cookie){
        this.name = cookie.getName();
        this.cookieComment = cookie.getComment();
        this.cookieDomain = cookie.getDomain();
        this.cookieExpiryDate = cookie.getExpiryDate();
        this.cookiePath = cookie.getPath();
        this.cookieVersion = cookie.getVersion();
        this.value = cookie.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttribs() {
        return attribs;
    }

    public void setAttribs(Map<String, String> attribs) {
        this.attribs = attribs;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCookieComment() {
        return cookieComment;
    }

    public void setCookieComment(String cookieComment) {
        this.cookieComment = cookieComment;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public Date getCookieExpiryDate() {
        return cookieExpiryDate;
    }

    public void setCookieExpiryDate(Date cookieExpiryDate) {
        this.cookieExpiryDate = cookieExpiryDate;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    public int getCookieVersion() {
        return cookieVersion;
    }

    public void setCookieVersion(int cookieVersion) {
        this.cookieVersion = cookieVersion;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
