package com.squareapp.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;



public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        DatabaseHandler myDb = new DatabaseHandler(context);

        ArrayList<TaskItem> items = new ArrayList<>();

        items = myDb.getAllTasksByStatus(0);

        myDb.close();


        for(int i = 0; i < items.size(); i++)
        {

            TaskItem taskItem = items.get(i);

            int id = taskItem.getId();


            Intent alarmIntent = new Intent(context, AlertReceiver.class);
            alarmIntent.putExtra("Task_ID", id);

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    DateFormatClass.getAlarmTime(DateFormatClass.getDateFromDatabaseAsCalendar(taskItem.getDate()), DateFormatClass.getTimeFromDatabaseAsCalendar(taskItem.getTime())),
                    PendingIntent.getBroadcast(context, alarmIntent.getIntExtra("Task_ID", 0),
                            alarmIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }





    }
}
