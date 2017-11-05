package com.okanerkan.sqlite.model;

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

public class BudgettItem implements Serializable, ISpinnerSource
{
    private int mID;
    private BudgettEntryType mEntryType;
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
        this.mEntryType = _type;
        this.mEntryDate = _date;
        this.mBudgettSource = _budgettSource;
        this.mBudgettType = _budgettType;
        this.mBudgettNote = _budgettNote;
        this.mAmount = _amount;
    }

    public int getID() { return this.mID; }
    public void setID(int _id) { this.mID = _id; }

    public BudgettEntryType getEntryType() { return this.mEntryType; }
    public void setEntryType(BudgettEntryType _type) { this.mEntryType = _type; }

    public long getEntryDate()
    {
        return this.mEntryDate;
    }
    public void setEntryDate(long _timestamp) { this.mEntryDate = _timestamp; }
    public void setEntryDate(int _year, int _month, int _day)
    {
        try
        {
            String dateStr =  String.format(Locale.getDefault(), "%02d/%02d/%04d", _day, _month, _year);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(dateStr);
            this.mEntryDate = new Timestamp(date.getTime()).getTime();
        }
        catch (Exception ex)
        {
            this.mEntryDate = new Timestamp(System.currentTimeMillis()).getTime();
        }
    }

    public int getBudgettSource()
    {
        return this.mBudgettSource;
    }
    public void setBudgettSource(int _sourceID) { this.mBudgettSource = _sourceID; }

    public int getBudgettType()
    {
        return this.mBudgettType;
    }
    public void setBudgettType(int _typeID) { this.mBudgettType = _typeID; }

    public String getBudgettNote() { return this.mBudgettNote; }
    public void setBudgettNote(String _note) { this.mBudgettNote = _note; }

    public double getAmount()
    {
        return this.mAmount;
    }
    public void setAmount(double _amount) { this.mAmount = _amount; }

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
}

