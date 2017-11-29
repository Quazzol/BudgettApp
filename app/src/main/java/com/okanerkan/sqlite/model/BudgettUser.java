package com.okanerkan.sqlite.model;

import com.okanerkan.dll.KnEntity;
import com.okanerkan.globals.Globals;
import com.okanerkan.globals.Guid;
import com.okanerkan.globals.TimeStampHelper;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;

import java.io.Serializable;

/**
 * Created by OkanErkan on 28.11.2017.
 */

public class BudgettUser extends KnEntity implements Serializable
{
    private String mID;
    private String mName;
    private String mPassword;
    private String mAccountID;
    private String mMailAddress;
    private long mCreatedDate;
    private long mSync;
    private int mStatus;

    public BudgettUser()
    {
        this(Guid.Empty(), "", "", "", "", TimeStampHelper.GetNow(), TimeStampHelper.GetNow(), 0);
    }

    public BudgettUser(String _id, String _name, String _password, String _accountID, String _mailAddress, long _createdDate, long _sync, int _status)
    {
        this.mID = _id;
        this.mName = _name;
        this.mPassword = _password;
        this.mAccountID = _accountID;
        this.mMailAddress = _mailAddress;
        this.mCreatedDate = _createdDate;
        this.mSync = _sync;
        this.mStatus = _status;
    }

    public String getID() { return this.mID; }
    public void setID(String _id) { this.SetValue("ID", _id); }

    public String getName() { return this.mName; }
    public void setName(String _name) { this.SetValue("Name", _name); }

    public String getPassword() { return this.mPassword; }
    public void setPassword(String _password) { this.SetValue("Password", _password); }

    public String getAccounID() { return this.mAccountID; }
    public void setAccountID(String _accountID) { this.SetValue("AccountID", _accountID); }

    public String getMailAddress() { return this.mMailAddress; }
    public void setMailAddress(String _mailAddress) { this.SetValue("MailAddress", _mailAddress); }

    public long getCreatedDate() { return this.mCreatedDate; }
    public void setCreatedDate(long _createdDate) { this.SetValue("CreatedDate", _createdDate); }

    public long getSync() { return this.mSync; }
    public void setSync(long _sync) { this.SetValue("Sync", _sync); }

    public int getStatus() { return this.mStatus; }
    public void setStatus(int _status) { this.SetValue("Status", _status); }

    public boolean ValidateModel()
    {
        return !this.mName.isEmpty() &&
                !this.mPassword.isEmpty() &&
                !this.mAccountID.isEmpty() &&
                !this.mMailAddress.isEmpty() &&
                this.mCreatedDate > 0;
    }

    public String toString()
    {
        return this.mName;
    }
    public Object GetID()
    {
        return this.mID;
    }

    @Override
    protected String TableName()
    {
        return BudgettDatabaseHelper.TABLE_USER;
    }

    public boolean Login()
    {
        return false;
    }
}
