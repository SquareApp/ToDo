package com.squareapp.todo;



public class CategoryListItem
{


    private String listName;

    private int listColor;

    private boolean isAddItem = false;


    public static CategoryListItem createListItem(String name, int listColor)
    {

        CategoryListItem listItem = new CategoryListItem();


        listItem.listName = name;
        listItem.listColor = listColor;

        return listItem;


    }

    public static CategoryListItem createAddItem()
    {

        CategoryListItem listItem = new CategoryListItem();


        listItem.isAddItem = true;

        return listItem;


    }

    public int getListColor()
    {
        return listColor;
    }

    public String getListName()
    {
        return listName;
    }

    public boolean isAddItem()
    {
        return isAddItem;
    }


}
