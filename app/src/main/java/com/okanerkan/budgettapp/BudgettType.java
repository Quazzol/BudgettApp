package com.okanerkan.budgettapp;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettType
{
    private int mID;
    private String mTypeValue;

    public BudgettType(int _id, String _typeValue)
    {
        this.mID = _id;
        this.mTypeValue = _typeValue;
    }

    public int getID()
    {
        return this.mID;
    }

    public String getTypeValue()
    {
        return this.mTypeValue;
    }
}
