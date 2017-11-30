package com.okanerkan.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.okanerkan.dll.KnEntity;
import com.okanerkan.exceptions.IncorrectTypeException;
import com.okanerkan.exceptions.ValidationException;
import com.okanerkan.sqlite.model.BudgettAccount;
import com.okanerkan.sqlite.model.BudgettCategory;
import com.okanerkan.enums.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model.BudgettUser;
import com.okanerkan.sqlite.model_list.BudgettSourceList;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;

import java.util.ArrayList;

/**
 * Created by Quazzol on 29.10.2017.
 */

public class BudgettDatabaseHelper extends SQLiteOpenHelper
{
    //region Members
    private static final String LOG = "BudgettDatabaseHelper";
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "budgett_db";
    //endregion

    //region Table Names
    public static final String TABLE_SOURCE = "budgett_source";
    public static final String TABLE_CATEGORY = "budgett_category";
    public static final String TABLE_BUDGETT_ITEM = "budgett_item";
    public static final String TABLE_USER = "budgett_user";
    public static final String TABLE_ACCOUNT = "budgett_account";
    //endregion

    //region Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_CODE = "code";
    public static final String KEY_ENTRY_TYPE = "entry_type";
    public static final String KEY_LAST_UPDATED = "last_updated_date";
    //endregion

    //region USER table - column names
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_ACCOUNT_ID = "account_id";
    private static final String KEY_USER_MAIL = "mail_address";
    private static final String KEY_USER_CREATED_DATE = "created_date";
    private static final String KEY_USER_SYNC = "sync";
    private static final String KEY_USER_STATUS = "status";
    //endregion

    //region ACCOUNT table - column name
    private static final String KEY_ACCOUNT_NAME = "name";
    private static final String KEY_ACCOUNT_OWNER_ID = "owner_id";
    private static final String KEY_ACCOUNT_SYNC = "sync";
    //endregion

    //region BUDGETT_ITEM Table - column names
    public static final String KEY_ITEM_ENTRY_DATE = "date";
    public static final String KEY_ITEM_USER_ID = "user_id";
    public static final String KEY_ITEM_ACCOUNT_ID = "account_id";
    public static final String KEY_ITEM_SOURCE_ID = "source_id";
    public static final String KEY_ITEM_CATEGORY_ID = "category_id";
    public static final String KEY_ITEM_NOTE = "note";
    public static final String KEY_ITEM_AMOUNT = "amount";
    //endregion

    //region CREATE TABLE STATEMENTS
    //region Source table create statement
    private static final String CREATE_TABLE_SOURCE = "CREATE TABLE "
            + TABLE_SOURCE
            + "(" + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_ENTRY_TYPE + " BOOLEAN,"
            + KEY_CODE + " TEXT NOT NULL,"
            + KEY_LAST_UPDATED + " LONG,"
            + "UNIQUE (" + KEY_ENTRY_TYPE + "," + KEY_CODE + "))";
    //endregion

    //region Category table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY
            + "(" + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_ENTRY_TYPE + " BOOLEAN,"
            + KEY_CODE + " TEXT NOT NULL,"
            + KEY_LAST_UPDATED + " LONG,"
            + "UNIQUE (" + KEY_ENTRY_TYPE + "," + KEY_CODE + "))";
    //endregion

    //region User table create statement
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
    //endregion

    //region Account table create statement
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE "
            + TABLE_ACCOUNT
            + "(" + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_ACCOUNT_NAME + " TEXT,"
            + KEY_ACCOUNT_OWNER_ID + " TEXT NOT NULL,"
            + KEY_ACCOUNT_SYNC + " INTEGER)";
    //endregion

    //region BudgettItem table create statement
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
            + KEY_LAST_UPDATED + " LONG,"
            + "FOREIGN KEY(" + KEY_ITEM_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_ITEM_ACCOUNT_ID + ") REFERENCES " + TABLE_ACCOUNT + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_ITEM_SOURCE_ID + ") REFERENCES " + TABLE_SOURCE + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_ITEM_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + "))";
    //endregion
    //endregion

    public BudgettDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //region Overrides

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

    //endregion

    //region BudgettCategory
    public boolean insertBudgettCategory(KnEntity _category) throws IncorrectTypeException, ValidationException
    {
        if(!(_category instanceof BudgettCategory))
        {
            throw new IncorrectTypeException("BudgettCategory");
        }
        BudgettCategory category = (BudgettCategory) _category;
        if (!category.ValidateModel())
        {
            throw new ValidationException("BudgettCategory not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, category.getID());
        values.put(KEY_ENTRY_TYPE, category.getEntryType().getValue());
        values.put(KEY_CODE, category.getCategoryCode());
        values.put(KEY_LAST_UPDATED, category.getLastUpdateDate());

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
                    cursor.getString(cursor.getColumnIndex(KEY_CODE)),
                    cursor.getLong(cursor.getColumnIndex(KEY_LAST_UPDATED)));
            categories.add(t);
            cursor.moveToNext();
        }
        return categories;
    }

    public boolean updateBudgettCategory(KnEntity _category) throws IncorrectTypeException, ValidationException
    {
        if(!(_category instanceof BudgettCategory))
        {
            throw new IncorrectTypeException("BudgettCategory");
        }
        BudgettCategory category = (BudgettCategory) _category;
        if (!category.ValidateModel())
        {
            throw new ValidationException("BudgettCategory not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_TYPE, category.getEntryType().getValue());
        values.put(KEY_CODE, category.getCategoryCode());
        values.put(KEY_LAST_UPDATED, category.getLastUpdateDate());

        return db.update(TABLE_CATEGORY, values, KEY_ID + " = ?",
                new String[]{String.valueOf(category.getID())}) > 0;
    }

    public boolean deleteBudgettCategory(KnEntity _category) throws IncorrectTypeException
    {
        if(!(_category instanceof BudgettCategory))
        {
            throw new IncorrectTypeException("BudgettCategory");
        }
        BudgettCategory category = (BudgettCategory) _category;
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete(TABLE_CATEGORY, KEY_ID + " = ?", new String[] { category.getID() }) > 0)
        {
            BudgettCategoryList.GetList().RemoveFromList(category);
            return true;
        }
        return false;
    }

    public KnEntity loadBudgettCategory(KnEntity _category) throws IncorrectTypeException
    {
        if(!(_category instanceof BudgettCategory))
        {
            throw new IncorrectTypeException("BudgettCategory");
        }
        BudgettCategory category = (BudgettCategory) _category;
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE id = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { category.getID()});

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettCategory t = new BudgettCategory(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    BudgettEntryType.values()[cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_TYPE))],
                    cursor.getString(cursor.getColumnIndex(KEY_CODE)),
                    cursor.getLong(cursor.getColumnIndex(KEY_LAST_UPDATED)));
            return t;
        }
        return null;
    }
    //endregion

    //region BudgettSource
    public boolean insertBudgettSource(KnEntity _source) throws IncorrectTypeException, ValidationException
    {
        if (!(_source instanceof BudgettSource))
        {
            throw new IncorrectTypeException("BudgettSource");
        }
        BudgettSource source = (BudgettSource) _source;
        if (!source.ValidateModel())
        {
            throw new ValidationException("BudgettSource not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, source.getID());
        values.put(KEY_ENTRY_TYPE, source.getEntryType().getValue());
        values.put(KEY_CODE, source.getSourceCode());
        values.put(KEY_LAST_UPDATED, source.getLastUpdatedDate());

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
                    cursor.getString(cursor.getColumnIndex(KEY_CODE)),
                    cursor.getLong(cursor.getColumnIndex(KEY_LAST_UPDATED)));
            sources.add(t);
            cursor.moveToNext();
        }
        return sources;
    }

    public boolean updateBudgettSource(KnEntity _source) throws IncorrectTypeException, ValidationException
    {
        if (!(_source instanceof BudgettSource))
        {
            throw new IncorrectTypeException("BudgettSource");
        }
        BudgettSource source = (BudgettSource) _source;
        if (!source.ValidateModel())
        {
            throw new ValidationException("BudgettSource not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_TYPE, source.getEntryType().getValue());
        values.put(KEY_CODE, source.getSourceCode());
        values.put(KEY_LAST_UPDATED, source.getLastUpdatedDate());

        return db.update(TABLE_SOURCE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(source.getID())}) > 0;
    }

    public boolean deleteBudgettSource(KnEntity _source) throws IncorrectTypeException
    {
        if (!(_source instanceof BudgettSource))
        {
            throw new IncorrectTypeException("BudgettSource");
        }
        BudgettSource source = (BudgettSource) _source;
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete(TABLE_SOURCE, KEY_ID + " = ?", new String[] { String.valueOf(source.getID()) }) > 0)
        {
            BudgettSourceList.GetList().RemoveFromList(source);
            return true;
        }
        return false;
    }

    public KnEntity loadBudgettSource(KnEntity _source) throws IncorrectTypeException
    {
        if(!(_source instanceof BudgettSource))
        {
            throw new IncorrectTypeException("BudgettSource");
        }
        BudgettSource source = (BudgettSource) _source;
        String selectQuery = "SELECT * FROM " + TABLE_SOURCE + " WHERE id = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { source.getID()});

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettSource t = new BudgettSource(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    BudgettEntryType.values()[cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_TYPE))],
                    cursor.getString(cursor.getColumnIndex(KEY_CODE)),
                    cursor.getLong(cursor.getColumnIndex(KEY_LAST_UPDATED)));
            return t;
        }
        return null;
    }
    //endregion

    //region BudgettItem
    public boolean insertBudgettItem(KnEntity _item) throws IncorrectTypeException, ValidationException
    {
        if (!(_item instanceof BudgettItem))
        {
            throw new IncorrectTypeException("BudgettItem");
        }
        BudgettItem item = (BudgettItem) _item;
        if (!item.ValidateModel())
        {
            throw new ValidationException("BudgettItem not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, item.getID());
        values.put(KEY_ENTRY_TYPE, item.getEntryType().getValue());
        values.put(KEY_ITEM_ENTRY_DATE, item.getEntryDate());
        values.put(KEY_ITEM_USER_ID, item.getUserID());
        values.put(KEY_ITEM_ACCOUNT_ID, item.getAccountID());
        values.put(KEY_ITEM_SOURCE_ID, item.getSourceID());
        values.put(KEY_ITEM_CATEGORY_ID, item.getCategoryID());
        values.put(KEY_ITEM_NOTE, item.getBudgettNote());
        values.put(KEY_ITEM_AMOUNT, item.getAmount());
        values.put(KEY_LAST_UPDATED, item.getLastUpdatedDate());

        long id = db.insert(TABLE_BUDGETT_ITEM, null, values);
        return id >= 0;
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
            BudgettItem item = new BudgettItem(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    BudgettEntryType.values()[cursor.getInt(cursor.getColumnIndex(KEY_ENTRY_TYPE))],
                    cursor.getLong(cursor.getColumnIndex(KEY_ITEM_ENTRY_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_ITEM_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_ITEM_ACCOUNT_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_ITEM_SOURCE_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_ITEM_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_ITEM_NOTE)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_ITEM_AMOUNT)),
                    cursor.getLong(cursor.getColumnIndex(KEY_LAST_UPDATED)));
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

    public boolean updateBudgettItem(KnEntity _item) throws IncorrectTypeException, ValidationException
    {
        if (!(_item instanceof BudgettItem))
        {
            throw new IncorrectTypeException("BudgettItem");
        }
        BudgettItem item = (BudgettItem) _item;
        if (!item.ValidateModel())
        {
            throw new ValidationException("BudgettItem not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_TYPE, item.getEntryType().getValue());
        values.put(KEY_ITEM_ENTRY_DATE, item.getEntryDate());
        values.put(KEY_ITEM_USER_ID, item.getUserID());
        values.put(KEY_ITEM_ACCOUNT_ID, item.getAccountID());
        values.put(KEY_ITEM_SOURCE_ID, item.getSourceID());
        values.put(KEY_ITEM_CATEGORY_ID, item.getCategoryID());
        values.put(KEY_ITEM_NOTE, item.getBudgettNote());
        values.put(KEY_ITEM_AMOUNT, item.getAmount());
        values.put(KEY_LAST_UPDATED, item.getLastUpdatedDate());

        return db.update(TABLE_BUDGETT_ITEM, values, KEY_ID + " = ?",
                new String[]{String.valueOf(item.getID())}) > 0;
    }

    public boolean deleteBudgettItem(KnEntity _item) throws IncorrectTypeException
    {
        if (!(_item instanceof BudgettItem))
        {
            throw new IncorrectTypeException("BudgettItem");
        }
        BudgettItem item = (BudgettItem) _item;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_BUDGETT_ITEM, KEY_ID + " = ?", new String[] { String.valueOf(item.getID()) }) > 0;
    }

    public BudgettItem loadBudgettItem(KnEntity _item) throws IncorrectTypeException
    {
        if (!(_item instanceof BudgettItem))
        {
            throw new IncorrectTypeException("BudgettItem");
        }
        BudgettItem item = (BudgettItem) _item;
        String filter = " WHERE " + KEY_ID + " = '" + item.getID() + "'";
        ArrayList<BudgettItem> list = this.getAllBudgettItem(filter);
        if (list == null || list.size() == 0)
        {
            return null;
        }
        return list.get(0);
    }
    //endregion

    //region BudgettAccount
    public boolean insertBudgettAccount(KnEntity _account) throws IncorrectTypeException, ValidationException
    {
        if (!(_account instanceof BudgettAccount))
        {
            throw new IncorrectTypeException("BudgettAccount");
        }
        BudgettAccount account = (BudgettAccount) _account;
        if (!account.ValidateModel())
        {
            throw new ValidationException("BudgettAccount not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, account.getID());
        values.put(KEY_ACCOUNT_NAME, account.getName());
        values.put(KEY_ACCOUNT_OWNER_ID, account.getOwnerID());
        values.put(KEY_ACCOUNT_SYNC, account.getSync());

        long id = db.insert(TABLE_ACCOUNT, null, values);
        return id >= 0;
    }

    public boolean updateBudgettAccount(KnEntity _account) throws IncorrectTypeException, ValidationException
    {
        if (!(_account instanceof BudgettAccount))
        {
            throw new IncorrectTypeException("BudgettAccount");
        }
        BudgettAccount account = (BudgettAccount) _account;
        if (!account.ValidateModel())
        {
            throw new ValidationException("BudgettAccount not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_NAME, account.getName());
        values.put(KEY_ACCOUNT_OWNER_ID, account.getOwnerID());
        values.put(KEY_ACCOUNT_SYNC, account.getSync());

        return db.update(TABLE_ACCOUNT, values, KEY_ID + " = ?",
                new String[]{String.valueOf(account.getID())}) > 0;
    }

    public boolean deleteBudgettAccount(KnEntity _account) throws IncorrectTypeException
    {
        if (!(_account instanceof BudgettAccount))
        {
            throw new IncorrectTypeException("BudgettAccount");
        }
        BudgettAccount account = (BudgettAccount) _account;
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_ACCOUNT, KEY_ID + " = ?", new String[] { String.valueOf(account.getID()) }) > 0;
    }

    public KnEntity loadBudgettAccount(KnEntity _account) throws IncorrectTypeException
    {
        if(!(_account instanceof BudgettAccount))
        {
            throw new IncorrectTypeException("BudgettAccount");
        }
        BudgettAccount account = (BudgettAccount) _account;
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE id = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { account.getID()});

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettAccount t = new BudgettAccount(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_OWNER_ID)),
                    cursor.getLong(cursor.getColumnIndex(KEY_ACCOUNT_SYNC)));
            return t;
        }
        return null;
    }
    //endregion

    //region BudgettUser
    public boolean insertBudgettUser(KnEntity _user) throws IncorrectTypeException, ValidationException
    {
        if (!(_user instanceof BudgettUser))
        {
            throw new IncorrectTypeException("BudgettUser");
        }
        BudgettUser user = (BudgettUser) _user;
        if (!user.ValidateModel())
        {
            throw new ValidationException("BudgettUser not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getID());
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        values.put(KEY_USER_ACCOUNT_ID, user.getAccounID());
        values.put(KEY_USER_MAIL, user.getMailAddress());
        values.put(KEY_USER_CREATED_DATE, user.getCreatedDate());
        values.put(KEY_USER_SYNC, user.getSync());
        values.put(KEY_USER_STATUS, user.getStatus());

        long id = db.insert(TABLE_USER, null, values);
        return id >= 0;
    }

    public boolean updateBudgettUser(KnEntity _user) throws IncorrectTypeException, ValidationException
    {
        if (!(_user instanceof BudgettUser))
        {
            throw new IncorrectTypeException("BudgettUser");
        }
        BudgettUser user = (BudgettUser) _user;
        if (!user.ValidateModel())
        {
            throw new ValidationException("BudgettUser not valid!");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        values.put(KEY_USER_ACCOUNT_ID, user.getAccounID());
        values.put(KEY_USER_MAIL, user.getMailAddress());
        values.put(KEY_USER_CREATED_DATE, user.getCreatedDate());
        values.put(KEY_USER_SYNC, user.getSync());
        values.put(KEY_USER_STATUS, user.getStatus());

        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getID())}) > 0;
    }

    public boolean deleteBudgettUser(KnEntity _user) throws IncorrectTypeException
    {
        if (!(_user instanceof BudgettUser))
        {
            throw new IncorrectTypeException("BudgettUser");
        }
        BudgettUser user = (BudgettUser) _user;
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_USER, KEY_ID + " = ?", new String[] { String.valueOf(user.getID()) }) > 0;
    }

    public KnEntity loadBudgettUser(KnEntity _user) throws IncorrectTypeException
    {
        if(!(_user instanceof BudgettUser))
        {
            throw new IncorrectTypeException("BudgettUser");
        }
        BudgettUser user = (BudgettUser) _user;
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE id = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { user.getID()});

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettUser t = new BudgettUser(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_ACCOUNT_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_MAIL)),
                    cursor.getLong(cursor.getColumnIndex(KEY_USER_CREATED_DATE)),
                    cursor.getLong(cursor.getColumnIndex(KEY_USER_SYNC)),
                    cursor.getInt(cursor.getColumnIndex(KEY_USER_STATUS)));
            return t;
        }
        return null;
    }

    public KnEntity loadBudgettUser(String _filter)
    {
        String selectQuery = "SELECT * FROM " + TABLE_USER + " " + _filter;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            BudgettUser t = new BudgettUser(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_ACCOUNT_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_USER_MAIL)),
                    cursor.getLong(cursor.getColumnIndex(KEY_USER_CREATED_DATE)),
                    cursor.getLong(cursor.getColumnIndex(KEY_USER_SYNC)),
                    cursor.getInt(cursor.getColumnIndex(KEY_USER_STATUS)));
            return t;
        }
        return null;
    }
    //endregion
}
