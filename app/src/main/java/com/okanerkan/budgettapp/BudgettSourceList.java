package com.okanerkan.budgettapp;

import java.util.ArrayList;

/**
 * Created by Quazzol on 28.10.2017.
 */

public class BudgettSourceList
{
    private static BudgettSourceList mSingleton;
    private ArrayList<BudgettSource> mList;

    private void BudgettSourceList()
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

    }

    public ArrayList<String> GetBudgettSourceNames()
    {
        ArrayList<String> names = new ArrayList<>();
        for (BudgettSource source: this.mList)
        {
            names.add(source.getSourceValue());
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
            if(source.getSourceValue().equals(_sourceCode))
                return source;
        }
        return null;
    }
}
