package utils;

import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author 90604
 * @project utils
 * @date 2020/2/12
 **/
public class DateUtils {


    public static final String DATE_PATTERN = "YYYY-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATE_TIME_PATTERN = "YYYY-MM-dd HH:mm:ss";

    public static String getNowDate(){
        return LocalDate.now().toString();
    }

    /**
     * 判断今年是否闰年
     * @return
     */
    public static boolean isNowLeapYear(){
        return LocalDate.now().isLeapYear();
    }

    /**
     * 判断某个日期是否是闰年
     * @param date 日期
     * @return
     */
    public static boolean isLeapYear(String date){
        return LocalDate.parse(date).isLeapYear();
    }

    public static long compareDate(String date){
        return compareDate(LocalDate.now(),LocalDate.parse(date));
    }

    public static long compareDate(String date1, String date2){
        return compareDate(LocalDate.parse(date1),LocalDate.parse(date2));
    }

    /**
     * 比较两个日期的天数
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static long compareDate(LocalDate date1 , LocalDate date2){
        return date1.until(date2, ChronoUnit.DAYS);
    }

    /**
     * 判断当天是否是生日
     * @param birthday 生日日期字符串
     * @return
     */
    public static boolean isBirthday(String birthday){
        LocalDate now = LocalDate.now();
        return isBirthday(getMonthDay(birthday), MonthDay.of(now.getMonth(), now.getDayOfMonth()));
    }

    /**
     * 判断某一天是否是生日
     * @param birthday 生日日期
     * @param date 某个日期
     * @return
     */
    public static boolean isBirthday(String birthday, String date){
        return isBirthday(getMonthDay(birthday),getMonthDay(date));
    }

    /**
     * 比较两个日期，判断是否相等
     * @param birthday
     * @param monthDay
     * @return
     */
    public static boolean isBirthday(MonthDay birthday, MonthDay monthDay){
        return monthDay.equals(birthday);
    }

    /**
     * 获取某月的某一天
     * @param monthDay
     * @return
     */
    public static MonthDay getMonthDay(String monthDay){
        return MonthDay.parse(monthDay,DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    /**
     * 时间戳转化LocalDateTime
     * @param timestamp 时间戳
     * @return
     */
    public static LocalDateTime getLocalDateTimeByTimestamp(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant,zoneId);
    }

    public static Date getDateByTimestamp(long timestamp){
        return new Date(timestamp);
    }

    /**
     * LocalDateTime转换为时间戳
     * @param localDateTime
     * @return
     */
    public static long getTimestampByLocalDateTime(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * 返回格式化的时间日期
     * @param dateTime 时间日期
     * @return
     */
    public static LocalDateTime parseLocalDateTimeOfPattern(String dateTime){
        return parseLocalDateTimeOfPattern(dateTime,DATE_TIME_PATTERN);
    }

    /**
     * 返回自定义格式化的日期时间
     * @param dateTime
     * @param pattern
     * @return
     */
    public static LocalDateTime parseLocalDateTimeOfPattern(String dateTime, String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    /**
     * 格式化日期时间
     * @param localDateTime
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime){
        return formatLocalDateTime(localDateTime, DATE_TIME_PATTERN);
    }

    /**
     * 格式化自定义的日期时间
     * @param localDateTime 日期时间
     * @param pattern 匹配表达式
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * Date 转 LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转Date
     * @param localDateTime
     * @return
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void main(String[] args) {

//        System.out.println(isBirthday("1997-11-20","2020-11-20"));
//        System.out.println(compareDate("2020-02-13"));
        System.out.println(getLocalDateTimeByTimestamp(System.currentTimeMillis()));
    }

}
