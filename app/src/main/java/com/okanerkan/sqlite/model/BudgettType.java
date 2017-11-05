package com.okanerkan.sqlite.model;

import com.okanerkan.interfaces.ISpinnerSource;

import java.io.Serializable;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettType implements Serializable, ISpinnerSource
{
    private int mID;
    private BudgettEntryType mEntryType;
    private String mTypeCode;

    public BudgettType(int _id, BudgettEntryType _entryType, String _typeValue)
    {
        this.mID = _id;
        this.mEntryType = _entryType;
        this.mTypeCode = _typeValue;
    }

    public int getID() { return this.mID; }
    public void setID(int _id) { this.mID = _id; }

    public BudgettEntryType getEntryType() { return this.mEntryType; }
    public void setEntryType(BudgettEntryType _entryType) { this.mEntryType = _entryType; }

    public String getTypeCode()
    {
        return this.mTypeCode;
    }
    public void setTypeCode(String _type) { this.mTypeCode = _type; }

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
}
