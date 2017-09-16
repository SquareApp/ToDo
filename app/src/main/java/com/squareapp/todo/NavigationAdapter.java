package com.squareapp.todo;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Valentin Purrucker on 31.08.2017.
 */

public class NavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;

    private LayoutInflater inflater;

    private ArrayList<NavViewListItem> listItems;

    private Typeface fontawesomeTypeface;
    private Typeface muliTypeface;

    public NavigationAdapter(Context context, LayoutInflater inflater, ArrayList<NavViewListItem> listItems)
    {
        this.context = context;

        this.inflater = inflater;

        this.listItems = listItems;

        this.fontawesomeTypeface = FontCache.get("fonts/fontawesome-webfont.ttf", context);
        this.muliTypeface = FontCache.get("fonts/Muli/Muli-Regular.ttf", context);
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.navigationbar_row_layout, parent, false);

        return new Row(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Row h = (Row)holder;

        NavViewListItem item = this.listItems.get(position);

        int color = 0;
        color = ColorUtils.setAlphaComponent(item.getcolorCode(), 200);

        h.listAmountText.setTextColor(color);

        if(getItemViewType(position) == 1)
        {
            h.listAmountText.setText("+");

            h.listNameText.setText(item.getName());
            h.listNameText.setTextColor(item.getcolorCode());
        }
        else
        {

            h.listNameText.setText(item.getName());
            h.listNameText.setTextColor(item.getcolorCode());


            Random r = new Random();
            int taskListAmount = r.nextInt(15) + 1;

            String taskListAmountString = null;
            if(taskListAmount < 10)
            {
                taskListAmountString = "0" + String.valueOf(taskListAmount);
            }
            else
            {
                taskListAmountString = String.valueOf(taskListAmount);
            }
            h.listAmountText.setText(taskListAmountString);

        }






    }

    @Override
    public int getItemCount()
    {
        return this.listItems.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        NavViewListItem item = this.listItems.get(position);

        int viewType = 0;

        if(item.getName().equals("Add new list"))
        {
            viewType = 1;
        }
        else
        {
            viewType = 0;
        }


        return viewType;
    }

    class Row extends RecyclerView.ViewHolder
    {

        private TextView listNameText;
        private TextView listAmountText;



        public Row(View itemView)
        {
            super(itemView);

            this.listNameText = (TextView)itemView.findViewById(R.id.listNameText);
            this.listAmountText = (TextView)itemView.findViewById(R.id.listAmountText);


            setTypeface();

        }



        private void setTypeface()
        {
            this.listAmountText.setTypeface(muliTypeface);
            this.listNameText.setTypeface(muliTypeface);
        }
    }


}
