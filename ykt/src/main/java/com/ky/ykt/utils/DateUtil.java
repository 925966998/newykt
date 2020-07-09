package com.ky.ykt.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");

    private final static SimpleDateFormat sdfDayNumber = new SimpleDateFormat("d");

    private final static SimpleDateFormat sdfMonthNumber = new SimpleDateFormat("M");

    private final static SimpleDateFormat sdYMdHmsS = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String dateStamp() {
        try {
            Date d = new Date();
            return String.valueOf(d.getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return sdfYear.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return sdfDay.format(new Date());
    }

    public static String getDay(Date d) {
        return sdfDay.format(d);
    }


    public static String getSdYMdHmsS() {
        return sdYMdHmsS.format(new Date());
    }

    /**
     * 获取YYYY-MM格式
     *
     * @return
     */
    public static String getMonth(Date d) {
        return sdfMonth.format(d);
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }

    public static String getTime(Date d) {
        return sdfTime.format(d);
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws @author luguosui
     * @Title: compareDate
     * @Description: TODO(日期比较 ， 如果s > = e 返回true 否则返回false)
     */
    public static boolean compareDate(String s, String e) {
        if (fomatDate(s) == null || fomatDate(e) == null) {
            return false;
        }
        return fomatDate(s).getTime() >= fomatDate(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long aa = 0;
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24))
                    / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        // System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 得到n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }


    /**
     * 獲取一周的第一天和最後一天
     *
     * @return
     */
    public static Map getWeekDay() {
        Map<String, Date> map = new HashMap<String, Date>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
        map.put("start", getDayBegin(cal.getTime()));
        // 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        map.put("end", getDayBegin(cal.getTime()));
        return map;
    }

    public static Date getDayBegin(Date d) {
        long current = d.getTime();
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零
        return new Date(zero);
    }

    /**
     * 获取一个月的第一天和最后一天
     *
     * @return
     */
    public static Map getMonthDate() {
        Map<String, Date> map = new HashMap<String, Date>();
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
        // 设置时间,当前时间不用设置
        // calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        map.put("start", getDayBegin(calendar.getTime()));
        // 设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        // 打印
        map.put("end", getDayBegin(calendar.getTime()));
        return map;
    }

    /**
     * 获取一年的第一天和最后一天
     *
     * @return
     */
    public static Map getYearDate() {
        Map<String, Date> map = new HashMap<String, Date>();
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
        // 设置时间,当前时间不用设置
        // calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        map.put("start", getDayBegin(calendar.getTime()));
        // 设置日期为本年最后一天
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        // 打印
        map.put("end", getDayBegin(calendar.getTime()));
        return map;
    }

    /**
     * 时间加一天
     *
     * @param date
     * @return
     */
    public static Date addDay(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
        return calendar.getTime(); // 这个时间就是日期往后推一天的结果
    }

    /**
     * 时间加一年
     *
     * @param date
     * @return
     */
    public static Date addYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.YEAR, 1);// 把日期往后增加一天.整数往后推,负数往前移动
        return calendar.getTime(); // 这个时间就是日期往后推一天的结果
    }

    /**
     * 时间减一天
     *
     * @param date
     * @return
     */
    public static Date addMonth(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MONTH, 1);
        return calendar.getTime();
    }
    public static Date getaddMonth(Date date,int num) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MONTH, num);
        return calendar.getTime();
    }

    public static String getDayNumber(Date d) {
        return sdfDayNumber.format(d);
    }

    public static String getMonthNumber(Date d) {
        return sdfMonthNumber.format(d);
    }

    private static String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};

    public static String getWeekNumber(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    @SuppressWarnings("deprecation")
    public static String timeAgo(Date publishTime) {
        Date startTime = publishTime;
        Date endTime = new Date();
        long time = endTime.getTime() - startTime.getTime();
        long day = time / (24 * 60 * 60 * 1000);
        long hour = (time / (60 * 60 * 1000) - day * 24);
        long min = ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
        if (day == 0) {
            if (hour == 0) {
                if (min == 0) {
                    return String.valueOf(s) + "秒前";
                } else {
                    return String.valueOf(min) + "分钟前";
                }
            } else {
                return String.valueOf(hour) + "小时前";
            }
        } else if (day < 4) {
            return String.valueOf(day) + "天前";
        } else {
            return startTime.getMonth() + "月" + startTime.getDay() + "日";
        }
    }


    public static Date addMonth(Date date, int m) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MONTH, m);// 把日期往后增加一天.整数往后推,负数往前移动
        return calendar.getTime(); // 这个时间就是日期往后推一天的结果
    }


    public static Date addDay(Date date, int m) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, m);// 把日期往后增加一天.整数往后推,负数往前移动
        return calendar.getTime(); // 这个时间就是日期往后推一天的结果
    }

    public static int getDay(Date str1, Date str2) {
        long day = 0;
//      long hour = 0;  
//      long min = 0;  
//      long sec = 0;  
        long time1 = str1.getTime();
        long time2 = str2.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
//      hour = (diff / (60 * 60 * 1000) - day * 24);  
//      min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
//      sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  

        return Integer.parseInt(String.valueOf(day));
    }


    public static void main(String[] args) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            System.out.println(getDay(new Date(), fmt.parse("2016-11-03 14:39:50")));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
