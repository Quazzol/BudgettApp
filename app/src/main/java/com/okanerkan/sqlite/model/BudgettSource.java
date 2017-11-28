package com.okanerkan.sqlite.model;

import com.okanerkan.dll.KnEntity;
import com.okanerkan.dll.ObservableBase;
import com.okanerkan.globals.Guid;
import com.okanerkan.interfaces.IObservable;
import com.okanerkan.interfaces.IObserver;
import com.okanerkan.interfaces.ISpinnerSource;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;

import java.io.Serializable;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettSource extends KnEntity implements ISpinnerSource, Serializable
{
    private String mID;
    private int mEntryType;
    private String mSourceCode;

    public BudgettSource()
    {
        this(Guid.New(), BudgettEntryType.EXPENSE, "");
    }

    public BudgettSource(String _id, BudgettEntryType _entryType, String _sourceValue)
    {
        this.mID = _id;
        this.mEntryType = _entryType.getValue();
        this.mSourceCode = _sourceValue;
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

    @Override
    protected String TableName()
    {
        return BudgettDatabaseHelper.TABLE_SOURCE;
    }
}
