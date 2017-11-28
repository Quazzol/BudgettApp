package com.okanerkan.sqlite.model;

import com.okanerkan.dll.KnEntity;
import com.okanerkan.globals.Globals;
import com.okanerkan.globals.Guid;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;

import java.io.Serializable;

/**
 * Created by OkanErkan on 28.11.2017.
 */

public class BudgettAccount extends KnEntity implements Serializable
{
    private String mID;
    private String mName;
    private String mOwnerID;
    private long mSync;

    public BudgettAccount()
    {
        this(Guid.New(), "", "", Globals.GetNow());
    }

    public BudgettAccount(String _id, String _name, String _ownerID, long _sync)
    {
        this.mID = _id;
        this.mName = _name;
        this.mOwnerID = _ownerID;
        this.mSync = _sync;
    }

    public String getID() { return this.mID; }
    public void setID(String _id) { this.SetValue("ID", _id); }

    public String getName() { return this.mName; }
    public void setName(String _name) { this.SetValue("Name", _name); }

    public String getOwnerID() { return this.mOwnerID; }
    public void setOwnerID(String _ownerID) { this.SetValue("OwnerID", _ownerID); }

    public long getSync() { return this.mSync; }
    public void setSync(long _sync) { this.SetValue("Sync", _sync); }

    public boolean ValidateModel()
    {
        return !this.mName.isEmpty() &&
                !this.mOwnerID.isEmpty();
    }

    public Object GetID()
    {
        return this.mID;
    }
    public String toString()
    {
        return this.mName;
    }

    @Override
    protected String TableName()
    {
        return BudgettDatabaseHelper.TABLE_ACCOUNT;
    }
}
