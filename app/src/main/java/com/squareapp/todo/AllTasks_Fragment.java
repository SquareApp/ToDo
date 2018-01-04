package com.squareapp.todo;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.Calendar;



public class AllTasks_Fragment extends Fragment implements View.OnClickListener
{

    private String currentDate;

    private int roundedCornerRadius;

    private ArrayList<TaskItem> mData;

    private LinearLayoutManager lm;

    private RecyclerView myRecyclerView;

    private TaskItemAdapter_Normal taskItemAdapter_normal;

    private DatabaseHandler myDb;

    public FloatingActionButton addNewTaskFab;

    private LinearLayout noNotificationsLayout;

    private Calendar nowCalendar;







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.alltasks_fragment, container, false);
        init(rootView);
        initRecyclerView();


        return rootView;
    }


    private void init(View rootView)
    {

        this.myRecyclerView = (RecyclerView)rootView.findViewById(R.id.myRecyclerView);

        this.myDb = new DatabaseHandler(getActivity());
        //this.myDb.deleteDatabase();

        this.mData = new ArrayList<>();

        this.nowCalendar = Calendar.getInstance();

        currentDate = DateFormatClass.setUserDateToDatabase(nowCalendar);

        mData.addAll(myDb.getAllTasks_TodayOrdered(currentDate));

        this.lm = new LinearLayoutManager(getActivity());

        this.noNotificationsLayout = (LinearLayout)rootView.findViewById(R.id.noNotificationsLayout);


        this.taskItemAdapter_normal = new TaskItemAdapter_Normal(getActivity(), getActivity().getLayoutInflater(), mData, getFragmentManager());

        this.myRecyclerView.setLayoutManager(lm);
        this.myRecyclerView.setAdapter(taskItemAdapter_normal);





        MyItemTouchHelperCallback myItemTpuchHelperCallback = new MyItemTouchHelperCallback(getActivity());
        ItemTouchHelperExtension itemTouchHelperExtension = new ItemTouchHelperExtension(myItemTpuchHelperCallback);
        //itemTouchHelperExtension.attachToRecyclerView(myRecyclerView);
        this.addNewTaskFab = (FloatingActionButton)rootView.findViewById(R.id.addNewTaskFab);
        this.addNewTaskFab.setOnClickListener(this);

    }


    private void initRecyclerView()
    {


        if(mData.size() > 0)
        {
            this.noNotificationsLayout.setVisibility(View.GONE);
            this.myRecyclerView.setVisibility(View.VISIBLE);
        }


        this.myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 10 && addNewTaskFab.getVisibility() == View.VISIBLE)
                {
                    addNewTaskFab.hide();
                }

                if(dy < 0 && addNewTaskFab.getVisibility() == View.GONE)
                {
                    addNewTaskFab.show();
                }
            }
        });
    }


    private BroadcastReceiver updateReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            updateList();
        }
    };






    private void updateList()
    {
        mData.clear();
        mData.addAll(myDb.getAllTasks_TodayOrdered(currentDate));
        taskItemAdapter_normal.notifyDataSetChanged();

        if(mData.size() > 0)
        {
            this.noNotificationsLayout.setVisibility(View.GONE);
            this.myRecyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            this.noNotificationsLayout.setVisibility(View.VISIBLE);
            this.myRecyclerView.setVisibility(View.GONE);
        }
    }








    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id)
        {
            case R.id.addNewTaskFab:
                Intent addNewTaskIntent = new Intent(getActivity(), AddNewTaskActivity.class);
                addNewTaskIntent.putExtra("State", "AddNewTask");
                startActivity(addNewTaskIntent);
                break;
        }
    }

    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateReceiver, new IntentFilter("REFRESH_BROADCAST"));
        updateList();
        this.addNewTaskFab.show();
        super.onResume();
    }

    @Override
    public void onPause()
    {

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateReceiver);
        super.onPause();
    }
}
