package com.okanerkan.sqlite.model;

import com.okanerkan.globals.Globals;

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
        this.mList = Globals.DBHelper.getAllBudgettType();
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

    public ArrayList<BudgettType> GetBudgettTypeList()
    {
        return this.mList;
    }

    public ArrayList<BudgettType> GetBudgettTypeList(BudgettEntryType _entryType)
    {
        ArrayList<BudgettType> list = new ArrayList<>();
        for(BudgettType type: this.mList)
        {
            if(type.getEntryType() == _entryType)
                list.add(type);
        }
        return list;
    }


    public void AddToList(BudgettType _type)
    {
        this.mList.add(_type);
    }

    public void RemoveFromList(BudgettType _type)
    {
        this.mList.remove(_type);
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
