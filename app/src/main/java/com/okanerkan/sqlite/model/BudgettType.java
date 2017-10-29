package com.okanerkan.sqlite.model;

import java.io.Serializable;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettType implements Serializable
{
    private int mID;
    private String mTypeCode;

    public BudgettType(int _id, String _typeValue)
    {
        this.mID = _id;
        this.mTypeCode = _typeValue;
    }

    public int getID() { return this.mID; }
    public String getTypeCode()
    {
        return this.mTypeCode;
    }
    public void setTypeCode(String _type) { this.mTypeCode = _type; }
}
