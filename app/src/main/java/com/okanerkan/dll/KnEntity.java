package com.okanerkan.dll;

import com.okanerkan.globals.Globals;
import com.okanerkan.interfaces.IKnEntity;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;

/**
 * Created by OkanErkan on 28.11.2017.
 */

public abstract class KnEntity extends ObservableBase implements IKnEntity
{
    protected BudgettDatabaseHelper mDBHelper;
    protected boolean mLoaded = false;

    public KnEntity()
    {
        this.mDBHelper = Globals.DBHelper;
    }

    protected abstract String TableName();

    @Override
    public boolean ExistInDB()
    {
        return this.mLoaded;
    }

    @Override
    public boolean Save()
    {
        return false;
    }

    @Override
    public boolean Insert()
    {
        return false;
    }

    @Override
    public boolean Update()
    {
        return false;
    }

    @Override
    public boolean Delete()
    {
        return false;
    }

    @Override
    public IKnEntity Load()
    {
        this.mLoaded = true;
        return null;
    }
}
