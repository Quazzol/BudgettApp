package com.okanerkan.sqlite.model;

import com.okanerkan.dll.ObservableBase;
import com.okanerkan.interfaces.ISpinnerSource;

import java.io.Serializable;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettType extends ObservableBase implements Serializable, ISpinnerSource
{
    private int mID;
    private int mEntryType;
    private String mTypeCode;

    public BudgettType()
    {
        this(-1, BudgettEntryType.EXPENSE, "");
    }

    public BudgettType(int _id, BudgettEntryType _entryType, String _typeValue)
    {
        this.mID = _id;
        this.mEntryType = _entryType.getValue();
        this.mTypeCode = _typeValue;
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

    public String getTypeCode()
    {
        return this.mTypeCode;
    }
    public void setTypeCode(String _type)
    {
        this.SetValue("TypeCode", _type);
    }

    public boolean ValidateModel()
    {
        int length = this.mTypeCode.length();
        return length > 3 && length < 50;
    }

    public String toString()
    {
        return this.mTypeCode;
    }

    public Object GetID()
    {
        return this.mID;
    }

    public boolean IsLoaded() { return false; }
    public boolean ExistInDB() { return this.mID >= 0; }
}
