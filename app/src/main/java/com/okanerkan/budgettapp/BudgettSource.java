package com.okanerkan.budgettapp;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettSource
{
    private int mID;
    private String mSourceValue;

    public BudgettSource(int _id, String _sourceValue)
    {
        this.mID = _id;
        this.mSourceValue = _sourceValue;
    }

    public int getID()
    {
        return this.mID;
    }

    public String getSourceValue()
    {
        return this.mSourceValue;
    }
}
