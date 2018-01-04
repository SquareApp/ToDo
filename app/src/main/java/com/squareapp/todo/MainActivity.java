package com.squareapp.todo;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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

    private ArrayList<CategoryListItem> listItems;

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
        setFinishedTasksText();
        setfinishedTasksAmountText();
        setlistText();
        loadDefaultFragment();

        this.getWindow().setNavigationBarColor(Color.parseColor("#1ED760"));

        getIntent().putExtra("String", "Test");




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


        this.myDb = new DatabaseHandler(this);

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



        /*
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myRecyclerView.getContext(),
                lm.getOrientation());

        myRecyclerView.addItemDecoration(dividerItemDecoration);

        */


        this.toolbarTitleText.setText(getString(R.string.alltasks_string));


    }


    private BroadcastReceiver messageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            setListsAmountText();
            setfinishedTasksAmountText();
        }
    };




    private void setCurrentDateText()
    {
        this.currentDateText.setText(DateFormatClass.getCurrentDateInString(currentDateCalendar));
    }

    private void setListsAmountText()
    {
        int listsAmn = listItems.size();

        String listAmnString = null;

        listAmnString = String.valueOf(listsAmn);

        this.listsAmountText.setText(listAmnString);
    }

    private void setFinishedTasksText()
    {
        this.finishedTasksText.setText(getString(R.string.done_date_description));
    }

    private void setlistText()
    {
        this.listsText.setText(getString(R.string.list_string));
    }

    private void setfinishedTasksAmountText()
    {
        this.finishedTasksAmountText.setText(String.valueOf(myDb.getTaskDoneCount()));
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
        if(myDb.getAllCategoryItems().size() == 0)
        {
            Toast.makeText(this, "Filled", Toast.LENGTH_SHORT).show();
            this.myDb.fillDefaultCategoryList();
        }


        //this.listItems.add(NavViewListItem.createListItem("Add new list", Color.parseColor("#d1d1d1")));
        this.listItems.addAll(myDb.getAllCategoryItems());

        //test data
        //this.listItems.add(CategoryListItem.createListItem("First", Color.parseColor("#d1d1d1")));







    }


    @Override
    protected void onResume()
    {
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("REFRESH_BROADCAST"));
        super.onResume();


    }

    @Override
    protected void onPause()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onPause();
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




