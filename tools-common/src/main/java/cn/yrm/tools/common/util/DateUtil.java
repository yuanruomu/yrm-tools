package cn.yrm.tools.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author yuanr
 */
public class DateUtil {

    /**
     * 当前时间，GMT 格式字符串 (RFC 1123)
     *
     * @return {String}
     */
    public static String getGmtString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }
}
