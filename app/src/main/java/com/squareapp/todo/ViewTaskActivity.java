package com.squareapp.todo;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ViewTaskActivity extends AppCompatActivity
{


    private String taskName;
    private String taskCategory;
    private String taskDescription;
    private String taskDate;
    private String taskDateDescription;
    private String taskTime;

    private int taskDateDescriptionNumber;
    private int taskStatus;



    private int taskID = 0;
    private int taskCategoryIconColor;

    private DatabaseHandler myDb;

    private TextView taskNameText;
    private TextView taskCategoryText;
    private TextView taskDateDescriptionText;
    private TextView taskDateText;
    private TextView listEditText;
    private TextView dateEditText;
    private TextView timeEditText;

    private View taskStatusView;

    private ImageView taskCategoryIcon;

    private TaskItem taskItem;


    private CardView taskItemCard;
    private CardView taskListCard;
    private CardView taskDateCard;
    private CardView taskTimeCard;

    private Calendar nowCalendar;

    private Drawable uncheckedDrawable;
    private Drawable checkedDrawable;

    private Typeface muliTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        init();
        setMuliTypeface();
        initData(this.taskID);
        bindData();



    }


    private void init()
    {

        this.myDb = new DatabaseHandler(this);

        this.muliTypeface = FontCache.get("fonts/Muli/Muli-Regular.ttf", this);

        this.nowCalendar = Calendar.getInstance();

        this.taskID = getIntent().getIntExtra("TaskID", 0);

        this.taskItem = myDb.getTask(this.taskID);

        this.taskItemCard = (CardView)findViewById(R.id.taskItemCard);
        this.taskListCard = (CardView)findViewById(R.id.taskListCard);
        this.taskDateCard = (CardView)findViewById(R.id.taskDateCard);
        this.taskTimeCard = (CardView)findViewById(R.id.taskTimeCard);




        this.taskNameText = (TextView)taskItemCard.findViewById(R.id.taskNameText);
        this.taskCategoryText = (TextView)taskItemCard.findViewById(R.id.taskCategoryText);
        this.taskDateDescriptionText = (TextView)taskItemCard.findViewById(R.id.taskDateDescriptionText);
        this.taskDateText = (TextView)taskItemCard.findViewById(R.id.taskDateText);
        this.listEditText = (TextView)taskListCard.findViewById(R.id.listEditText);
        this.dateEditText = (TextView)taskDateCard.findViewById(R.id.dateEditText);
        this.timeEditText = (TextView)taskTimeCard.findViewById(R.id.timeEditText);

        this.taskStatusView = (View)taskItemCard.findViewById(R.id.taskStatusView);

        this.taskCategoryIcon = (ImageView)taskItemCard.findViewById(R.id.taskCategoryIcon);

        this.uncheckedDrawable = ContextCompat.getDrawable(this, R.drawable.taskitem_uncheck);
        this.checkedDrawable = ContextCompat.getDrawable(this, R.drawable.taskitem_checked);

    }


    private void initData(int taskID)
    {

        this.taskName = taskItem.getName();
        this.taskCategory = taskItem.getCategory();
        this.taskDateDescriptionNumber = DateFormatClass.setTaskItemAdapterDateDescriptionText(getDateDiff(taskItem.getDate()));
        this.taskDateDescription = getTaskDateDescriptionByNumber(taskDateDescriptionNumber, taskItem);
        this.taskDate = DateFormatClass.setTaskItemDateText(taskDateDescriptionNumber, taskItem);
        this.taskTime = taskItem.getTime();
        this.taskStatus = taskItem.getStatus();

        CategoryListItem categoryListItem = myDb.getCategoryItem(this.taskCategory);
        this.taskCategoryIconColor = categoryListItem.getListColor();



        if(taskItem.getStatus() == 0)
        {
            //task not done yet
            this.taskStatusView.setBackground(this.uncheckedDrawable);
        }
        else
        {
            //task done
            this.taskStatusView.setBackground(this.checkedDrawable);
        }


    }

    private void bindData()
    {
        this.taskNameText.setText(this.taskName);
        this.taskCategoryText.setText(this.taskCategory);
        this.taskDateText.setText(this.taskDate);
        this.taskDateDescriptionText.setText(this.taskDateDescription);

        this.listEditText.setText(this.taskCategory);

        this.dateEditText.setText(DateFormatClass.getDateFromDatabase_String(taskItem.getDate()));
        this.timeEditText.setText(this.taskTime);


        this.taskDateDescriptionText.setTextColor(DateFormatClass.setTaskItemDateTextColor(this.taskDateDescriptionNumber));
        this.listEditText.setTextColor(this.taskCategoryIconColor);

        this.taskCategoryIcon.getDrawable().setColorFilter(this.taskCategoryIconColor, PorterDuff.Mode.SRC_IN);


    }

    private void setMuliTypeface()
    {
        this.taskNameText.setTypeface(this.muliTypeface);
        this.taskCategoryText.setTypeface(this.muliTypeface);
        this.taskDateDescriptionText.setTypeface(this.muliTypeface);
        this.taskDateText.setTypeface(this.muliTypeface);
    }


    private String getTaskDateDescriptionByNumber(int number, TaskItem item)
    {
        String dateDescription = null;


        if(number == 0)
        {
            dateDescription = this.getResources().getString(R.string.tomorrow_date_description);
        }

        if(number == 1)
        {
            dateDescription = this.getResources().getString(R.string.yesterday_date_description);
        }

        if(number == 2 && item.getStatus() == 0)
        {
            dateDescription = this.getResources().getString(R.string.today_date_description);
        }
        else
        {
            dateDescription = this.getResources().getString(R.string.done_date_description_today);
        }

        if(number == 3)
        {
            dateDescription = this.getResources().getString(R.string.upcoming_task);
        }


        if(number == 4)
        {
            dateDescription = this.getResources().getString(R.string.done_date_description);
        }




        return dateDescription;


    }

    public int getDateDiff(String itemDate)
    {

        int days = 0;


        try
        {
            days =  (int) TimeUnit.DAYS.
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


}
