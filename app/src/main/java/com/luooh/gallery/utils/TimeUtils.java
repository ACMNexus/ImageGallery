package com.luooh.gallery.utils;

import android.content.Context;
import android.text.format.DateFormat;

import com.luooh.gallery.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Luooh on 2017/8/3.
 */
public class TimeUtils {

    public static String formatTime(String formatType, long time) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        String result = format.format(new Date(time));
        return result;
    }

    public static long parseTime(String formatType, String time) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String judgeTime(Context context, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = sdf.format(calendar.getTime());
        String nowadays = sdf.format(System.currentTimeMillis());
        String formatTime = sdf.format(new Date(time));
        if (nowadays.equals(formatTime)) {
            return context.getString(R.string.format_time_today);
        } else if (yesterday.equals(formatTime)) {
            return context.getString(R.string.format_time_yesterday);
        }
        java.text.DateFormat mediumDateFormat = DateFormat.getMediumDateFormat(context);
        return mediumDateFormat.format(time);
    }

}
