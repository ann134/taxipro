package ru.sigmadigital.taxipro.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {


    //get date
    public static Date getDateFromStringUTC(String datetime) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            return simpleDateFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateFromStringUTCWithSeconds(String datetime) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            return simpleDateFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateFromStringUTCTimeZone(String datetime) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return simpleDateFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    //getString
    public static String getStringDdMmmmEromDate(Date date) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        return outputDateFormat.format(date);
    }

    public static String getStringDdMmmmEeFromDate(Date date) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM, EE", Locale.getDefault());
        return outputDateFormat.format(date);
    }

    public static String getStringSimpleTimeFromDate(Date date) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return outputDateFormat.format(date);
    }

    public static String getStringUTCFromDate(Date date) {
        //SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return outputDateFormat.format(date);
    }


    public static String getStringDateTimeFromDate(Date date) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH:mm dd.MM", Locale.getDefault());
        return outputDateFormat.format(date);
    }

    public static String getSimpleTime(Date date) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return outputDateFormat.format(date);
    }

    public static String getSimpleDate(Date date) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return outputDateFormat.format(date);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return outputDateFormat.format(date1).equals(outputDateFormat.format(date2));
    }

    public static String getDay(Date date) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return outputDateFormat.format(date);
    }

    public static String getMonth(Date date) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        return outputDateFormat.format(date);
    }



    //time
    public static String getHoursDifference(Date startDate, Date endDate) {
        long milliseconds = endDate.getTime() - startDate.getTime();

        // 3 600 секунд = 60 минут = 1 час
        int hours = (int) (milliseconds / (60 * 60 * 1000));
       /* System.out.println("Разница между датами в часах: " + hours);*/

        return hours+"";
    }

    public static int getMinutesDifference(Date startDate, Date endDate) {
        long milliseconds = endDate.getTime() - startDate.getTime();

        // 60 000 миллисекунд = 60 секунд = 1 минута
        int minutes = (int) (milliseconds / (60 * 1000));

        return minutes;
    }


    // calendar
    public static Date datePlusDays(Date date, int days) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, days);
        return instance.getTime();
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
        }

        return 7;
    }

}
