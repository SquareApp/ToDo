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






public class DatabaseHandler extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasksManager";
    private static final String TABLE_TASKS = "Tasks";
    private static final String TABLE_CATEGORIES = "Categories";

    //Table for the tasks
    private static final String TASK_ID = "id";                  //column 0
    private static final String TASK_NAME = "name";              //column 1
    private static final String TASK_CATEGORY = "category";      //column 2
    private static final String TASK_STATUS = "status";          //column 3
    private static final String TASK_DATE = "date";     //column 4
    private static final String TASK_TIME = "time";              //Column 5

    //table for the categories
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



        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, NOT NULL," +
                TASK_NAME + " TEXT, NOT NULL, " +
                TASK_CATEGORY + " TEXT," +
                TASK_STATUS + " INTEGER," +
                TASK_DATE + " TEXT, NOT NULL, " +
                TASK_TIME + " TEXT, NOT NULL" + ")";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "(" +
                CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, NOT NULL, " +
                CATEGORY_NAME + " TEXT, NOT NULL" +
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
        contentValues.put(TASK_NAME, task.getName());
        contentValues.put(TASK_CATEGORY, task.getCategory());
        contentValues.put(TASK_STATUS, task.getStatus());
        contentValues.put(TASK_DATE, task.getDate());
        contentValues.put(TASK_TIME, task.getTime());

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


        String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASK_ID + " = " +  "'" + id + "'";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null)
            cursor.moveToFirst();

        TaskItem task = new TaskItem();
        task = TaskItem.createTask(cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(4),
                cursor.getString(5));

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

    public int updateTask(TaskItem task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_NAME, task.getName());
        contentValues.put(TASK_CATEGORY, task.getCategory());
        contentValues.put(TASK_STATUS, task.getStatus());
        contentValues.put(TASK_DATE, task.getDate());
        contentValues.put(TASK_TIME, task.getTime());



        // updating row
        return db.update(TABLE_TASKS, contentValues, TASK_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });



    }

    public void deleteTask(int id)
    {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = TASK_ID + " =?";
        String[] whereArguments = new String[]
                {
                        String.valueOf(id)
                };

        db.delete(TABLE_TASKS, whereClause, whereArguments);
        db.close();
    }


    public ArrayList<TaskItem> getAllTasks_TodayOrdered(String currentDate)
    {
        ArrayList<TaskItem> mData = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String queryToday = "SELECT * FROM " + TABLE_TASKS
                + " WHERE " + TASK_DATE + "='" + currentDate + "'"
                + " AND " + TASK_STATUS + "='0'"
                + " ORDER BY " + TASK_TIME + " DESC, " + TASK_CATEGORY + " DESC";

        Cursor today = db.rawQuery(queryToday, null);


        if(today.moveToFirst())
        {
            do {

                mData.add(
                        TaskItem.createTask(today.getString(1),
                                today.getString(2),
                                Integer.parseInt(today.getString(3)),
                                Integer.parseInt(today.getString(0)),
                                today.getString(4),
                                today.getString(5)));
            }
            while (today.moveToNext());
        }


        String queryGeneral = "SELECT * FROM " + TABLE_TASKS
                + " WHERE " + TASK_DATE + "='" + currentDate + "'"
                + " AND " + TASK_STATUS + "='1'"
                + " OR " + TASK_DATE + "!='" + currentDate + "'"
                + " ORDER BY " + TASK_STATUS + " ASC, " + TASK_DATE + " DESC, " + TASK_TIME + " DESC, " + TASK_CATEGORY + " DESC";

        Cursor general = db.rawQuery(queryGeneral, null);

        if(general.moveToFirst())
        {
            do {

                mData.add(
                        TaskItem.createTask(general.getString(1),
                                general.getString(2),
                                Integer.parseInt(general.getString(3)),
                                Integer.parseInt(general.getString(0)),
                                general.getString(4),
                                general.getString(5)));
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




    public int getTaskAmountCountByName(String category)
    {
        int amount = 0;

        SQLiteDatabase db = getReadableDatabase();


        String query = "SELECT " + TASK_ID + " FROM " + TABLE_TASKS + " WHERE " + TASK_CATEGORY + "='" + category + "'" + " AND "  + TASK_STATUS + "='0'";
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

        Cursor cursor = db.rawQuery("SELECT " + TASK_ID + " FROM " + TABLE_TASKS + " WHERE " + TASK_STATUS + " = '1'", null);

        amount = cursor.getCount();

        cursor.close();

        return amount;
    }

    //get all tasks of one status e.g. all tasks which aren't completed yet

    public ArrayList<TaskItem> getAllTasksByStatus(int status)
    {
        ArrayList<TaskItem> allTasksList = new ArrayList<>();

        //String allTasks = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_CATEGORY + " DESC";
        String allTasks = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASK_STATUS + " = " + "'" + status + "'"  + " ORDER BY " + TASK_DATE + " DESC, " + TASK_ID + " DESC, " + TASK_CATEGORY + " DESC";

        SQLiteDatabase db = getReadableDatabase();

        Cursor allTasksCursor = db.rawQuery(allTasks, null);

        if(allTasksCursor.moveToFirst())
        {
            do {

                allTasksList.add(
                        TaskItem.createTask(allTasksCursor.getString(1),
                                allTasksCursor.getString(2),
                                Integer.parseInt(allTasksCursor.getString(3)),
                                Integer.parseInt(allTasksCursor.getString(0)),
                                allTasksCursor.getString(4),
                                allTasksCursor.getString(5)));
            }
            while (allTasksCursor.moveToNext());
        }

        allTasksCursor.close();
        db.close();

        return allTasksList;
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

    public int getCategoryAmount()
    {
        SQLiteDatabase db = getReadableDatabase();

        int amount = (int)DatabaseUtils.queryNumEntries(db, TABLE_CATEGORIES);

        db.close();

        return amount;


    }

    public ArrayList<TaskItem> getAllTasks()
    {
        ArrayList<TaskItem> allTasksList = new ArrayList<>();

        //String allTasks = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_CATEGORY + " DESC";
        String allTasks = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + TASK_STATUS + " ASC, " + TASK_DATE + " DESC, " + TASK_TIME + " DESC, " + TASK_CATEGORY + " DESC";

        SQLiteDatabase db = getReadableDatabase();

        Cursor allTasksCursor = db.rawQuery(allTasks, null);

        if(allTasksCursor.moveToFirst())
        {
            do {

                allTasksList.add(
                        TaskItem.createTask(allTasksCursor.getString(1),
                                allTasksCursor.getString(2),
                                Integer.parseInt(allTasksCursor.getString(3)),
                                Integer.parseInt(allTasksCursor.getString(0)),
                                allTasksCursor.getString(4),
                                allTasksCursor.getString(5)));
            }
            while (allTasksCursor.moveToNext());
        }

        return allTasksList;
    }

    public void deleteTable(String table)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table);
    }

//get all tasks by category
    public ArrayList<TaskItem> getAllTasksOfCategory(String category)
    {
        ArrayList<TaskItem> allTasksList = new ArrayList<>();

        String allTasks = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASK_CATEGORY + " = " + "'" + category + "'";

        SQLiteDatabase db = getReadableDatabase();

        Cursor allTasksCursor = db.rawQuery(allTasks, null);

        if(allTasksCursor.moveToFirst())
        {
            do {

                allTasksList.add(
                        TaskItem.createTask(allTasksCursor.getString(1),
                                allTasksCursor.getString(2),
                                Integer.parseInt(allTasksCursor.getString(3)),
                                Integer.parseInt(allTasksCursor.getString(0)),
                                allTasksCursor.getString(4),
                                allTasksCursor.getString(5)));
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

        mList.add(CategoryListItem.createListItem(context.getString(R.string.default_category_home), Color.parseColor("#00b4a3")));
        mList.add(CategoryListItem.createListItem(context.getString(R.string.default_category_work), Color.parseColor("#00ab4e")));
        mList.add(CategoryListItem.createListItem(context.getString(R.string.default_category_todo), Color.parseColor("#dc0053")));
        mList.add(CategoryListItem.createListItem(context.getString(R.string.default_category_today), Color.parseColor("#cf4800")));
        mList.add(CategoryListItem.createListItem(context.getString(R.string.default_category_travel), Color.parseColor("#00BCD4")));
        mList.add(CategoryListItem.createListItem(context.getString(R.string.default_category_movies), Color.parseColor("#ffd412")));



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
