package com.squareapp.todo;

import android.graphics.Color;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Valentin Purrucker on 03.09.2017.
 */

public class DateFormatClass
{

    public static final SimpleDateFormat currentDateFormatForNavigationDrawer = new SimpleDateFormat("EEEE, d");

    public static final SimpleDateFormat userForamt_Date = new SimpleDateFormat("E, MMM dd, yyyy");

    public static final SimpleDateFormat databaseFormat_Time = new SimpleDateFormat("HH:mm");

    public static final SimpleDateFormat timeFormat_w_Marker = new SimpleDateFormat("HH:mm");

    public static final SimpleDateFormat databaseFormat_Date = new SimpleDateFormat("yyyyMMdd");

    public static final SimpleDateFormat futureDateFormat = new SimpleDateFormat("dd-LL-yyyy");

    public static final SimpleDateFormat date_time_Format = new SimpleDateFormat("yyyyMMddHHmm");




    public static Long toLongDateAndTime(Calendar calendar)
    {
        Long time;

        String timeString;

        timeString = date_time_Format.format(calendar.getTime());

        time = Long.parseLong(timeString);

        return time;
    }



    public static String setUserDateToDatabase(Calendar calendar)
    {
        String dbDateString;

        dbDateString = databaseFormat_Date.format(calendar.getTime());

        return dbDateString;
    }

    public static String setDateFromDatePicker(Calendar calendar)
    {
        String date;

        date = userForamt_Date.format(calendar.getTime());


        return date;
    }




    public static Calendar getDateFromDatabase(String date)
    {


        Calendar userDateCalendar = Calendar.getInstance();

        try
        {
            userDateCalendar.setTime(databaseFormat_Date.parse(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        return userDateCalendar;

    }


    public static String getCurrentDateInString(Calendar calendar)
    {
        String currentDateInString;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        currentDateInString = currentDateFormatForNavigationDrawer.format(calendar.getTime());
        Log.d("DateString", currentDateInString);



            if (day >= 11 && day <= 13)
            {
                return currentDateInString +="th";
            }

            switch (day % 10)
            {
                case 1:
                    currentDateInString += "st";
                    break;
                case 2:
                    currentDateInString += "nd";
                    break;
                case 3:
                    currentDateInString += "rd";
                    break;
                default:
                    currentDateInString += "th";
                    break;
            }






        return currentDateInString;
    }


    public static Calendar getCurrentDateInCalendar(String dateString)
    {
        Calendar date = Calendar.getInstance();

        String dateStringInFormat = null;

        dateStringInFormat = dateString.substring(0, dateString.length() -2);

        try
        {
            date.setTime(currentDateFormatForNavigationDrawer.parse(dateStringInFormat));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        return date;
    }


    public static String getTimeFromTimePicker(Calendar timeCalendar)
    {
        String time;

        time = databaseFormat_Time.format(timeCalendar.getTime());


        return time;
    }


    public static String setTimeToDatabase(Calendar timeCalendar)
    {
        String time = null;

        time = databaseFormat_Time.format(timeCalendar.getTime());

        return time;
    }


    public static long getAlarmTime(Calendar date, Calendar time)
    {
        long alarmTime;



        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.YEAR, date.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, date.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));



        alarmTime = calendar.getTimeInMillis();


        return alarmTime;

    }


    public static int setTaskItemAdapterDateDescriptionText(int days)
    {
        int dateText = 0;

        //ToDo set right strings for dateText

        if(days == 1)
        {
            //0 = Tomorrow
            dateText = 0;
        }


        if(days == -1)
        {
            //1 = Yesterday
            dateText = 1;
        }

        if(days == 0)
        {
            //2 = today
            dateText = 2;
        }


        if(days > 1)
        {
            //3 = upcoming
            dateText = 3;
        }

        if(days < -1)
        {
            //4 = done
            dateText = 4;
        }


        return dateText;
    }


    public static String setTaskItemDateText(int description, TaskItem item)
    {
        String dateText;


        Calendar time = Calendar.getInstance();
        Calendar date = Calendar.getInstance();

        try
        {
            time.setTime(databaseFormat_Time.parse(item.getTime()));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        try
        {
            date.setTime(databaseFormat_Date.parse(item.getDate()));
        }
        catch (ParseException e)
        {
            Log.e("Date exception", "Parsing error");
            e.printStackTrace();
        }

        switch (description)
        {
            case 0:
                dateText = timeFormat_w_Marker.format(time.getTime());
                break;

            case 1:
                dateText = timeFormat_w_Marker.format(time.getTime());
                break;

            case 2:
                dateText = timeFormat_w_Marker.format(time.getTime());
                break;

            case 3:
                dateText = futureDateFormat.format(date.getTime());
                break;

            case 4:
                dateText = futureDateFormat.format(date.getTime());
                break;

            default:
                dateText = "";
                break;
        }



        return dateText;
    }


    public static int setTaskItemDateTextColor(int description)
    {

        int color = 0;


        switch (description)
        {
            case 2:
                color = Color.parseColor("#00d6b9");
                break;

            case 1:
                color = Color.parseColor("#00ad96");
                break;

            case 0:
                color = Color.parseColor("#00ad96");
                break;

            case 3:
                color = Color.parseColor("#01aeff");
                break;

            default:
                color = Color.parseColor("#d1d1d1");
                break;
        }



        return color;
    }




}
