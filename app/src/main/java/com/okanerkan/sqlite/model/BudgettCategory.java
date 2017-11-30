package com.okanerkan.sqlite.model;

import com.okanerkan.dll.KnEntity;
import com.okanerkan.enums.BudgettEntryType;
import com.okanerkan.globals.Guid;
import com.okanerkan.globals.TimeStampHelper;
import com.okanerkan.interfaces.ISpinnerSource;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;

import java.io.Serializable;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettCategory extends KnEntity implements Serializable, ISpinnerSource
{
    private String mID;
    private int mEntryType;
    private String mCategoryCode;
    private long mLastUpdatedDate;

    public BudgettCategory()
    {
        this(Guid.New(), BudgettEntryType.EXPENSE, "", TimeStampHelper.GetNow());
    }

    public BudgettCategory(String _id, BudgettEntryType _entryType, String _categoryCode, long _lastUpdatedDate)
    {
        this.mID = _id;
        this.mEntryType = _entryType.getValue();
        this.mCategoryCode = _categoryCode;
        this.mLastUpdatedDate = _lastUpdatedDate;
    }

    public String getID() { return this.mID; }
    public void setID(String _id)
    {
        this.SetValue("ID", _id);
    }

    public BudgettEntryType getEntryType() { return BudgettEntryType.values()[this.mEntryType]; }
    public void setEntryType(int _entryType)
    {
        this.SetValue("EntryType", _entryType);
    }

    public String getCategoryCode()
    {
        return this.mCategoryCode;
    }
    public void setCategoryCode(String _category)
    {
        this.SetValue("CategoryCode", _category);
    }

    public long getLastUpdateDate() { return this.mLastUpdatedDate; }
    public void setLastUpdateDate(long _lastUpdatedDate) { this.SetValue("LastUpdatedDate", _lastUpdatedDate); }

    public boolean ValidateModel()
    {
        int length = this.mCategoryCode.length();
        return length >= 3 && length <= 50;
    }

    public String toString()
    {
        return this.mCategoryCode;
    }
    public Object GetID()
    {
        return this.mID;
    }

    @Override
    protected String TableName()
    {
        return BudgettDatabaseHelper.TABLE_CATEGORY;
    }
}
