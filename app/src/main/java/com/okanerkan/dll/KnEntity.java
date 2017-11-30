package com.okanerkan.dll;

import com.okanerkan.exceptions.IncorrectTypeException;
import com.okanerkan.exceptions.TableNotFoundException;
import com.okanerkan.exceptions.ValidationException;
import com.okanerkan.globals.Globals;
import com.okanerkan.globals.KNGlobal;
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
        this.mDBHelper = KNGlobal.DBHelper();
    }

    protected abstract String TableName();

    @Override
    public boolean ExistInDB()
    {
        return this.mLoaded;
    }

    @Override
    public boolean Save() throws TableNotFoundException, ValidationException, IncorrectTypeException
    {
        if (this.mLoaded)
            return this.Update();
        return this.Insert();
    }

    @Override
    public boolean Insert() throws TableNotFoundException, ValidationException, IncorrectTypeException
    {
        switch (this.TableName())
        {
            case BudgettDatabaseHelper.TABLE_ACCOUNT : return this.mDBHelper.insertBudgettAccount(this);
            case BudgettDatabaseHelper.TABLE_BUDGETT_ITEM : return this.mDBHelper.insertBudgettItem(this);
            case BudgettDatabaseHelper.TABLE_CATEGORY : return this.mDBHelper.insertBudgettCategory(this);
            case BudgettDatabaseHelper.TABLE_SOURCE : return this.mDBHelper.insertBudgettSource(this);
            case BudgettDatabaseHelper.TABLE_USER : return this.mDBHelper.insertBudgettUser(this);
        }
        throw new TableNotFoundException(this.TableName());
    }

    @Override
    public boolean Update() throws TableNotFoundException, ValidationException, IncorrectTypeException
    {
        switch (this.TableName())
        {
            case BudgettDatabaseHelper.TABLE_ACCOUNT : return this.mDBHelper.updateBudgettAccount(this);
            case BudgettDatabaseHelper.TABLE_BUDGETT_ITEM : return this.mDBHelper.updateBudgettItem(this);
            case BudgettDatabaseHelper.TABLE_CATEGORY : return this.mDBHelper.updateBudgettCategory(this);
            case BudgettDatabaseHelper.TABLE_SOURCE : return this.mDBHelper.updateBudgettSource(this);
            case BudgettDatabaseHelper.TABLE_USER : return this.mDBHelper.updateBudgettUser(this);
        }
        throw new TableNotFoundException(this.TableName());
    }

    @Override
    public boolean Delete() throws TableNotFoundException, IncorrectTypeException
    {
        switch (this.TableName())
        {
            case BudgettDatabaseHelper.TABLE_ACCOUNT : return this.mDBHelper.deleteBudgettAccount(this);
            case BudgettDatabaseHelper.TABLE_BUDGETT_ITEM : return this.mDBHelper.deleteBudgettItem(this);
            case BudgettDatabaseHelper.TABLE_CATEGORY : return this.mDBHelper.deleteBudgettCategory(this);
            case BudgettDatabaseHelper.TABLE_SOURCE : return this.mDBHelper.deleteBudgettCategory(this);
            case BudgettDatabaseHelper.TABLE_USER : return this.mDBHelper.deleteBudgettUser(this);
        }
        throw new TableNotFoundException(this.TableName());
    }

    @Override
    public IKnEntity Load() throws TableNotFoundException, IncorrectTypeException
    {
        KnEntity loadedEntity;
        switch (this.TableName())
        {
            case BudgettDatabaseHelper.TABLE_ACCOUNT : loadedEntity = this.mDBHelper.loadBudgettAccount(this); break;
            case BudgettDatabaseHelper.TABLE_BUDGETT_ITEM : loadedEntity = this.mDBHelper.loadBudgettItem(this); break;
            case BudgettDatabaseHelper.TABLE_CATEGORY : loadedEntity = this.mDBHelper.loadBudgettCategory(this); break;
            case BudgettDatabaseHelper.TABLE_SOURCE : loadedEntity = this.mDBHelper.loadBudgettSource(this); break;
            case BudgettDatabaseHelper.TABLE_USER : loadedEntity = this.mDBHelper.loadBudgettUser(this); break;
            default: throw new TableNotFoundException(this.TableName());
        }

        if (loadedEntity != null)
        {
            this.mLoaded = true;
        }
        return loadedEntity;
    }
}
