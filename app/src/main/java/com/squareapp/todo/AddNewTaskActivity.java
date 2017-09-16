package com.squareapp.todo;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.blurry.Blurry;

public class AddNewTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ColorChooserDialog.ColorCallback
{


    private String taskName;
    private String taskList;
    private String taskDateString;
    private String taskTimeString;
    private String newCategoryName;

    public int colorCode = 0;

    private boolean isAddingItem = false;

    private Calendar now;
    private Calendar dateCalendar;
    private Calendar timeCalendar;


    private Toolbar myToolbar;

    private RecyclerView myRecyclerView;

    private View view;

    private TextView toolbarTitleText;
    private TextView taskTimeEt;
    private TextView taskDateEt;
    private TextView taskListEt;

    private ImageView blurImage;
    private ImageView addItemIcon;

    private Typeface muliTypeface = FontCache.get("fonts/Muli/Muli-Regular.ttf", this);

    private CardView taskNameCard;
    private CardView taskListCard;
    private CardView taskDateCard;
    private CardView taskTimeCard;

    private EditText taskNameEt;
    private EditText newCategoryEt;


    private LinearLayoutManager lm;

    private ListDialogAdapter listDialogAdapter;

    private ArrayList<CategoryListItem> mData;



    private Drawable checkIcon;
    private Drawable addIcon;

    private Dialog listDialog;

    private DatePickerDialog dpd;

    private TimePickerDialog tpd;

    private DatabaseHandler myDb;

    private FloatingActionButton addNewTaskFab;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_task);


        Log.d("AddNewTaskActivity", "OnCreate");
        init();
        initOnClick();
        initPickers();
        initDefaults();
        setTypeface();

        myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_close));

        setSupportActionBar(myToolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);





        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


    }


    private void init()
    {
        this.myToolbar = (Toolbar)findViewById(R.id.myToolbar);

        this.toolbarTitleText = (TextView)myToolbar.findViewById(R.id.toolbarTitleText);

        now = Calendar.getInstance();
        dateCalendar = Calendar.getInstance();
        timeCalendar = Calendar.getInstance();

        this.myDb = new DatabaseHandler(this);


        this.addNewTaskFab = (FloatingActionButton)findViewById(R.id.addNewTaskFab);




        this.taskNameCard = (CardView)findViewById(R.id.taskNameCard);
        this.taskListCard = (CardView)findViewById(R.id.taskListCard);
        this.taskDateCard = (CardView)findViewById(R.id.taskDateCard);
        this.taskTimeCard = (CardView)findViewById(R.id.taskTimeCard);

        this.taskNameEt = (EditText)taskNameCard.findViewById(R.id.nameEditText);

        this.taskDateEt = (TextView) taskDateCard.findViewById(R.id.dateEditText);
        this.taskTimeEt = (TextView) taskTimeCard.findViewById(R.id.timeEditText);
        this.taskListEt = (TextView)taskListCard.findViewById(R.id.listEditText);

        this.blurImage = (ImageView)findViewById(R.id.blurImage);

        this.lm = new LinearLayoutManager(this);

        mData = new ArrayList<>();
        initListItems();


        this.listDialogAdapter = new ListDialogAdapter(this, getLayoutInflater(), mData);

        view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);

        myRecyclerView = (RecyclerView)view.findViewById(R.id.myRecyclerView);



        checkIcon = ContextCompat.getDrawable(this, R.drawable.ic_done);
        addIcon = ContextCompat.getDrawable(this, R.drawable.ic_add);



    }

    private void createDialogList()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);

        myRecyclerView = (RecyclerView)view.findViewById(R.id.myRecyclerView);

        ImageView closeDialogIcon = (ImageView)view.findViewById(R.id.closeDialogIcon);
        this.addItemIcon = (ImageView)view.findViewById(R.id.addItemIcon);


        closeDialogIcon.setOnClickListener(this);
        addItemIcon.setOnClickListener(this);

        TextView myTitle = (TextView)view.findViewById(R.id.title);
        myTitle.setTypeface(muliTypeface);

        listDialogAdapter = new ListDialogAdapter(this, getLayoutInflater(), mData);

        myRecyclerView.setAdapter(listDialogAdapter);

        LinearLayoutManager lm = new LinearLayoutManager(this);

        myRecyclerView.setLayoutManager(lm);



        mBuilder.setView(view);




        listDialog = mBuilder.create();
        listDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        Drawable background = ContextCompat.getDrawable(this, R.drawable.dialog_background);
        listDialog.getWindow().setBackgroundDrawable(background);

        listDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                dialog.cancel();
                blurImage.setImageDrawable(null);
                addNewTaskFab.show();


                if(mData.size() > 0)
                {
                    if(mData.get(0).isAddItem())
                    {
                        mData.remove(0);
                        isAddingItem = false;
                    }
                }

            }
        });

        blurImage.setVisibility(View.VISIBLE);
        Blurry.with(this)
                .radius(8)
                .sampling(2)
                .async()
                .capture(findViewById(R.id.content))
                .into(blurImage);



        listDialog.show();
        addNewTaskFab.hide();

        listDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }




    private void initOnClick()
    {
        this.taskDateCard.setOnClickListener(this);
        this.taskTimeCard.setOnClickListener(this);
        this.taskListCard.setOnClickListener(this);
    }

    //fill the category list with items which are already stored in the database
    private void initListItems()
    {
        mData.addAll(myDb.getAllCategoryItems());
    }

    private void initDefaults()
    {
        this.taskDateString = DateFormatClass.setUserDateToDatabase(now);
        this.taskTimeString = DateFormatClass.setTimeToDatabase(now);
        this.taskList = "To-Do";

        this.taskDateEt.setText(getString(R.string.today));
        this.taskTimeEt.setText(getString(R.string.now));
        this.taskListEt.setText(getString(R.string.default_string));

        this.dateCalendar.setTime(now.getTime());
        this.dateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        this.dateCalendar.set(Calendar.MINUTE, 0);
        this.timeCalendar.setTime(now.getTime());
    }


    private void initPickers()
    {
        int colorAccent = Color.parseColor("#292852");

        now = Calendar.getInstance();

        //datepicker
        this.dpd = DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));


        dpd.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                blurImage.setImageDrawable(null);
                addNewTaskFab.show();
            }
        });
        dpd.setAccentColor(colorAccent);
        dpd.setCancelText(getString(R.string.cancel));



        //Timepicker
        this.tpd = TimePickerDialog.newInstance(this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);


        tpd.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                blurImage.setImageDrawable(null);
                addNewTaskFab.show();
            }
        });
        tpd.setAccentColor(colorAccent);
        tpd.setCancelText(getString(R.string.cancel));
    }

    private void setTypeface()
    {
        this.toolbarTitleText.setTypeface(muliTypeface);
        this.taskNameEt.setTypeface(muliTypeface);
        this.taskListEt.setTypeface(muliTypeface);
        this.taskDateEt.setTypeface(muliTypeface);
        this.taskTimeEt.setTypeface(muliTypeface);
    }







    private void openDatePickerDialog()
    {
        dpd.show(getFragmentManager(), "Datepickerdialog");
        Blurry.with(this)
                .radius(8)
                .sampling(2)
                .async()
                .capture(findViewById(R.id.content))
                .into(blurImage);

        addNewTaskFab.hide();
    }

    private void openTimePickerDialog()
    {
        tpd.show(getFragmentManager(), "Timepickerdialog");
        Blurry.with(this)
                .radius(8)
                .sampling(2)
                .async()
                .capture(findViewById(R.id.content))
                .into(blurImage);

        addNewTaskFab.hide();
    }


    public void openColorPicker()
    {
        new ColorChooserDialog.Builder(this, R.string.color_palette)
                .allowUserColorInputAlpha(false)
                .show();

        addNewTaskFab.hide();
    }


    private String getNewCategoryName()
    {


        this.newCategoryName = listDialogAdapter.getNewCategoryName();
        return newCategoryName;
    }


    private void setColorCode(int colorCode)
    {
        this.colorCode = colorCode;
        listDialogAdapter.changeColor(colorCode);
    }





    private void setDateString(Calendar dateCalendar)
    {
        this.taskDateString = DateFormatClass.setUserDateToDatabase(dateCalendar);
        this.taskDateEt.setText(DateFormatClass.setDateFromDatePicker(this.dateCalendar));
    }

    private void setTimeString(Calendar timeCalendar)
    {
        this.taskTimeString = DateFormatClass.setTimeToDatabase(timeCalendar);
        this.taskTimeEt.setText(DateFormatClass.setTimeToDatabase(this.timeCalendar));


    }


    public void setTaskList(String taskList, int color)
    {
        this.taskList = taskList;
        this.taskListEt.setText(taskList);
        this.taskListEt.setTextColor(color);
        this.listDialog.cancel();
    }












    //Check inputs
    private boolean isTaskNameOk()
    {
        boolean isOk = false;


        StringBuilder nullBuilder = new StringBuilder();

        for(int i = 0; i < this.taskNameEt.length(); i++)
        {
            nullBuilder = nullBuilder.append(" ");
        }


        if(this.taskNameEt.getText().length() > 0)
        {
            if(!this.taskNameEt.getText().toString().equals(nullBuilder.toString()))
            {
                isOk = true;
            }
        }
        else
        {
            isOk = false;
        }




        if(isOk == false)
        {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.content),
                    "Title cannot be blank", Snackbar.LENGTH_INDEFINITE);

            snackbar.setDuration(2000);

            snackbar.show();
        }

        return isOk;
    }


    private boolean isDateOk()
    {
        boolean isOk = false;

        now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);


        Calendar userDate = Calendar.getInstance();

        userDate.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
        userDate.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
        userDate.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));

        userDate.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        userDate.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        userDate.set(Calendar.SECOND, 0);



        if(DateFormatClass.toLongDateAndTime(userDate) < DateFormatClass.toLongDateAndTime(now))
        {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.content),
                    "Date cannot be in past", Snackbar.LENGTH_INDEFINITE);

            snackbar.setDuration(2000);

            snackbar.show();
        }
        else
        {
            isOk = true;
            Log.d("isDateOk", "isOk is true");
        }







        return isOk;
    }





    private void saveNewTask()
    {
        this.taskName = this.taskNameEt.getText().toString();



        TaskItem taskItem = TaskItem.createTask(this.taskName, this.taskList, "", 0, 0, this.taskDateString, this.taskTimeString);

        myDb.addTask(taskItem);
        int id = myDb.task_ID;

        Intent alarmIntent = new Intent(this, AlertReceiver.class);
        alarmIntent.putExtra("Task_ID", id);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                DateFormatClass.getAlarmTime(this.dateCalendar, this.timeCalendar),
                PendingIntent.getBroadcast(this, alarmIntent.getIntExtra("Task_ID", 0),
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));



    }












    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                finish();
                break;

            case R.id.saveIcon:

                if(isDateOk() && isTaskNameOk())
                {
                    saveNewTask();
                }
                else
                {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }


                break;
        }





        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.addnewtask_menu, menu);

        return true;
    }




    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        if(id == R.id.taskDateCard || id == R.id.dateEditText)
        {
            openDatePickerDialog();
        }

        if(id == R.id.taskTimeCard || id == R.id.timeEditText)
        {
            openTimePickerDialog();
        }

        if(id == R.id.taskListCard || id == R.id.listEditText)
        {
            //createDialog();
            createDialogList();
        }


        if(id == R.id.closeDialogIcon)
        {
            listDialog.cancel();
        }


        if(id == R.id.addItemIcon)
        {
            if(isAddingItem)
            {

                if(getNewCategoryName().length() > 0)
                {
                    if(this.colorCode != 0)
                    {
                        CategoryListItem item = new CategoryListItem();
                        item = CategoryListItem.createListItem(this.newCategoryName, this.colorCode);
                        this.myDb.addCategory(item);
                        mData.add(item);
                        listDialogAdapter.notifyItemInserted(this.mData.size());
                        Log.d("ListDialog", "adding new category succeeded");
                        Log.d("ListDialog", this.newCategoryName);
                    }
                    else
                    {
                        Snackbar.make(findViewById(R.id.content), "Select a color", Snackbar.LENGTH_SHORT).show();
                    }


                }
                else
                {
                    Snackbar.make(findViewById(R.id.content), "Choose a category name", Snackbar.LENGTH_SHORT).show();
                }

            }
            else
            {

                

                if(mData.get(0).isAddItem())
                {

                }
                else
                {
                    isAddingItem = true;
                    mData.add(0, CategoryListItem.createAddItem());
                    listDialogAdapter.notifyItemInserted(0);
                    this.addItemIcon.setImageDrawable(this.checkIcon);
                }
            }





        }


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {

        this.dateCalendar.set(Calendar.YEAR, year);
        this.dateCalendar.set(Calendar.MONTH, monthOfYear);
        this.dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        setDateString(dateCalendar);
        blurImage.setImageDrawable(null);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second)
    {
        this.timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.timeCalendar.set(Calendar.MINUTE, minute);
        this.timeCalendar.set(Calendar.SECOND, 0);

        setTimeString(timeCalendar);
        blurImage.setImageDrawable(null);
    }


    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor)
    {
        setColorCode(selectedColor);
        Log.d("Color", String.format("#%06X", 0xFFFFFF & selectedColor));
        addNewTaskFab.show();
    }
}
