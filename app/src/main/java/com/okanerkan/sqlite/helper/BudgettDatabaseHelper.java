package com.okanerkan.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.okanerkan.exceptions.ValidationException;
import com.okanerkan.sqlite.model.BudgettCategory;
import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model_list.BudgettSourceList;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;

import java.util.ArrayList;

/**
 * Created by Quazzol on 29.10.2017.
 */

public class BudgettDatabaseHelper extends SQLiteOpenHelper
{
    private static final String LOG = "BudgettDatabaseHelper";
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "budgett_db";

    public static final String TABLE_SOURCE = "budgett_source";
    public static final String TABLE_CATEGORY = "budgett_category";
    public static final String TABLE_BUDGETT_ITEM = "budgett_item";
    public static final String TABLE_USER = "budgett_user";
    public static final String TABLE_ACCOUNT = "budgett_account";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_CODE = "code";
    public static final String KEY_ENTRY_TYPE = "entry_type";

    // USER table - column names
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_ACCOUNT_ID = "account_id";
    private static final String KEY_USER_MAIL = "mail_address";
    private static final String KEY_USER_CREATED_DATE = "created_date";
    private static final String KEY_USER_SYNC = "sync";
    private static final String KEY_USER_STATUS = "status";

    // ACCOUNT table - column name
    private static final String KEY_ACCOUNT_NAME = "name";
    private static final String KEY_ACCOUNT_OWNER_ID = "owner_id";
    private static final String KEY_ACCOUNT_SYNC = "sync";

    // BUDGETT_ITEM Table - column names
    public static final String KEY_ITEM_ENTRY_DATE = "date";
    public static final String KEY_ITEM_USER_ID = "user_id";
    public static final String KEY_ITEM_ACCOUNT_ID = "account_id";
    public static final String KEY_ITEM_SOURCE_ID = "source_id";
    public static final String KEY_ITEM_CATEGORY_ID = "category_id";
    public static final String KEY_ITEM_NOTE = "note";
    public static final String KEY_ITEM_AMOUNT = "amount";

    // Source table create statement
    private static final String CREATE_TABLE_SOURCE = "CREATE TABLE "
            + TABLE_SOURCE
            + "(" + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_ENTRY_TYPE + " BOOLEAN,"
            + KEY_CODE + " TEXT NOT NULL,"
            + "UNIQUE (" + KEY_ENTRY_TYPE + "," + KEY_CODE + "))";

    // Category table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY
            + "(" + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_ENTRY_TYPE + " BOOLEAN,"
            + KEY_CODE + " TEXT NOT NULL,"
            + "UNIQUE (" + KEY_ENTRY_TYPE + "," + KEY_CODE + "))";

    // User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER
            + "(" + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_USER_NAME + " TEXT NOT NULL,"
            + KEY_USER_PASSWORD + " TEXT NOT NULL,"
            + KEY_USER_ACCOUNT_ID + " TEXT NOT NULL,"
            + KEY_USER_MAIL + " TEXT,"
            + KEY_USER_CREATED_DATE + " INTEGER,"
            + KEY_USER_SYNC + " INTEGER,"
            + KEY_USER_STATUS + " INTEGER DEFAULT '0')";

    // Account table create statement
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE "
            + TABLE_ACCOUNT
            + "(" + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_ACCOUNT_NAME + " TEXT,"
            + KEY_ACCOUNT_OWNER_ID + " TEXT NOT NULL,"
            + KEY_ACCOUNT_SYNC + " INTEGER)";

    // BudgettItem table create statement
    private static final String CREATE_TABLE_BUDGETT_ITEM = "CREATE TABLE "
            + TABLE_BUDGETT_ITEM
            + "(" + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_ENTRY_TYPE + " BOOLEAN,"
            + KEY_ITEM_ENTRY_DATE + " LONG,"
            + KEY_ITEM_USER_ID + " TEXT NOT NULL,"
            + KEY_ITEM_ACCOUNT_ID + " TEXT NOT NULL,"
            + KEY_ITEM_SOURCE_ID + " TEXT NOT NULL,"
            + KEY_ITEM_CATEGORY_ID + " TEXT NOT NULL,"
            + KEY_ITEM_NOTE + " VARCHAR,"
            + KEY_ITEM_AMOUNT + " DOUBLE,"
            + "FOREIGN KEY(" + KEY_ITEM_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_ITEM_ACCOUNT_ID + ") REFERENCES " + TABLE_ACCOUNT + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_ITEM_SOURCE_ID + ") REFERENCES " + TABLE_SOURCE + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_ITEM_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + "))";

    public BudgettDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_SOURCE);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BUDGETT_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.DropTables(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.DropTables(db);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        if (!db.isReadOnly())
        {
            db.execSQL("PRAGMA foreign_keys='ON';");
        }
    }

    private void DropTables(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETT_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOURCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
    }

    // ------------------------ "budgett_category" table methods ----------------//

    public boolean insertBudgettCategory(BudgettCategory _category) throws Exception
    {
        if (!_category.ValidateModel())
        {
            throw new Exception("BudgettCategory not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, _category.getID());
        values.put(KEY_ENTRY_TYPE, _category.getEntryType().getValue());
        values.put(KEY_CODE, _category.getCategoryCode());

        long id = db.insert(TABLE_CATEGORY, null, values);
        return id >= 0;
    }

    public ArrayList<BudgettCategory> getAllBudgettCategory()
    {
        ArrayList<BudgettCategory> categories = new ArrayList<BudgettCategory>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettCategory t = new BudgettCategory(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    BudgettEntryType.values()[cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_TYPE))],
                    cursor.getString(cursor.getColumnIndex(KEY_CODE)));
            categories.add(t);
            cursor.moveToNext();
        }
        return categories;
    }

    public boolean updateBudgettCategory(BudgettCategory _category) throws Exception
    {
        if (!_category.ValidateModel())
        {
            throw new ValidationException("BudgettCategory not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_TYPE, _category.getEntryType().getValue());
        values.put(KEY_CODE, _category.getCategoryCode());

        long id = db.update(TABLE_CATEGORY, values, KEY_ID + " = ?",
                new String[]{String.valueOf(_category.getID())});
        return id >= 0;
    }

    public void deleteBudgettCategory(BudgettCategory _category)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete(TABLE_CATEGORY, KEY_ID + " = ?", new String[] { String.valueOf(_category.getID()) }) > 0)
        {
            BudgettCategoryList.GetList().RemoveFromList(_category);
        }
    }

    // ------------------------ "budgett_source" table methods ----------------//

    public boolean insertBudgettSource(BudgettSource source) throws Exception
    {
        if (!source.ValidateModel())
        {
            throw new ValidationException("BudgettSource not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, source.getID());
        values.put(KEY_ENTRY_TYPE, source.getEntryType().getValue());
        values.put(KEY_CODE, source.getSourceCode());

        long id = db.insert(TABLE_SOURCE, null, values);
        return id >= 0;
    }

    public ArrayList<BudgettSource> getAllBudgettSource()
    {
        ArrayList<BudgettSource> sources = new ArrayList<BudgettSource>();
        String selectQuery = "SELECT * FROM " + TABLE_SOURCE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettSource t = new BudgettSource(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    BudgettEntryType.values()[cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_TYPE))],
                    cursor.getString(cursor.getColumnIndex(KEY_CODE)));
            sources.add(t);
            cursor.moveToNext();
        }
        return sources;
    }

    public int updateBudgettSource(BudgettSource source) throws Exception
    {
        if (!source.ValidateModel())
        {
            throw new Exception("BudgettSource not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_TYPE, source.getEntryType().getValue());
        values.put(KEY_CODE, source.getSourceCode());

        return db.update(TABLE_SOURCE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(source.getID())});
    }

    public void deleteBudgettSource(BudgettSource source)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete(TABLE_SOURCE, KEY_ID + " = ?", new String[] { String.valueOf(source.getID()) }) > 0)
        {
            BudgettSourceList.GetList().RemoveFromList(source);
        }
    }

    // ------------------------ "budgett_item" table methods ----------------//

    public long insertBudgettItem(BudgettItem item) throws Exception
    {
        if (!item.ValidateModel())
        {
            throw new Exception("BudgettItem not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_TYPE, item.getEntryType().getValue());
        values.put(KEY_ITEM_ENTRY_DATE, item.getEntryDate());
        values.put(KEY_ITEM_SOURCE_ID, item.getBudgettSource());
        values.put(KEY_ITEM_CATEGORY_ID, item.getBudgettType());
        values.put(KEY_ITEM_NOTE, item.getBudgettNote());
        values.put(KEY_ITEM_AMOUNT, item.getAmount());

        long id = db.insert(TABLE_BUDGETT_ITEM, null, values);
        if (id >= 0)
        {
            item.setID((int) id);
        }
        return id;
    }

    public BudgettItem getBudgettItem(long id)
    {
        String filter = " WHERE " + KEY_ID + " = " + id;
        ArrayList<BudgettItem> list = this.getAllBudgettItem(filter);
        if (list == null || list.size() == 0)
        {
            return null;
        }
        return list.get(0);
    }

    public ArrayList<BudgettItem> getAllBudgettItem(String filter)
    {
        ArrayList<BudgettItem> items = new ArrayList<BudgettItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_BUDGETT_ITEM;
        if (filter != null)
        {
            selectQuery += " " + filter;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettItem item = new BudgettItem(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    BudgettEntryType.values()[cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_TYPE))],
                    cursor.getLong(cursor.getColumnIndex(KEY_ITEM_ENTRY_DATE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_ITEM_SOURCE_ID)),
                    cursor.getInt(cursor.getColumnIndex(KEY_ITEM_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_ITEM_NOTE)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_ITEM_AMOUNT)));
            items.add(item);
            cursor.moveToNext();
        }

        return items;
    }

    public int getBudgettItemCount(String filter)
    {
        String countQuery = "SELECT  * FROM " + TABLE_BUDGETT_ITEM;
        if (filter != null)
        {
            countQuery += filter;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateBudgettItem(BudgettItem item) throws Exception
    {
        if (!item.ValidateModel())
        {
            throw new Exception("BudgettItem not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_TYPE, item.getEntryType().getValue());
        values.put(KEY_ITEM_ENTRY_DATE, item.getEntryDate());
        values.put(KEY_ITEM_SOURCE_ID, item.getBudgettSource());
        values.put(KEY_ITEM_CATEGORY_ID, item.getBudgettType());
        values.put(KEY_ITEM_NOTE, item.getBudgettNote());
        values.put(KEY_ITEM_AMOUNT, item.getAmount());

        return db.update(TABLE_BUDGETT_ITEM, values, KEY_ID + " = ?",
                new String[]{String.valueOf(item.getID())});
    }

    public void deleteBudgettItem(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUDGETT_ITEM, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
}
