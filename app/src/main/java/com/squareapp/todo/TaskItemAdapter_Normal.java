package com.squareapp.todo;

import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;



public class TaskItemAdapter_Normal extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{



    private Context context;

    private LayoutInflater inflater;

    private ArrayList<TaskItem> mData;

    private Calendar nowCalendar = Calendar.getInstance();

    private Typeface montserratTypeface;

    private int marginLeft, marginRight, marginTop, marginBottom;

    private Resources resources;

    private DatabaseHandler myDb;

    private MainActivity mainActivity;

    private AllTasks_Fragment allTasks_fragment;




    public TaskItemAdapter_Normal(Context context, LayoutInflater inflater, ArrayList<TaskItem> mData, FragmentManager fm)
    {
        this.context = context;

        this.mainActivity = (MainActivity)context;

        this.myDb = new DatabaseHandler(context);

        this.resources = context.getResources();

        this.inflater = inflater;

        this.mData = mData;

        this.nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
        this.nowCalendar.set(Calendar.MINUTE, 0);


        this.montserratTypeface = FontCache.get("fonts/Muli/Muli-Regular.ttf", context);

        this.allTasks_fragment = (AllTasks_Fragment)fm.findFragmentByTag("AllTasksFragment");



    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.task_list_item_w_textseperator, parent, false);

        TaskItemViewHolder taskItemViewHolder = new TaskItemViewHolder(view, context);

        return taskItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        final TaskItemViewHolder itemViewHolder = (TaskItemViewHolder)holder;

        final TaskItem item = this.mData.get(position);

        CategoryListItem categoryListItem = myDb.getCategoryItem(item.getCategory());

        int taskDateDescriptionNumber;
        taskDateDescriptionNumber = DateFormatClass.setTaskItemAdapterDateDescriptionText(getDateDiff(item.getDate()));

        Calendar dateCalendar = Calendar.getInstance();
        String currentDate = DateFormatClass.setUserDateToDatabase(dateCalendar);


        itemViewHolder.taskItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                Intent viewIntent = new Intent(context, ViewTaskActivity.class);
                viewIntent.putExtra("TaskID", item.getId());

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mainActivity, itemViewHolder.taskItem, "taskItem");
                ActivityCompat.startActivity(context, viewIntent, options.toBundle());

            }
        });


        itemViewHolder.taskNameText.setText(item.getName());
        itemViewHolder.taskCategoryText.setText(item.getCategory());
        itemViewHolder.taskCategoryIcon.getDrawable().setColorFilter(categoryListItem.getListColor(), PorterDuff.Mode.SRC_IN);





        //TaskItemCard: Set up date text and description
        itemViewHolder.taskDateDescriptionText.setText(getTaskDateDescriptionByNumber(taskDateDescriptionNumber, item));
        itemViewHolder.taskDateText.setText(DateFormatClass.setTaskItemDateText(taskDateDescriptionNumber, item));
        itemViewHolder.taskDateDescriptionText.setTextColor(DateFormatClass.setTaskItemDateTextColor(taskDateDescriptionNumber));



        if(position == 0 && item.getDate().equals(currentDate) && item.getStatus() == 0)
        {
            itemViewHolder.headerSeparator.setVisibility(View.VISIBLE);
            itemViewHolder.headerSeparator.setText(context.getString(R.string.today));
        }
        else
        {
            itemViewHolder.headerSeparator.setVisibility(View.GONE);
        }


        if(position == 0 && !item.getDate().equals(currentDate))
        {
            setFirstMargin(itemViewHolder);
        }


        if(mData.size() > 1)
        {
            if(item.getStatus() == 0)
            {
                if(item.getDate().equals(currentDate))
                {
                    if(mData.get(position +1).getDate().equals(currentDate) && mData.get(position +1).getStatus() == 0)
                    {
                        itemViewHolder.bottomSeperator.setVisibility(View.GONE);
                    }
                    else
                    {
                        itemViewHolder.bottomSeperator.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    itemViewHolder.bottomSeperator.setVisibility(View.GONE);
                }

            }
            else
            {
                itemViewHolder.bottomSeperator.setVisibility(View.GONE);
            }
        }









        if(item.getStatus() == 0)
        {
            //task not done yet
            itemViewHolder.taskStatusView.setBackground(itemViewHolder.uncheckedDrawable);
            itemViewHolder.taskNameText.setPaintFlags(0);
            itemViewHolder.taskDateDescriptionText.setPaintFlags(0);
            itemViewHolder.taskDateText.setPaintFlags(0);
            itemViewHolder.taskItemDoneView.setVisibility(View.GONE);
        }
        else
        {
            //task done
            itemViewHolder.taskStatusView.setBackground(itemViewHolder.checkedDrawable);
            itemViewHolder.taskNameText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskDateDescriptionText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskDateDescriptionText.setTextColor(Color.parseColor("#d1d1d1"));
            itemViewHolder.taskDateText.setPaintFlags(itemViewHolder.taskNameText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemViewHolder.taskItemDoneView.setVisibility(View.VISIBLE);
        }






    }

    private void setExtraMargin(TaskItemViewHolder viewHolder)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        params.setMargins(setMarginInPixelFromDP(3), setMarginInPixelFromDP(0), setMarginInPixelFromDP(3), setMarginInPixelFromDP(30));

        viewHolder.taskItem.setLayoutParams(params);
    }

    private void setDefaultMargin(TaskItemViewHolder viewHolder)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        params.setMargins(setMarginInPixelFromDP(3), setMarginInPixelFromDP(0), setMarginInPixelFromDP(3), setMarginInPixelFromDP(1));

        viewHolder.taskItem.setLayoutParams(params);
    }

    private void setFirstMargin(TaskItemViewHolder viewHolder)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        params.setMargins(setMarginInPixelFromDP(3), setMarginInPixelFromDP(5), setMarginInPixelFromDP(3), setMarginInPixelFromDP(1));

        viewHolder.taskItem.setLayoutParams(params);
    }


    private String getTaskDateDescriptionByNumber(int number, TaskItem item)
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

        if(number == 2 && item.getStatus() == 0)
        {
            dateDescription = context.getResources().getString(R.string.today_date_description);
        }
        else
        {
            dateDescription = context.getResources().getString(R.string.done_date_description_today);
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






    private int setMarginInPixelFromDP(float marginInDp)
    {
        int margin = 0;

        margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                marginInDp,
                resources.getDisplayMetrics()
        );

        return margin;

    }
















}
