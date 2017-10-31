package com.okanerkan.sqlite.model;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettSource
{
    private int mID;
    private String mSourceCode;

    public BudgettSource(int _id, String _sourceValue)
    {
        this.mID = _id;
        this.mSourceCode = _sourceValue;
    }

    public int getID() { return this.mID; }
    public void setID(int _id) { this.mID = _id; }

    public String getSourceCode() { return this.mSourceCode; }
    public void setSourceCode(String _source) { this.mSourceCode = _source; }

    public boolean ValidateModel()
    {
        int length = this.mSourceCode.length();
        return length > 3 && length < 50;
    }
}
