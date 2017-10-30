package com.okanerkan.sqlite.model;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettItem
{
    private int mID;
    private BudgettEntryType mEntryType;
    private int mEntryDate;
    private int mBudgettSource;
    private int mBudgettType;
    private double mPrice;

    public BudgettItem(int _id,
                       BudgettEntryType _type,
                       int _date,
                       int _budgettSource,
                       int _budgettType,
                       double _price)
    {
        this.mID = _id;
        this.mEntryType = _type;
        this.mEntryDate = _date;
        this.mBudgettSource = _budgettSource;
        this.mBudgettType = _budgettType;
        this.mPrice = _price;
    }

    public int getID() { return this.mID; }

    public BudgettEntryType getEntryType() { return this.mEntryType; }

    public int getEntryDate()
    {
        return this.mEntryDate;
    }

    public int getBudgettSource()
    {
        return this.mBudgettSource;
    }

    public int getBudgettType()
    {
        return this.mBudgettType;
    }

    public double getPrice()
    {
        return this.mPrice;
    }
}

