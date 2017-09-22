package com.squareapp.todo;


import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopeer.itemtouchhelperextension.Extension;

/**
 * Created by Valentin Purrucker on 20.09.2017.
 */

public class TaskItemViewHolder extends RecyclerView.ViewHolder implements Extension
{

    Context context;

    float actionWidth;

    public TextView headerSeparator;
    public TextView taskNameText;
    public TextView taskCategoryText;
    public TextView taskDateDescriptionText;
    public TextView taskDateText;

    public ImageView taskCategoryIcon;
    public ImageView taskItemDoneView;

    public View taskStatusView;
    public View bottomSeperator;

    public CardView taskItem;


    public Drawable uncheckedDrawable;
    public Drawable checkedDrawable;


    private Typeface muliTypeface;

    public TaskItemViewHolder(View itemView, Context context)
    {
        super(itemView);

        this.context = context;

        this.muliTypeface = FontCache.get("fonts/Muli/Muli-Regular.ttf", context);


        headerSeparator = (TextView)itemView.findViewById(R.id.dateSeperator);
        taskNameText = (TextView)itemView.findViewById(R.id.taskNameText);
        taskCategoryText = (TextView)itemView.findViewById(R.id.taskCategoryText);
        taskDateDescriptionText = (TextView)itemView.findViewById(R.id.taskDateDescriptionText);
        taskDateText = (TextView)itemView.findViewById(R.id.taskDateText);

        taskCategoryIcon = (ImageView)itemView.findViewById(R.id.taskCategoryIcon);
        taskItemDoneView = (ImageView)itemView.findViewById(R.id.taskItemDoneView);

        taskStatusView = (View) itemView.findViewById(R.id.taskStatusView);
        bottomSeperator = (View)itemView.findViewById(R.id.bottomSeperator);

        taskItem = (CardView)itemView.findViewById(R.id.taskItemCard);

        checkedDrawable = context.getDrawable(R.drawable.taskitem_checked);
        uncheckedDrawable = context.getDrawable(R.drawable.taskitem_uncheck);



        setTypeface();
    }




    private void setTypeface()
    {
        headerSeparator.setTypeface(muliTypeface);
        taskNameText.setTypeface(muliTypeface);
        taskDateText.setTypeface(muliTypeface);
        taskCategoryText.setTypeface(muliTypeface);
        taskDateDescriptionText.setTypeface(muliTypeface);
    }


    //set return value to the size you want to be as the drawable which is drawn ontop of your recyclerview when swiped
    @Override
    public float getActionWidth()
    {
        actionWidth = itemView.getWidth() / 4.5f;
        return actionWidth;
    }




}
