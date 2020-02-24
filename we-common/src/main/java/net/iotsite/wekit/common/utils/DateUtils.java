package net.iotsite.wekit.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private static final String STR_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String strFormat(Date time) {
        return format(time, STR_FORMAT);
    }

    public static String format(long time, String format) {
        return format(new Date(time), format);
    }

    public static String format(Date time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(time);
    }

    public static long timeDiff(Date d1, Date d2) {
        return timeDiff(d1.getTime(), d2.getTime());
    }

    public static long timeDiff(long d1, long d2) {
        return d1 - d2;
    }

}
