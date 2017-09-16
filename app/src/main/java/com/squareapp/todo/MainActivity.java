package com.squareapp.todo;

import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{


    private Toolbar myToolbar;

    private DrawerLayout myDrawerLayout;

    private ActionBarDrawerToggle myToggle;

    private TextView toolbarTitleText;
    private TextView currentDateText;
    private TextView finishedTasksText;
    private TextView finishedTasksAmountText;
    private TextView listsText;
    private TextView listsAmountText;

    private LinearLayoutManager lm;

    private RecyclerView myRecyclerView;

    private NavigationAdapter myAdapter;

    private ArrayList<NavViewListItem> listItems;

    private Calendar currentDateCalendar;

    private Typeface fontawesomeTypeface;
    private Typeface muliTypeface;

    private DatabaseHandler myDb;

    private AllTasks_Fragment allTasks_fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {






        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setTypeface();
        setCurrentDateText();
        setListsAmountText();
        loadDefaultFragment();
        initDatabase();




        myDrawerLayout.addDrawerListener(myToggle);

        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        myToggle.syncState();

        myToggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }


    private void init()
    {

        myToolbar = (Toolbar) findViewById(R.id.myToolbar);

        myDrawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);

        myToggle = new ActionBarDrawerToggle(this, myDrawerLayout, R.string.open, R.string.close);

        toolbarTitleText = (TextView)myToolbar.findViewById(R.id.toolbarTitleText);
        currentDateText = (TextView)findViewById(R.id.currentDateText);
        finishedTasksText = (TextView)findViewById(R.id.finishedTasksText);
        finishedTasksAmountText = (TextView)findViewById(R.id.finishedTasksAmountText);
        listsText = (TextView)findViewById(R.id.listText);
        listsAmountText = (TextView)findViewById(R.id.listsAmountText);


        this.myRecyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);

        this.lm = new LinearLayoutManager(this);

        myRecyclerView.setLayoutManager(lm);


        this.listItems = new ArrayList<>();
        fillListItems();

        this.myAdapter = new NavigationAdapter(this, getLayoutInflater(), this.listItems);

        this.myRecyclerView.setAdapter(this.myAdapter);

        this.currentDateCalendar = Calendar.getInstance();


        fontawesomeTypeface = FontCache.get("fonts/fontawesome-webfont.ttf", this);
        this.muliTypeface = FontCache.get("fonts/Muli/Muli-Regular.ttf", this);







    }


    private void initDatabase()
    {
        this.myDb = new DatabaseHandler(this);
        //this.myDb.deleteDatabase();
        if(this.myDb.getAllCategoryItems().size() <= 0)
        {
            Toast.makeText(this, "Filled", Toast.LENGTH_SHORT).show();
            this.myDb.fillDefaultCategoryList();
        }





        //this.myDb.addTask(TaskItem.createTask("Meeting", "Work", "", 1 ,0 , "20170910", "15:00"));
        //this.myDb.addTask(TaskItem.createTask("Call", "Work", "", 0 ,0 , "20170915", "12:40"));
        //this.myDb.addTask(TaskItem.createTask("Phone", "Work", "", 1 ,0 , "20170909", "12:10"));
        //this.myDb.addTask(TaskItem.createTask("Cleaning", "Work", "", 1 ,0 , "20170911", "16:00"));
        //this.myDb.addTask(TaskItem.createTask("Coding", "Home", "", 0 ,0 , "20170915", "18:00"));


    }


    private void setCurrentDateText()
    {
        this.currentDateText.setText(DateFormatClass.getCurrentDateInString(currentDateCalendar));
    }

    private void setListsAmountText()
    {
        int listsAmn = 0;

        String listAmnString = null;

        listsAmn = this.listItems.size() -1;
        if(listsAmn < 10)
        {
            listAmnString = "0" + String.valueOf(listsAmn);
        }
        else
        {
            listAmnString = String.valueOf(listsAmn);
        }

        this.listsAmountText.setText(listAmnString);
    }

    private void setTypeface()
    {
        this.toolbarTitleText.setTypeface(muliTypeface);
        this.currentDateText.setTypeface(muliTypeface);
        this.finishedTasksAmountText.setTypeface(muliTypeface);
        this.finishedTasksText.setTypeface(muliTypeface);
        this.listsText.setTypeface(muliTypeface);
        this.listsAmountText.setTypeface(muliTypeface);
    }


    private void loadDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();


        fm.beginTransaction().replace(R.id.contentFrame, new AllTasks_Fragment(), "AllTasksFragment").commit();
        this.allTasks_fragment = (AllTasks_Fragment)getFragmentManager().findFragmentByTag("AllTasksFragment");
    }


    private void fillListItems()
    {
        this.listItems.add(NavViewListItem.createListItem("Add new list", Color.parseColor("#d1d1d1")));
        this.listItems.add(NavViewListItem.createListItem("All tasks", Color.parseColor("#236EAF")));
        this.listItems.add(NavViewListItem.createListItem("Work", Color.parseColor("#F0A722")));
        this.listItems.add(NavViewListItem.createListItem("Movies to watch", Color.parseColor("#F4D356")));
        this.listItems.add(NavViewListItem.createListItem("Today", Color.parseColor("#8ADA4B")));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (myToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {


        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }



}




