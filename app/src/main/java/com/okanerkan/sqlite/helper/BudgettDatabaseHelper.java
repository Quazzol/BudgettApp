package com.okanerkan.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model.BudgettSourceList;
import com.okanerkan.sqlite.model.BudgettType;
import com.okanerkan.sqlite.model.BudgettTypeList;

import java.util.ArrayList;

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
            + KEY_SOURCE_CODE + " TEXT UNIQUE)";

    // Tag table create statement
    private static final String CREATE_TABLE_TYPE = "CREATE TABLE " + TABLE_TYPE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_TYPE_CODE + " TEXT UNIQUE)";

    // todo_tag table create statement
    private static final String CREATE_TABLE_BUDGETT_ITEM = "CREATE TABLE "
            + TABLE_BUDGETT_ITEM + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_ITEM_ENTRY_TYPE + " BOOLEAN," + KEY_ITEM_ENTRY_DATE + " INTEGER,"
            + KEY_ITEM_SOURCE_ID + " INTEGER," + KEY_ITEM_TYPE_ID + " INTEGER,"
            + KEY_ITEM_PRICE + " DOUBLE)";

    public BudgettDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
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

    // ------------------------ "budgett_type" table methods ----------------//

    public long insertBudgettType(BudgettType type)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE_CODE, type.getTypeCode());

        long id = db.insert(TABLE_TYPE, null, values);
        if(id >= 0)
            BudgettTypeList.GetList().AddToList(type);
        return id;
    }

    public ArrayList<BudgettType> getAllBudgettType()
    {
        ArrayList<BudgettType> types = new ArrayList<BudgettType>();
        String selectQuery = "SELECT * FROM " + TABLE_TYPE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettType t = new BudgettType(cursor.getInt((cursor.getColumnIndex(KEY_ID))),
                    cursor.getString(cursor.getColumnIndex(KEY_TYPE_CODE)));
            types.add(t);
            cursor.moveToNext();
        }
        return types;
    }

    public int updateBudgettType(BudgettType type)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE_CODE, type.getTypeCode());

        return db.update(TABLE_TYPE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(type.getID()) });
    }

    public void deleteBudgettType(BudgettType type)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete(TABLE_TYPE, KEY_ID + " = ?", new String[] { String.valueOf(type.getID()) }) > 0)
            BudgettTypeList.GetList().RemoveFromList(type);
    }

    // ------------------------ "budgett_source" table methods ----------------//

    public long insertBudgettSource(BudgettSource source)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SOURCE_CODE, source.getSourceCode());

        long id = db.insert(TABLE_SOURCE, null, values);
        if (id >= 0)
            BudgettSourceList.GetList().AddToList(source);
        return id;
    }

    public ArrayList<BudgettSource> getAllBudgettSource()
    {
        ArrayList<BudgettSource> sources = new ArrayList<BudgettSource>();
        String selectQuery = "SELECT * FROM " + TABLE_SOURCE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettSource t = new BudgettSource(cursor.getInt((cursor.getColumnIndex(KEY_ID))),
                    cursor.getString(cursor.getColumnIndex(KEY_SOURCE_CODE)));
            sources.add(t);
            cursor.moveToNext();
        }
        return sources;
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

        if (db.delete(TABLE_SOURCE, KEY_ID + " = ?", new String[] { String.valueOf(source.getID()) }) > 0)
            BudgettSourceList.GetList().RemoveFromList(source);
    }

    // ------------------------ "budgett_item" table methods ----------------//

    public long insertBudgettItem(BudgettItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_ENTRY_TYPE, item.getEntryType().getValue());
        values.put(KEY_ITEM_SOURCE_ID, item.getBudgettSource());
        values.put(KEY_ITEM_TYPE_ID, item.getBudgettType());
        values.put(KEY_ITEM_ENTRY_DATE, item.getEntryDate());
        values.put(KEY_ITEM_PRICE, item.getPrice());

        long id = db.insert(TABLE_BUDGETT_ITEM, null, values);
        return id;
    }

    public BudgettItem getBudgettItem(long id)
    {
        String filter = " WHERE " + KEY_ID + " = " + id;
        ArrayList<BudgettItem> list = this.getAllBudgettItem(filter);
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    public ArrayList<BudgettItem> getAllBudgettItem(String filter)
    {
        ArrayList<BudgettItem> items = new ArrayList<BudgettItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_BUDGETT_ITEM;
        if (filter != null)
            selectQuery += filter;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettItem item = new BudgettItem(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    BudgettEntryType.values()[cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ENTRY_TYPE))],
                    cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ENTRY_DATE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_ITEM_SOURCE_ID)),
                    cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ENTRY_TYPE)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_ITEM_PRICE)));
            items.add(item);
            cursor.moveToNext();
        }

        return items;
    }

    public int getBudgettItemCount(String filter)
        {
        String countQuery = "SELECT  * FROM " + TABLE_BUDGETT_ITEM;
        if (filter != null)
            countQuery += filter;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateBudgettItem(BudgettItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_ENTRY_TYPE, item.getEntryType().getValue());
        values.put(KEY_ITEM_ENTRY_DATE, item.getEntryDate());
        values.put(KEY_ITEM_SOURCE_ID, item.getBudgettSource());
        values.put(KEY_ITEM_TYPE_ID, item.getBudgettType());
        values.put(KEY_ITEM_PRICE, item.getPrice());

        return db.update(TABLE_BUDGETT_ITEM, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getID()) });
    }

    public void deleteBudgettItem(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUDGETT_ITEM, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
}