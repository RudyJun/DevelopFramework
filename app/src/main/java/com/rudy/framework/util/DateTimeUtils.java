package com.rudy.framework.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Seconds;
import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by RudyJun on 2016/11/23.
 */
public class DateTimeUtils {

    private final static String TAG = "DateTimeUtils";
    private static DateTimeUtils util;

    public static DateTimeUtils getInstance() {

        if (util == null) {
            util = new DateTimeUtils();
        }

        return util;

    }

    private DateTimeUtils() {
        super();
    }

    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long ONE_WEEK = 604800000L;

    public String DateToString(Date date, String parttern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(parttern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }


    /**
     * 获取SimpleDateFormat
     *
     * @param parttern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
        try {
            return new SimpleDateFormat(parttern);
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 获取两个时间差的描述， 类似 2年前，3月前，2小时前， 3分钟前， 刚刚
     *
     * @param from
     * @param to   当前时间
     * @return
     */
    public String getUpdateDateString(Date from, Date to) {
        DateSpace dateSpace = getMaxUnitSpace(from, to);
        long delta = to.getTime() - from.getTime();
        switch (dateSpace.type) {
            case Calendar.YEAR:
                if (dateSpace.space > 1) {
                    return DateTimeUtils.getInstance().DateToString(from, "yyyy-MM-dd");
                }
            case Calendar.MONTH:
                return DateTimeUtils.getInstance().DateToString(from, "MM-dd");
            case Calendar.DATE:
                if (dateSpace.space < 2) {
                    if (dateSpace.space >= 1) {
                        return "昨天" + DateTimeUtils.getInstance().DateToString(from, "HH:mm");
                    }
                } else {
                    return DateTimeUtils.getInstance().DateToString(from, "MM-dd HH:mm");
                }
            case Calendar.HOUR:
                if (dateSpace.space > 1) {
                    return "今天" + DateTimeUtils.getInstance().DateToString(from, "HH:mm");
                } else {
                    if (delta < 1L * ONE_HOUR) {
                        long minutes = toMinutes(delta);
                        return minutes + "分钟前";
                    } else {
                        return "今天" + DateTimeUtils.getInstance().DateToString(from, "HH:mm");
                    }
                }
            case Calendar.MINUTE:
                if (dateSpace.space > 1) {
                    return dateSpace.space + "分钟前";
                }
            default:
                return "刚刚";
        }
    }


    private static DateSpace getMaxUnitSpace(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);

        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        DateSpace dateSpace = new DateSpace();

        dateSpace.space = to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
        if (dateSpace.space > 0) {
            dateSpace.type = Calendar.YEAR;
            return dateSpace;
        }

        dateSpace.space = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        if (dateSpace.space > 0) {
            dateSpace.type = Calendar.MONTH;
            return dateSpace;
        }

        dateSpace.space = to.get(Calendar.DATE) - from.get(Calendar.DATE);
        if (dateSpace.space > 0) {
            dateSpace.type = Calendar.DATE;
            return dateSpace;
        }

        dateSpace.space = to.get(Calendar.HOUR_OF_DAY) - from.get(Calendar.HOUR_OF_DAY);
        if (dateSpace.space > 0) {
            dateSpace.type = Calendar.HOUR;
            return dateSpace;
        }

        dateSpace.space = to.get(Calendar.MINUTE) - from.get(Calendar.MINUTE);
        if (dateSpace.space > 0) {
            dateSpace.type = Calendar.MINUTE;
            return dateSpace;
        }

        return dateSpace;
    }

    private static class DateSpace {
        int space;

        int type;
    }


    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


    public static int offsetSeconds(DateTime beginTime, DateTime endTime){
        return Seconds.secondsBetween(beginTime, endTime).getSeconds();
    }

    public static int offsetDays(DateTime beginTime, DateTime endTime){
        return Days.daysBetween(beginTime, endTime).getDays();
    }

    public static int offsetYears(DateTime beginTime, DateTime endTime){
        return Years.yearsBetween(beginTime, endTime).getYears();
    }


}