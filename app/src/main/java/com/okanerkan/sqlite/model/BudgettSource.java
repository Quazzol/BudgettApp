package com.okanerkan.sqlite.model;

import com.okanerkan.dll.ObservableBase;
import com.okanerkan.interfaces.IObservable;
import com.okanerkan.interfaces.IObserver;
import com.okanerkan.interfaces.ISpinnerSource;

import java.io.Serializable;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettSource extends ObservableBase implements ISpinnerSource, Serializable
{
    private int mID;
    private int mEntryType;
    private String mSourceCode;

    public BudgettSource()
    {
        this(-1, BudgettEntryType.EXPENSE, "");
    }

    public BudgettSource(int _id, BudgettEntryType _entryType, String _sourceValue)
    {
        this.mID = _id;
        this.mEntryType = _entryType.getValue();
        this.mSourceCode = _sourceValue;
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

    public String getSourceCode() { return this.mSourceCode; }
    public void setSourceCode(String _source)
    {
        this.SetValue("SourceCode", _source);
    }

    public boolean ValidateModel()
    {
        int length = this.mSourceCode.length();
        return length >= 3 && length <= 50;
    }

    public String toString()
    {
        return this.mSourceCode;
    }

    public Object GetID()
    {
        return this.mID;
    }

    public boolean IsLoaded() { return false; }
    public boolean ExistInDB() { return this.mID >= 0; }
}
