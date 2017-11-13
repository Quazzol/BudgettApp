package com.okanerkan.sqlite.model;

import com.okanerkan.dll.ObservableBase;
import com.okanerkan.interfaces.ISpinnerSource;

import java.io.Serializable;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettCategory extends ObservableBase implements Serializable, ISpinnerSource
{
    private int mID;
    private int mEntryType;
    private String mCategoryCode;

    public BudgettCategory()
    {
        this(-1, BudgettEntryType.EXPENSE, "");
    }

    public BudgettCategory(int _id, BudgettEntryType _entryType, String _categoryCode)
    {
        this.mID = _id;
        this.mEntryType = _entryType.getValue();
        this.mCategoryCode = _categoryCode;
    }

    public int getID() { return this.mID; }
    public void setID(int _id)
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

    public boolean IsLoaded() { return false; }
    public boolean ExistInDB() { return this.mID >= 0; }
}
