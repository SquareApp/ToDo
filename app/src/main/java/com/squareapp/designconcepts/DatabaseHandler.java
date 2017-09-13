package com.squareapp.designconcepts;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * Created by Valentin Purrucker on 03.09.2017.
 */





public class DatabaseHandler extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasksManager";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_COLORS = "colors";

    private static final String KEY_ID = "id";                  //column 0
    private static final String KEY_NAME = "name";              //column 1
    private static final String KEY_CATEGORY = "category";      //column 2
    private static final String KEY_DESCRIPTION = "description"; //column 3
    private static final String KEY_STATUS = "status";          //column 4
    private static final String KEY_DATE_DATABASE = "date";     //column 5
    private static final String KEY_TIME = "time";              //Column 6




    public int task_ID = -1;


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

        Toast.makeText(context, "Database Created", Toast.LENGTH_SHORT).show();

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT," +
                KEY_CATEGORY + " TEXT," +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_STATUS + " INTEGER," +
                KEY_DATE_DATABASE + " TEXT," +
                KEY_TIME + " TEXT" + ")";
        db.execSQL(CREATE_TASKS_TABLE);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_COLORS);
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
        String allTasks = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + KEY_DATE_DATABASE + " DESC, " + KEY_TIME + " DESC, " + KEY_CATEGORY + " DESC";

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

        return allTasksList;
    }










    public void deleteDatabase()
    {
        context.deleteDatabase(DATABASE_NAME);
        Log.d("Database", "Deleted");

    }












}
