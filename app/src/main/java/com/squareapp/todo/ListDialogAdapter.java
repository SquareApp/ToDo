package com.squareapp.todo;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;



public class ListDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;

    private ImageView listIconImage;

    private LayoutInflater inflater;

    private ArrayList<CategoryListItem> mData;

    private Typeface muliTypeface;

    private AddNewTaskActivity addNewTaskActivity;

    private AddListItemView addListItemView;


    public int offset = 0;


    public ListDialogAdapter(Context context, LayoutInflater inflater, ArrayList<CategoryListItem> mData)
    {
        this.context = context;

        this.inflater = inflater;

        this.muliTypeface = FontCache.get("fonts/Muli/Muli-Regular.ttf", context);

        this.mData = mData;

        addNewTaskActivity = (AddNewTaskActivity)context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(viewType == 1)
        {
            View itemView = inflater.inflate(R.layout.list_dialog_row, parent, false);
            return new CategoryListView(itemView);
        }
        else
        {
            View addItemView = inflater.inflate(R.layout.list_dialog_add_item_row, parent, false);
            return new AddListItemView(addItemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {


        if(getItemViewType(position) == 1)
        {
            CategoryListView itemHolder = (CategoryListView)holder;

            final CategoryListItem item = mData.get(position -offset);

            itemHolder.listName.setText(item.getListName());
            int color = item.getListColor();
            itemHolder.listColorView.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);


            itemHolder.content.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addNewTaskActivity.setTaskList(item.getListName(), item.getListColor());
                }
            });
        }
        else
        {
            AddListItemView h = (AddListItemView)holder;

            this.listIconImage = h.listIcon;

        }



    }

    public void changeColor(int colorCode)
    {
        this.listIconImage.setColorFilter(colorCode);
    }
    

    @Override
    public int getItemCount()
    {
        return mData.size() + offset;
    }

    @Override
    public int getItemViewType(int position)
    {

        int viewType = 0;

        CategoryListItem item = mData.get(position - offset);

        if(item.isAddItem())
        {
            viewType = 0;
        }
        else
        {
            viewType = 1;
        }




        return viewType;
    }

    public String getNewCategoryName()
    {
        String name = null;

        AddListItemView holder = addListItemView;
        name = holder.newCategoryEditText.getText().toString();

        return name;
    }

    public void resetNameEditText()
    {
        AddListItemView holder = addListItemView;
        holder.newCategoryEditText.setText(null);
    }

    class CategoryListView extends RecyclerView.ViewHolder
    {

        TextView listName;
        ImageView listColorView;

        LinearLayout content;

        public CategoryListView(View itemView)
        {
            super(itemView);

            listName = (TextView)itemView.findViewById(R.id.listName);

            listColorView = (ImageView)itemView.findViewById(R.id.listIcon);

            content = (LinearLayout)itemView.findViewById(R.id.content);


            setTypeface();
        }


        private void setTypeface()
        {
            listName.setTypeface(muliTypeface);
        }
    }


    class AddListItemView extends RecyclerView.ViewHolder implements View.OnClickListener
    {


        ImageView listIcon;
        EditText newCategoryEditText;

        public AddListItemView(View itemView)
        {
            super(itemView);

            listIcon = (ImageView)itemView.findViewById(R.id.listIcon);
            listIcon.setOnClickListener(this);

            this.newCategoryEditText = (EditText)itemView.findViewById(R.id.newCategoryEditText);

            addListItemView = this;


            setTypeface();
        }


        private void setTypeface()
        {
            this.newCategoryEditText.setTypeface(muliTypeface);
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();

            if(id == R.id.listIcon)
            {
                addNewTaskActivity.openColorPicker();
            }
        }


    }



}
