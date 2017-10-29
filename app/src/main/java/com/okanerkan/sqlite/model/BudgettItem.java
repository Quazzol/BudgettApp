package com.okanerkan.sqlite.model;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettItem
{
    private BudgettEntryType mEntryType;
    private java.util.Date mEntryDate;
    private int mBudgettSource;
    private int mBudgettType;
    private double mPrice;

    public BudgettItem(BudgettEntryType _type,
                       java.util.Date _date,
                       int _budgettSource,
                       int _budgettType,
                       double _price)
    {
        this.mEntryType = _type;
        this.mEntryDate = _date;
        this.mBudgettSource = _budgettSource;
        this.mBudgettType = _budgettType;
        this.mPrice = _price;
    }

    public BudgettEntryType getEntryType()
    {
        return this.mEntryType;
    }

    public java.util.Date getEntryDate()
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

