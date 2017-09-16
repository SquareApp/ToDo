package com.squareapp.todo;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopeer.itemtouchhelperextension.Extension;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Valentin Purrucker on 03.09.2017.
 */

public class TaskItemAdapter_Normal extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    private Context context;

    private LayoutInflater inflater;

    private ArrayList<TaskItem> mData;

    private Calendar nowCalendar = Calendar.getInstance();

    private Typeface montserratTypeface;

    private int marginLeft, marginRight, marginTop, marginBottom;

    private Resources resources;




    public TaskItemAdapter_Normal(Context context, LayoutInflater inflater, ArrayList<TaskItem> mData, FragmentManager fm)
    {
        this.context = context;

        this.resources = context.getResources();

        this.inflater = inflater;

        this.mData = mData;

        this.nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
        this.nowCalendar.set(Calendar.MINUTE, 0);

        this.montserratTypeface = FontCache.get("fonts/Muli-Regular.ttf", context);

    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.task_list_item_w_textseperator, parent, false);

        TaskItemViewHolder taskItemViewHolder = new TaskItemViewHolder(view);

        return taskItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        TaskItemViewHolder itemViewHolder = (TaskItemViewHolder)holder;

        TaskItem item = this.mData.get(position);

        int taskDateDescriptionNumber;
        taskDateDescriptionNumber = DateFormatClass.setTaskItemAdapterDateDescriptionText(getDateDiff(item.getDate()));

        Calendar dateCalendar = DateFormatClass.getDateFromDatabase(this.mData.get(position).getDate());


        /*
        if(position > 0 && this.mData.get(position).getDate().equals(this.mData.get(position -1).getDate()))
        {
            itemViewHolder.headerSeparator.setVisibility(View.GONE);
        }
        else
        {
            String date = DateFormatClass.getCurrentDateInString(dateCalendar);
            if(DateFormatClass.setUserDateToDatabase(dateCalendar).equals(DateFormatClass.setUserDateToDatabase(nowCalendar)))
            {
                itemViewHolder.headerSeparator.setText("Today");
            }
            else
            {
                itemViewHolder.headerSeparator.setText(date);
            }

            itemViewHolder.headerSeparator.setVisibility(View.VISIBLE);
        }

        */


        itemViewHolder.taskNameText.setText(item.getName());
        itemViewHolder.taskCategoryText.setText(item.getCategory());





        //TaskItemCard: Set up date text and description
        itemViewHolder.taskDateDescriptionText.setText(getTaskDateDescriptionByNumber(taskDateDescriptionNumber));
        itemViewHolder.taskDateText.setText(DateFormatClass.setTaskItemDateText(taskDateDescriptionNumber, item));
        itemViewHolder.taskDateDescriptionText.setTextColor(DateFormatClass.setTaskItemDateTextColor(taskDateDescriptionNumber));

    /*
        if(taskDateDescriptionNumber == 1 || taskDateDescriptionNumber == 4)
        {
            itemViewHolder.taskNameText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskDateDescriptionText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskDateDescriptionText.setTextColor(Color.parseColor("#d1d1d1"));
            itemViewHolder.taskDateText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskCategoryText.setVisibility(View.INVISIBLE);
            itemViewHolder.taskCategoryIcon.setVisibility(View.INVISIBLE);
            itemViewHolder.taskItemDoneView.setVisibility(View.VISIBLE);
        }

        */




        if(item.getStatus() == 0)
        {
            //task not done yet

            itemViewHolder.taskStatusView.setBackground(itemViewHolder.uncheckedDrawable);
        }
        else
        {
            //task done
            itemViewHolder.taskStatusView.setBackground(itemViewHolder.checkedDrawable);
            itemViewHolder.taskNameText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskDateDescriptionText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskDateDescriptionText.setTextColor(Color.parseColor("#d1d1d1"));
            itemViewHolder.taskDateText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskCategoryText.setVisibility(View.INVISIBLE);
            itemViewHolder.taskCategoryIcon.setVisibility(View.INVISIBLE);
            itemViewHolder.taskItemDoneView.setVisibility(View.VISIBLE);
        }






    }


    private String getTaskDateDescriptionByNumber(int number)
    {
        String dateDescription = null;


        if(number == 0)
        {
            dateDescription = context.getResources().getString(R.string.tomorrow_date_description);
        }

        if(number == 1)
        {
            dateDescription = context.getResources().getString(R.string.yesterday_date_description);
        }

        if(number == 2)
        {
            dateDescription = context.getResources().getString(R.string.today_date_description);
        }

        if(number == 3)
        {
            dateDescription = context.getResources().getString(R.string.upcoming_task);
        }


        if(number == 4)
        {
            dateDescription = context.getResources().getString(R.string.done_date_description);
        }




        return dateDescription;


    }


    public int getDateDiff(String itemDate)
    {

        int days = 0;


        try
        {
            days =  (int)TimeUnit.DAYS.
                    convert(DateFormatClass.databaseFormat_Date.parse(itemDate).getTime() - DateFormatClass.databaseFormat_Date.parse(DateFormatClass.setUserDateToDatabase(nowCalendar))
                            .getTime(), TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            days = 0;
        }


        return days;
    }


    @Override
    public int getItemCount()
    {
        return mData.size();
    }





/*
    private void setMarginInPixelFromDP(float dp_margin_Left, float dp_margin_Top, float dp_margin_Right, float dp_margin_Bottom)
    {
        this.marginLeft = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp_margin_Left,
                resources.getDisplayMetrics()
        );

        this.marginTop = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp_margin_Top,
                resources.getDisplayMetrics()
        );

        this.marginRight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp_margin_Right,
                resources.getDisplayMetrics()
        );

        this.marginBottom = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp_margin_Bottom,
                resources.getDisplayMetrics()
        );
    }

    */




    class TaskItemViewHolder extends RecyclerView.ViewHolder implements Extension
    {

        float actionWidth;

        TextView headerSeparator;
        TextView taskNameText;
        TextView taskCategoryText;
        TextView taskDateDescriptionText;
        TextView taskDateText;

        ImageView taskCategoryIcon;
        ImageView taskItemDoneView;

        View taskStatusView;

        CardView taskItem;


        Drawable uncheckedDrawable;
        Drawable checkedDrawable;

        public TaskItemViewHolder(View itemView)
        {
            super(itemView);


            headerSeparator = (TextView)itemView.findViewById(R.id.dateSeperator);
            taskNameText = (TextView)itemView.findViewById(R.id.taskNameText);
            taskCategoryText = (TextView)itemView.findViewById(R.id.taskCategoryText);
            taskDateDescriptionText = (TextView)itemView.findViewById(R.id.taskDateDescriptionText);
            taskDateText = (TextView)itemView.findViewById(R.id.taskDateText);

            taskCategoryIcon = (ImageView)itemView.findViewById(R.id.taskCategoryIcon);
            taskItemDoneView = (ImageView)itemView.findViewById(R.id.taskItemDoneView);

            taskStatusView = (View) itemView.findViewById(R.id.taskStatusView);

            taskItem = (CardView)itemView.findViewById(R.id.taskItemCard);

            checkedDrawable = context.getDrawable(R.drawable.taskitem_checked);
            uncheckedDrawable = context.getDrawable(R.drawable.taskitem_uncheck);



            setTypeface();
        }




        private void setTypeface()
        {
            headerSeparator.setTypeface(montserratTypeface);
            taskNameText.setTypeface(montserratTypeface);
            taskDateText.setTypeface(montserratTypeface);
            taskCategoryText.setTypeface(montserratTypeface);
            taskDateDescriptionText.setTypeface(montserratTypeface);
        }


        //set return value to the size you want to be as the drawable which is drawn ontop of your recyclerview when swiped
        @Override
        public float getActionWidth()
        {
            actionWidth = itemView.getWidth() / 4.5f;
            return actionWidth;
        }
    }









}
