package com.jwt.demo.util.date;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author jiangwentao
 * @version 1.0.0
 * @className DateUtil.java
 * @description 时间工具类
 * @createTime 2022年03月24日 10:36
 */
public class DateUtil {

    /**
     * 日期格式(yyyyMM)
     */
    public static final String YYYYMM_EN = "yyyyMM";

    /**
     * 日期格式(yyyy-MM)
     */
    public static final String YYYY_MM_EN = "yyyy-MM";

    /**
     * 日期格式(yyyy-MM-dd)
     */
    public static final String YYYY_MM_DD_EN = "yyyy-MM-dd";

    /**
     * 日期格式(yyyy-MM-dd HH:mm:ss)
     */
    public static final String YYYY_MM_DD_HH_MM_SS_EN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式(2022-03-23 16:40:04 055)
     */
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss SSS";

    /**
     * 时间格式(20220323164004055)
     */
    public static final String YYYY_MM_DD_HH_MM_SS_SSS_2 = "yyyyMMddHHmmssSSS";




    public static void main(String[] args) {


        System.out.println(stampToDate("1662292800000"));

    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 获取当前时间指定多少天以前的时间
     *
     * @param days 指定天数 正数表示多少天以前
     * @return 日期
     */
    public static Instant getDateBeforeDays(int days) {
        return Instant.now().minus(days, ChronoUnit.DAYS);
    }

    /**
     * 获取当前时间指定多少天以后的时间
     *
     * @param days 指定天数 正数表示多少天以后
     * @return 日期
     */
    public static Instant getDateAfterDays(int days) {
        return Instant.now().plus(days, ChronoUnit.DAYS);
    }

    /**
     * 获取指定格式的dateformat
     */
    public static DateFormat getSimpleDateFormat(String dateFormat) {
        return new SimpleDateFormat(dateFormat);
    }

    /**
     * 获取当系统前时间的指定格式String
     */
    public static String getCurrentDateString(String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    /**
     * Date类型时间转换成指定格式String
     */
    public static String convertDateToFormat(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * String类型时间转换成Date类型，dateString必须是dateFormat格式
     */
    public static Date convertFormatToDate(String dateString, String dateFormat) {
        try {
            return new SimpleDateFormat(dateFormat).parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 得到两个Date类型时间之间的差值，单位：毫秒
     */
    public static Long getTimeDiffMilli(Date startDate, Date endDate) {
        return Math.abs(endDate.getTime() - startDate.getTime());
    }

    /**
     * 比较两个日期的大小，date1大于date2 返回1  date1小于date2 返回-1 相等返回0
     */
    public static int compareStringDate(String date1, String date2) throws Exception {
        DateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_EN);
        Date dt1 = df.parse(date1);
        Date dt2 = df.parse(date2);
        return Long.compare(dt1.getTime(), dt2.getTime());
    }

    /**
     * 得到两个Date类型时间之间的差值，单位：秒
     */
    public static Long getTimeDiffSecond(Date startDate, Date endDate) {
        return Math.abs(endDate.getTime() - startDate.getTime()) / 1000;
    }

    /**
     * 获取当前日期seconds秒后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterSeconds(int seconds, String dateFormat) {
        return getDateStringAfterSeconds(new Date(), seconds, dateFormat);
    }

    /**
     * 获取指定日期seconds秒后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterSeconds(Date date, int seconds, String dateFormat) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        now.add(Calendar.SECOND, seconds);
        return convertDateToFormat(now.getTime(), dateFormat);
    }

    /**
     * 获取当前日期minutes分钟后的一个dateFormat格式的字符串
     */
    public static String getDateAfterMins(int minutes, String dateFormat) {
        return getDateAfterMinutes(new Date(), minutes, dateFormat);
    }


    /**
     * 获取指定日期minutes分钟后的一个dateFormat格式的字符串
     */
    public static String getDateAfterMinutes(Date date, int minutes, String dateFormat) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        now.add(Calendar.SECOND, minutes * 60);
        return convertDateToFormat(now.getTime(), dateFormat);
    }

    /**
     * 获取当前日期hours小时后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterHours(int hours, String dateFormat) {
        return getDateStringAfterHours(new Date(), hours, dateFormat);
    }

    /**
     * 获取指定日期hours小时后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterHours(Date date, int hours, String dateFormat) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        now.add(Calendar.HOUR_OF_DAY, hours);
        return convertDateToFormat(now.getTime(), dateFormat);
    }

    /**
     * 获取当前日期days天后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterDays(int days, String dateFormat) {
        return getDateStringAfterDays(new Date(), days, dateFormat);
    }

    /**
     * 获取指定日期days天后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterDays(Date date, int days, String dateFormat) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        now.add(Calendar.DATE, days);
        return convertDateToFormat(now.getTime(), dateFormat);
    }

    /**
     * 获取当前日期months月后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterMonths(int months, String dateFormat) {
        return getDateStringAfterMonths(new Date(), months, dateFormat);
    }

    /**
     * 获取指定日期months月后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterMonths(Date date, int months, String dateFormat) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        now.add(Calendar.MONTH, months);
        return convertDateToFormat(now.getTime(), dateFormat);
    }

    /**
     * 获取当前日期years年后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterYears(int years, String dateFormat) {
        return getDateStringAfterYears(new Date(), years, dateFormat);
    }

    /**
     * 获取指定日期years年后的一个dateFormat格式的字符串
     */
    public static String getDateStringAfterYears(Date date, int years, String dateFormat) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        now.add(Calendar.YEAR, years);
        return convertDateToFormat(now.getTime(), dateFormat);
    }

    /**
     * 获取Date中的秒
     */
    public static int getSecondOfDate(Date date) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        return now.get(Calendar.SECOND);
    }

    /**
     * 获取Date中的分钟
     */
    public static int getMinuteOfDate(Date date) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        return now.get(Calendar.MINUTE);
    }

    /**
     * 获取Date中的小时(24小时)
     */
    public static int getHourOfDate(Date date) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        return now.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取指定日期天，1-31日
     */
    public static int getDayOfDate(Date date) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        return now.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定日期月份，1-12月
     */
    public static int getMonthOfDate(Date date) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        return now.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定日期年份，如19xx，20xx
     */
    public static int getYearOfDate(Date date) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        return now.get(Calendar.YEAR);
    }

    /**
     * 获得系统当前月份的天数
     */
    public static int getDaysOfCurentMonth() {
        Date date = Calendar.getInstance().getTime();
        return getDaysOfDateMonth(date);
    }

    /**
     * 获得指定日期月份的天数
     */
    public static int getDaysOfDateMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * 得到当前是星期几
     */
    public static String getDayOfWeek() {
        final String[] dayChaneseNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        final String[] dayEnglishNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String dateString = getCurrentDateString(YYYY_MM_DD_EN);
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayChaneseNames[dayOfWeek - 1];
    }

    /**
     * 得到本周周(*)，yyyy-MM-dd格式
     */
    public static String getDayOfWeek(int num) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        calendar.add(Calendar.DATE, -dayOfWeek + num);
        return convertDateToFormat(calendar.getTime(), YYYY_MM_DD_EN);
    }

    /**
     * 得到本周周一，yyyy-MM-dd格式
     */
    public static String getMondayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        calendar.add(Calendar.DATE, -dayOfWeek + 1);
        return convertDateToFormat(calendar.getTime(), YYYY_MM_DD_EN);
    }

    /**
     * 得到本周周日的日期，yyyy-MM-dd格式
     */
    public static String getSundayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        calendar.add(Calendar.DATE, -dayOfWeek + 7);
        return convertDateToFormat(calendar.getTime(), YYYY_MM_DD_EN);
    }

    /**
     * 按照指定格式，将Instant类型的时间转成String
     */
    public static String getDateStringFromInstant(Instant instant, String dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat).withZone(ZoneId.systemDefault());
        return dateTimeFormatter.format(instant);
    }

    /**
     * Instant类型转换成Date
     */
    public static Date convertInstantToDate(Instant instant) {
        BigInteger milis = BigInteger.valueOf(instant.getEpochSecond()).multiply(BigInteger.valueOf(1000));
        milis = milis.add(BigInteger.valueOf(instant.getNano()).divide(BigInteger.valueOf(1_000_000)));
        return new Date(milis.longValue());
    }

    /**
     * 获取指定Instant类型时间最小时间，如Thu Mar 24 00:00:00 CST 2022
     */
    public static Instant getMinInstantOfDay(Instant instant) {
        return LocalDateTime.of(instant.atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.MIN).toInstant(ZoneOffset.of("+8"));
    }

    /**
     * 获取指定Instant类型时间最大时间，如Thu Mar 24 23:59:59 CST 2022
     */
    public static Instant getMaxInstantOfDay(Instant instant) {
        return LocalDateTime.of(instant.atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.MAX).toInstant(ZoneOffset.of("+8"));
    }

    /**
     * 计算两个Instant类型时间之间的差值，单位：毫秒
     */
    public static Long getTimeDiffMilli(Instant startTime, Instant endTime) {
        return Math.abs(endTime.toEpochMilli() - startTime.toEpochMilli());
    }

    /**
     * 两个时间（Instant类型）是否相等
     */
    public static boolean isInstantTimeEqual(Instant time1, Instant time2) {
        return time1.toEpochMilli() == time2.toEpochMilli();
    }
}
