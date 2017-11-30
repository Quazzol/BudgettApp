package com.okanerkan.sqlite.model;

import com.okanerkan.dll.KnEntity;
import com.okanerkan.enums.BudgettEntryType;
import com.okanerkan.globals.Guid;
import com.okanerkan.globals.TimeStampHelper;
import com.okanerkan.interfaces.ISpinnerSource;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;

import java.io.Serializable;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public class BudgettItem extends KnEntity implements Serializable, ISpinnerSource
{
    private String mID;
    private int mEntryType;
    private long mEntryDate;
    private String mUserID;
    private String mAccountID;
    private String mSourceID;
    private String mCategoryID;
    private String mBudgettNote;
    private double mAmount;
    private long mLastUpdatedDate;

    public BudgettItem()
    {
        this(Guid.New(),
                BudgettEntryType.EXPENSE,
                TimeStampHelper.GetNow(),
                "",
                "",
                "",
                "",
                "",
                0,
                TimeStampHelper.GetNow());
    }

    public BudgettItem(String _id,
                       BudgettEntryType _type,
                       long _date,
                       String _userID,
                       String _accountID,
                       String _sourceID,
                       String _categoryID,
                       String _budgettNote,
                       double _amount,
                       long _lastUpdatedDate)
    {
        this.mID = _id;
        this.mEntryType = _type.getValue();
        this.mEntryDate = _date;
        this.mUserID = _userID;
        this.mAccountID = _accountID;
        this.mSourceID = _sourceID;
        this.mCategoryID = _categoryID;
        this.mBudgettNote = _budgettNote;
        this.mAmount = _amount;
        this.mLastUpdatedDate = _lastUpdatedDate;
    }

    public String getID() { return this.mID; }
    public void setID(String _id)
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
    public void setEntryDate(long _timestamp)
    {
        this.SetValue("EntryDate", _timestamp);
    }

    public String getUserID() { return this.mUserID; }
    public void setUserID(String _userID) { this.SetValue("UserID", _userID); }

    public String getAccountID() { return this.mAccountID; }
    public void setAccountID(String _accountID) { this.SetValue("AccountID", _accountID); }

    public String getSourceID()
    {
        return this.mSourceID;
    }
    public void setSourceID(String _sourceID)
    {
        this.SetValue("SourceID", _sourceID);
    }

    public String getCategoryID()
    {
        return this.mCategoryID;
    }
    public void setBudgettType(String _categoryID)
    {
        this.SetValue("CategoryID", _categoryID);
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

    public long getLastUpdatedDate() { return this.mLastUpdatedDate; }
    public void setLastUpdatedDate(long _lastUpdatedDate) { this.SetValue("LastUpdatedDate", _lastUpdatedDate); }

    public boolean ValidateModel()
    {
        return !this.mUserID.isEmpty() &&
                !this.mAccountID.isEmpty() &&
                !this.mSourceID.isEmpty() &&
                !this.mCategoryID.isEmpty() &&
                this.mEntryDate > 0 &&
                this.mAmount > 0;
    }

    public Object GetID()
    {
        return this.mID;
    }
    public String toString()
    {
        return "";
    }

    @Override
    protected String TableName()
    {
        return BudgettDatabaseHelper.TABLE_BUDGETT_ITEM;
    }
}

