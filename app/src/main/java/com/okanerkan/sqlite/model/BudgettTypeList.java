package com.okanerkan.sqlite.model;

import java.util.ArrayList;

/**
 * Created by Quazzol on 29.10.2017.
 */

public class BudgettTypeList
{
    private static BudgettTypeList mSingleton;
    private ArrayList<BudgettType> mList;

    private BudgettTypeList()
    {
        this.mList = new ArrayList<BudgettType>();
    }

    public static BudgettTypeList GetList()
    {
        if (BudgettTypeList.mSingleton == null)
        {
            BudgettTypeList.mSingleton = new BudgettTypeList();
            BudgettTypeList.mSingleton.LoadListFromDB();
        }
        return BudgettTypeList.mSingleton;
    }

    private void LoadListFromDB()
    {
        // TODO get list from database
    }

    public ArrayList<String> GetBudgettTypeNames()
    {
        ArrayList<String> names = new ArrayList<>();
        for (BudgettType type: this.mList)
        {
            names.add(type.getTypeCode());
        }
        return names;
    }

    public int GetBudgettListID(int _index)
    {
        if (_index < 0 || _index >= this.mList.size())
            return -1;
        return this.mList.get(_index).getID();
    }

    public BudgettType GetBudgettTypeWithIndex(int _index)
    {
        if (_index < 0 || _index >= this.mList.size())
            return null;
        return this.mList.get(_index);
    }

    public BudgettType GetBudgettType(int _id)
    {
        for (BudgettType source: this.mList)
        {
            if(source.getID() == _id)
                return source;
        }
        return null;
    }

    public BudgettType GetBudgettType(String _typeCode)
    {
        for (BudgettType type: this.mList)
        {
            if(type.getTypeCode().equals(_typeCode))
                return type;
        }
        return null;
    }
}
