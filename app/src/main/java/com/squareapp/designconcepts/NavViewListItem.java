package com.squareapp.designconcepts;

/**
 * Created by Valentin Purrucker on 31.08.2017.
 */

public class NavViewListItem
{

    private String name;

    private int colorCode;



    public static NavViewListItem createListItem(String name, int colorCode)
    {
        NavViewListItem listItem = new NavViewListItem();

        listItem.name = name;
        listItem.colorCode = colorCode;


        return listItem;

    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getcolorCode()
    {
        return colorCode;
    }

    public void setcolorCode(int colorCode)
    {
        colorCode = colorCode;
    }
}
