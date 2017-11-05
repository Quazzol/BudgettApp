package com.okanerkan.sqlite.model;

import com.okanerkan.globals.Globals;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Quazzol on 28.10.2017.
 */

public class BudgettSourceList
{
    private static BudgettSourceList mSingleton;
    private ArrayList<BudgettSource> mList;

    private BudgettSourceList()
    {
        this.mList = new ArrayList<BudgettSource>();
    }

    public static BudgettSourceList GetList()
    {
        if (BudgettSourceList.mSingleton == null)
        {
            BudgettSourceList.mSingleton = new BudgettSourceList();
            BudgettSourceList.mSingleton.LoadListFromDB();
        }
        return BudgettSourceList.mSingleton;
    }

    private void LoadListFromDB()
    {
        this.mList = Globals.DBHelper.getAllBudgettSource();
    }

    public void ReLoadListFromDB()
    {
        this.LoadListFromDB();
    }

    public ArrayList<String> GetBudgettSourceNames()
    {
        ArrayList<String> names = new ArrayList<>();
        for (BudgettSource source: this.mList)
        {
            names.add(source.getSourceCode());
        }
        return names;
    }

    public int GetBudgettSourceID(int _index)
    {
        if (_index < 0 || _index >= this.mList.size())
            return -1;
        return this.mList.get(_index).getID();
    }

    public BudgettSource GetBudgettSourceWithIndex(int _index)
    {
        if (_index < 0 || _index >= this.mList.size())
            return null;
        return this.mList.get(_index);
    }

    public BudgettSource GetBudgettSource(int _id)
    {
        for (BudgettSource source: this.mList)
        {
            if(source.getID() == _id)
                return source;
        }
        return null;
    }

    public BudgettSource GetBudgettSource(String _sourceCode)
    {
        for (BudgettSource source: this.mList)
        {
            if(source.getSourceCode().equals(_sourceCode))
                return source;
        }
        return null;
    }

    public ArrayList<BudgettSource> GetBudgettSourceList()
    {
        return this.mList;
    }

    public void AddToList(BudgettSource _source)
    {
        this.mList.add(_source);
    }

    public void RemoveFromList(BudgettSource _source)
    {
        this.mList.remove(_source);
    }

    public void RemoveFromList(int _id)
    {
        for (int i = 0; i < this.mList.size(); i++)
            if (this.mList.get(i).getID() == _id)
                this.mList.remove(i);
    }

    public void RemoveFromListWithIndex(int _index)
    {
        this.mList.remove(_index);
    }
}
