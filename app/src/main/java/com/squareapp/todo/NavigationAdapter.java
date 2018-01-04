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



public class NavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;

    private LayoutInflater inflater;

    private ArrayList<CategoryListItem> listItems;

    private Typeface fontawesomeTypeface;
    private Typeface muliTypeface;

    private MainActivity mainActivity;

    private DatabaseHandler myDb;

    public NavigationAdapter(Context context, LayoutInflater inflater, ArrayList<CategoryListItem> listItems)
    {
        this.context = context;

        this.myDb = new DatabaseHandler(context);

        mainActivity = (MainActivity)context;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        Row h = (Row)holder;

        CategoryListItem item = this.listItems.get(position);

        int color = 0;
        color = ColorUtils.setAlphaComponent(item.getListColor(), 200);

        h.listAmountText.setTextColor(color);

        if(getItemViewType(position) == 1)
        {
            h.listAmountText.setText("+");

            h.listNameText.setText(item.getListName());
            h.listNameText.setTextColor(item.getListColor());
        }
        else
        {

            h.listNameText.setText(item.getListName());
            h.listNameText.setTextColor(item.getListColor());


            h.listAmountText.setText(String.valueOf(myDb.getTaskAmountCountByName(item.getListName())));



        }


        if(position == listItems.size() -1)
        {
            h.dividerView.setVisibility(View.GONE);
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
        CategoryListItem item = this.listItems.get(position);

        int viewType = 0;

        if(item.getListName().equals("Add new list"))
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

        private View dividerView;



        public Row(View itemView)
        {
            super(itemView);

            this.listNameText = (TextView)itemView.findViewById(R.id.listNameText);
            this.listAmountText = (TextView)itemView.findViewById(R.id.listAmountText);

            this.dividerView = (View)itemView.findViewById(R.id.dividerView);


            setTypeface();

        }



        private void setTypeface()
        {
            this.listAmountText.setTypeface(muliTypeface);
            this.listNameText.setTypeface(muliTypeface);
        }
    }


}
