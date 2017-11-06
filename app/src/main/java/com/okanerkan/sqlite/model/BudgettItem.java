package com.okanerkan.sqlite.model;

import com.okanerkan.dll.ObservableBase;
import com.okanerkan.interfaces.ISpinnerSource;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettItem extends ObservableBase implements Serializable, ISpinnerSource
{
    private int mID;
    private int mEntryType;
    private long mEntryDate;
    private int mBudgettSource;
    private int mBudgettType;
    private String mBudgettNote;
    private double mAmount;

    public BudgettItem()
    {
    }

    public BudgettItem(int _id,
                       BudgettEntryType _type,
                       long _date,
                       int _budgettSource,
                       int _budgettType,
                       String _budgettNote,
                       double _amount)
    {
        this.mID = _id;
        this.mEntryType = _type.getValue();
        this.mEntryDate = _date;
        this.mBudgettSource = _budgettSource;
        this.mBudgettType = _budgettType;
        this.mBudgettNote = _budgettNote;
        this.mAmount = _amount;
    }

    public int getID() { return this.mID; }
    public void setID(int _id)
    {
        this.SetValue("ID", _id);
    }

    public BudgettEntryType getEntryType() { return BudgettEntryType.values()[this.mEntryType]; }
    public void setEntryType(int _type)
    {
        this.SetValue("EntryType", _type);
    }

    public long getEntryDate()
    {
        return this.mEntryDate;
    }
    public void setEntryDate(String _date)
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(_date);
            this.mEntryDate = new Timestamp(date.getTime()).getTime();
        }
        catch (Exception ex)
        {
            this.mEntryDate = new Timestamp(System.currentTimeMillis()).getTime();
        }
    }
    public void setEntryDate(long _timestamp)
    {
        this.SetValue("EntryDate", _timestamp);
    }

    public int getBudgettSource()
    {
        return this.mBudgettSource;
    }
    public void setBudgettSource(int _sourceID)
    {
        this.SetValue("BudgettSource", _sourceID);
    }

    public int getBudgettType()
    {
        return this.mBudgettType;
    }
    public void setBudgettType(int _typeID)
    {
        this.SetValue("BudgettType", _typeID);
    }

    public String getBudgettNote() { return this.mBudgettNote; }
    public void setBudgettNote(String _note)
    {
        this.SetValue("BudgettNote", _note);
    }

    public double getAmount()
    {
        return this.mAmount;
    }
    public void setAmount(double _amount)
    {
        this.SetValue("Amount", _amount);
    }

    public boolean ValidateModel()
    {
        return this.mBudgettSource > 0 &&
                this.mBudgettType > 0 &&
                this.mEntryDate > 0 &&
                this.mAmount > 0;
    }

    public Object GetID()
    {
        return this.mID;
    }

    public boolean IsLoaded() { return false; }
    public boolean ExistInDB() { return this.mID >= 0; }
}

