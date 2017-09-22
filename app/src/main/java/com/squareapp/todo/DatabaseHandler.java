package com.squareapp.todo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
/**
 * Created by Valentin Purrucker on 03.09.2017.
 */





public class DatabaseHandler extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasksManager";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_CATEGORIES = "categories";

    private static final String KEY_ID = "id";                  //column 0
    private static final String KEY_NAME = "name";              //column 1
    private static final String KEY_CATEGORY = "category";      //column 2
    private static final String KEY_DESCRIPTION = "description"; //column 3
    private static final String KEY_STATUS = "status";          //column 4
    private static final String KEY_DATE_DATABASE = "date";     //column 5
    private static final String KEY_TIME = "time";              //Column 6

    private static final String CATEGORY_ID = "id";             //column 0
    private static final String CATEGORY_NAME = "name";         //column 1
    private static final String CATEGORY_COLOR = "color";       //column 2




    public int task_ID = -1;
    public static int tasks_today_amount = 0;


    private Context context;


    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;

        SQLiteDatabase db = getWritableDatabase();
        db.close();
    }




    @Override
    public void onCreate(SQLiteDatabase db)
    {



        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT," +
                KEY_CATEGORY + " TEXT," +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_STATUS + " INTEGER," +
                KEY_DATE_DATABASE + " TEXT," +
                KEY_TIME + " TEXT" + ")";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "(" + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_NAME + " TEXT," +
                CATEGORY_COLOR + " INTEGER" + ")";

        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CATEGORIES);
        onCreate(db);


    }



    public void addTask(TaskItem task)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, task.getName());
        contentValues.put(KEY_CATEGORY, task.getCategory());
        contentValues.put(KEY_DESCRIPTION, task.getDescription());
        contentValues.put(KEY_STATUS, task.getStatus());
        contentValues.put(KEY_DATE_DATABASE, task.getDate());
        contentValues.put(KEY_TIME, task.getTime());

        this.task_ID = (int) db.insert(TABLE_TASKS, null, contentValues);
        db.close();
    }

    public void addCategory(CategoryListItem item)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, item.getListName());
        contentValues.put(CATEGORY_COLOR, item.getListColor());

        db.insert(TABLE_CATEGORIES, null, contentValues);
    }



    public TaskItem getTask(int id)
    {
        SQLiteDatabase db = getReadableDatabase();


        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_ID + " = " +  "'" + id + "'";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null)
            cursor.moveToFirst();

        TaskItem task = new TaskItem();
        task = TaskItem.createTask(cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(5),
                cursor.getString(6));

        cursor.close();

        return task;

    }

    public CategoryListItem getCategoryItem(String category)
    {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + CATEGORY_NAME + "=" + "'" + category + "'";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            cursor.moveToFirst();
        }
        CategoryListItem item = new CategoryListItem();
        item = CategoryListItem.createListItem(cursor.getString(1),
                Integer.parseInt(cursor.getString(2)));
        cursor.close();

        return item;

    }





    public int getTaskCount(int status)
    {
        int taskAmn = 0;

        SQLiteDatabase db = getReadableDatabase();

        String countQuery = "SELECT " + KEY_ID + " FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS + " = " +"'" + status + "'";

        Cursor countCursor = db.rawQuery(countQuery, null);
        taskAmn = countCursor.getCount();

        return taskAmn;
    }






    public int updateTask(TaskItem task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, task.getName());
        contentValues.put(KEY_CATEGORY, task.getCategory());
        contentValues.put(KEY_DESCRIPTION, task.getDescription());
        contentValues.put(KEY_STATUS, task.getStatus());
        contentValues.put(KEY_DATE_DATABASE, task.getDate());
        contentValues.put(KEY_TIME, task.getTime());



        // updating row
        return db.update(TABLE_TASKS, contentValues, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });



    }






    public void deleteTask(int id)
    {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = KEY_ID + " =?";
        String[] whereArguments = new String[]
                {
                        String.valueOf(id)
                };

        db.delete(TABLE_TASKS, whereClause, whereArguments);
        db.close();
    }




    public void deleteTable(String table)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table);
    }




    public ArrayList<TaskItem> getAllTasks()
    {
        ArrayList<TaskItem> allTasksList = new ArrayList<>();

        //String allTasks = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_CATEGORY + " DESC";
        String allTasks = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + KEY_STATUS + " ASC, " + KEY_DATE_DATABASE + " DESC, " + KEY_TIME + " DESC, " + KEY_CATEGORY + " DESC";

        SQLiteDatabase db = getReadableDatabase();

        Cursor allTasksCursor = db.rawQuery(allTasks, null);

        if(allTasksCursor.moveToFirst())
        {
            do {

                allTasksList.add(
                        TaskItem.createTask(allTasksCursor.getString(1),
                                allTasksCursor.getString(2),
                                allTasksCursor.getString(3),
                                Integer.parseInt(allTasksCursor.getString(4)),
                                Integer.parseInt(allTasksCursor.getString(0)),
                                allTasksCursor.getString(5),
                                allTasksCursor.getString(6)));
            }
            while (allTasksCursor.moveToNext());
        }

        return allTasksList;
    }

    public ArrayList<TaskItem> getAllTasks_TodayOrdered(String currentDate)
    {
        ArrayList<TaskItem> mData = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String queryToday = "SELECT * FROM " + TABLE_TASKS
                + " WHERE " + KEY_DATE_DATABASE + "='" + currentDate + "'"
                + " AND " + KEY_STATUS + "='0'"
                + " ORDER BY " + KEY_TIME + " DESC, " + KEY_CATEGORY + " DESC";

        Cursor today = db.rawQuery(queryToday, null);


        if(today.moveToFirst())
        {
            do {

                mData.add(
                        TaskItem.createTask(today.getString(1),
                                today.getString(2),
                                today.getString(3),
                                Integer.parseInt(today.getString(4)),
                                Integer.parseInt(today.getString(0)),
                                today.getString(5),
                                today.getString(6)));
            }
            while (today.moveToNext());
        }


        String queryGeneral = "SELECT * FROM " + TABLE_TASKS
                + " WHERE " + KEY_DATE_DATABASE + "='" + currentDate + "'"
                + " AND " + KEY_STATUS + "='1'"
                + " OR " + KEY_DATE_DATABASE + "!='" + currentDate + "'"
                + " ORDER BY " + KEY_STATUS + " ASC, " + KEY_DATE_DATABASE + " DESC, " + KEY_TIME + " DESC, " + KEY_CATEGORY + " DESC";

        Cursor general = db.rawQuery(queryGeneral, null);

        if(general.moveToFirst())
        {
            do {

                mData.add(
                        TaskItem.createTask(general.getString(1),
                                general.getString(2),
                                general.getString(3),
                                Integer.parseInt(general.getString(4)),
                                Integer.parseInt(general.getString(0)),
                                general.getString(5),
                                general.getString(6)));
            }
            while (general.moveToNext());
        }



        return mData;
    }

    public ArrayList<CategoryListItem> getAllCategoryItems()
    {
        ArrayList<CategoryListItem> items = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CATEGORIES;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do
            {
                items.add(CategoryListItem.createListItem(cursor.getString(1),
                        Integer.parseInt(cursor.getString(2))));
            }
            while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return items;

    }


    public int getCategoryAmount()
    {
        SQLiteDatabase db = getReadableDatabase();

        int amount = (int)DatabaseUtils.queryNumEntries(db, TABLE_CATEGORIES);

        db.close();

        return amount;


    }


    public int getTaskAmountCountByName(String category)
    {
        int amount = 0;

        SQLiteDatabase db = getReadableDatabase();


        String query = "SELECT " + KEY_ID + " FROM " + TABLE_TASKS + " WHERE " + KEY_CATEGORY + "='" + category + "'" + " AND "  + KEY_STATUS + "='0'";
        Cursor cursor = db.rawQuery(query, null);

        amount = cursor.getCount();


        cursor.close();
        db.close();
        return amount;
    }





    public int getTaskDoneCount()
    {
        SQLiteDatabase db = getReadableDatabase();

        int amount = 0;

        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS + " = '1'", null);

        amount = cursor.getCount();

        cursor.close();

        return amount;
    }

    public int getTasks_today_amount()
    {
        return this.tasks_today_amount;
    }

    public int getTasks_today_amount_index()
    {
        return this.tasks_today_amount -1;
    }

    public static void setTaks_today_amount(int amn)
    {
        tasks_today_amount = amn;
    }




    //get all tasks of one status e.g. all tasks which aren't completed yet

    public ArrayList<TaskItem> getAllTasksByStatus(int status)
    {
        ArrayList<TaskItem> allTasksList = new ArrayList<>();

        //String allTasks = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_CATEGORY + " DESC";
        String allTasks = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS + " = " + "'" + status + "'"  + " ORDER BY " + KEY_DATE_DATABASE + " DESC, " + KEY_ID + " DESC, " + KEY_CATEGORY + " DESC";

        SQLiteDatabase db = getReadableDatabase();

        Cursor allTasksCursor = db.rawQuery(allTasks, null);

        if(allTasksCursor.moveToFirst())
        {
            do {

                allTasksList.add(
                        TaskItem.createTask(allTasksCursor.getString(1),
                                allTasksCursor.getString(2),
                                allTasksCursor.getString(3),
                                Integer.parseInt(allTasksCursor.getString(4)),
                                Integer.parseInt(allTasksCursor.getString(0)),
                                allTasksCursor.getString(5),
                                allTasksCursor.getString(6)));
            }
            while (allTasksCursor.moveToNext());
        }

        allTasksCursor.close();
        db.close();

        return allTasksList;
    }



//get all tasks by category
    public ArrayList<TaskItem> getAllTasksOfCategory(String category)
    {
        ArrayList<TaskItem> allTasksList = new ArrayList<>();

        String allTasks = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_CATEGORY + " = " + "'" + category + "'";

        SQLiteDatabase db = getReadableDatabase();

        Cursor allTasksCursor = db.rawQuery(allTasks, null);

        if(allTasksCursor.moveToFirst())
        {
            do {

                allTasksList.add(
                        TaskItem.createTask(allTasksCursor.getString(1),
                                allTasksCursor.getString(2),
                                allTasksCursor.getString(3),
                                Integer.parseInt(allTasksCursor.getString(4)),
                                Integer.parseInt(allTasksCursor.getString(0)),
                                allTasksCursor.getString(5),
                                allTasksCursor.getString(6)));
            }
            while (allTasksCursor.moveToNext());
        }

        allTasksCursor.close();
        db.close();

        return allTasksList;
    }










    public void deleteDatabase()
    {
        context.deleteDatabase(DATABASE_NAME);
        Log.d("Database", "Deleted");

    }



    public void fillDefaultCategoryList()
    {


        SQLiteDatabase db = getWritableDatabase();

        ArrayList<CategoryListItem> mList = new ArrayList<>();

        mList.add(CategoryListItem.createListItem("Home", Color.parseColor("#00b4a3")));
        mList.add(CategoryListItem.createListItem("Work", Color.parseColor("#00ab4e")));
        mList.add(CategoryListItem.createListItem("To-Do", Color.parseColor("#dc0053")));
        mList.add(CategoryListItem.createListItem("Today", Color.parseColor("#cf4800")));
        mList.add(CategoryListItem.createListItem("Travel", Color.parseColor("#00BCD4")));
        mList.add(CategoryListItem.createListItem("Movies to watch", Color.parseColor("#ffd412")));



        for(int i = 0; i < mList.size(); i++)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CATEGORY_NAME, mList.get(i).getListName());
            contentValues.put(CATEGORY_COLOR, mList.get(i).getListColor());

            db.insert(TABLE_CATEGORIES, null, contentValues);
        }


        db.close();




    }










}
