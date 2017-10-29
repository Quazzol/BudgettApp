package com.okanerkan.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model.BudgettSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quazzol on 29.10.2017.
 */

public class BudgettDatabaseHelper extends SQLiteOpenHelper
{
    private static final String LOG = "BudgettDatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "budgett_db";

    private static final String TABLE_SOURCE = "budgett_source";
    private static final String TABLE_TYPE = "budgett_type";
    private static final String TABLE_BUDGETT_ITEM = "budgett_item";

    // Common column names
    private static final String KEY_ID = "id";

    // SOURCE Table - column nmaes
    private static final String KEY_SOURCE_CODE = "source_code";

    // TYPE Table - column names
    private static final String KEY_TYPE_CODE = "type_code";

    // BUDGETT_ITEM Table - column names
    private static final String KEY_ITEM_ENTRY_TYPE = "entry_type";
    private static final String KEY_ITEM_ENTRY_DATE = "date";
    private static final String KEY_ITEM_SOURCE_ID = "source_id";
    private static final String KEY_ITEM_TYPE_ID = "type_id";
    private static final String KEY_ITEM_PRICE = "price";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_SOURCE = "CREATE TABLE "
            + TABLE_SOURCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_SOURCE_CODE + " TEXT PRIMARY KEY)";

    // Tag table create statement
    private static final String CREATE_TABLE_TYPE = "CREATE TABLE " + TABLE_TYPE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_TYPE_CODE + " TEXT PRIMARY KEY)";

    // todo_tag table create statement
    private static final String CREATE_TABLE_BUDGETT_ITEM = "CREATE TABLE "
            + TABLE_BUDGETT_ITEM + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_ITEM_ENTRY_TYPE + " BOOLEAN," + KEY_ITEM_ENTRY_DATE + " DATETIME,"
            + KEY_ITEM_SOURCE_ID + " INTEGER," + KEY_ITEM_TYPE_ID + " INTEGER,"
            + KEY_ITEM_PRICE + " DOUBLE)";

    public BudgettDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // creating required tables
        db.execSQL(CREATE_TABLE_SOURCE);
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_BUDGETT_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETT_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOURCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        onCreate(db);
    }

    // ------------------------ "budgett_source" table methods ----------------//

    public long insertBudgettSource(BudgettSource source)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SOURCE_CODE, source.getSourceCode());

        return db.insert(TABLE_SOURCE, null, values);
    }

    public List<BudgettSource> getAllBudgettSource()
    {
        List<BudgettSource> tags = new ArrayList<BudgettSource>();
        String selectQuery = "SELECT * FROM " + TABLE_SOURCE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettSource t = new BudgettSource(cursor.getInt((cursor.getColumnIndex(KEY_ID))),
                    cursor.getString(cursor.getColumnIndex(KEY_SOURCE_CODE)));
            tags.add(t);
            cursor.moveToNext();
        }
        return tags;
    }

    public int updateBudgettSource(BudgettSource source)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SOURCE_CODE, source.getSourceCode());

        return db.update(TABLE_SOURCE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(source.getID()) });
    }

    public void deleteBudgettSource(BudgettSource source)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SOURCE, KEY_ID + " = ?",
                new String[] { String.valueOf(source.getID()) });
    }
/*
    // ------------------------ "budgett_item" table methods ----------------//

    public long insertBudgettItem(BudgettItem item, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long todo_id = db.insert(TABLE_TODO, null, values);

        // insert tag_ids
        for (long tag_id : tag_ids) {
            createTodoTag(todo_id, tag_id);
        }

        return todo_id;
    }

    public Todo getTodo(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " WHERE "
                + KEY_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Todo td = new Todo();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
        td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return td;
    }

    public List<Todo> getAllToDos() {
        List<Todo> todos = new ArrayList<Todo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    public List<Todo> getAllToDosByTag(String tag_name) {
        List<Todo> todos = new ArrayList<Todo>();

        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TODO_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    public int getToDoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateToDo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());

        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
    }

    public void deleteToDo(long tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }
    */
}
