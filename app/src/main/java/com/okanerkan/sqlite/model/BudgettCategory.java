package com.okanerkan.sqlite.model;

import com.okanerkan.dll.KnEntity;
import com.okanerkan.dll.ObservableBase;
import com.okanerkan.globals.Guid;
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

    public BudgettCategory()
    {
        this(Guid.New(), BudgettEntryType.EXPENSE, "");
    }

    public BudgettCategory(String _id, BudgettEntryType _entryType, String _categoryCode)
    {
        this.mID = _id;
        this.mEntryType = _entryType.getValue();
        this.mCategoryCode = _categoryCode;
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
