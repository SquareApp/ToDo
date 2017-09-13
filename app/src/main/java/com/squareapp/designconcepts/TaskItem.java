package com.squareapp.designconcepts;

/**
 * Created by Valentin Purrucker on 03.09.2017.
 */

public class TaskItem
{


    private String name;
    private String category;
    private String date;
    private String time;
    private String description;

    private int status;
    private int id;







    public static TaskItem createTask(String name, String category, String description, int status, int id, String date, String time)
    {
        TaskItem task = new TaskItem();
        task.name = name;
        task.category = category;
        task.description = description;
        task.status = status;
        task.id = id;
        task.date = date;
        task.time = time;


        return task;
    }






    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return status;
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getDescription()
    {
        return description;
    }


}
