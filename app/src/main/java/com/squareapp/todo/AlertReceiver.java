package com.squareapp.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log

/**
 * Created by Valentin Purrucker on 12.09.2017.
 */
;


public class AlertReceiver extends BroadcastReceiver
{

    private Context context;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;
        createNotification(intent, context);





    }



    private void createNotification(Intent alarmIntent, Context context)
    {

        Intent viewTaskIntent = new Intent(context, MainActivity.class);


        DatabaseHandler db = new DatabaseHandler(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        int taskID = alarmIntent.getIntExtra("Task_ID", 1);


        viewTaskIntent.putExtra("Task_ID", taskID);

        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, viewTaskIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Log.d("AlertReceiver", "Task ID from Intent = " + String.valueOf(taskID));

        TaskItem task = new TaskItem();
        task = db.getTask(taskID);
        if(task.getStatus() == 0)
        {
            task.setStatus(1);
        }
        else
        {
            task.setStatus(0);
        }

        db.updateTask(task);

        mBuilder.setContentIntent(notificationIntent);
        mBuilder.setSmallIcon(R.drawable.ic_access);
        mBuilder.setContentTitle(task.getName());
        mBuilder.setContentText(task.getCategory());
        mBuilder.setTicker(task.getName());


        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(android.support.v7.app.NotificationCompat.PRIORITY_HIGH);
        mBuilder.setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_ALL);

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(task.getId(), mBuilder.build());



        Intent updateIntent = new Intent("REFRESH_BROADCAST");
        LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);

        db.close();



    }



}

