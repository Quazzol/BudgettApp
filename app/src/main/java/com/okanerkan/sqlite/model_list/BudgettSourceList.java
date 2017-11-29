package com.okanerkan.sqlite.model_list;

import android.support.annotation.NonNull;

import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    public ArrayList<String> GetBudgettSourceNames(BudgettEntryType _entryType)
    {
        ArrayList<String> names = new ArrayList<>();
        for (BudgettSource source: this.mList)
        {
            if (source.getEntryType() == _entryType)
            {
                names.add(source.getSourceCode());
            }
        }
        return names;
    }

    public String GetBudgettSourceID(int _index)
    {
        if (_index < 0 || _index >= this.mList.size())
            return "";
        return this.mList.get(_index).getID();
    }

    public BudgettSource GetBudgettSourceWithIndex(int _index)
    {
        if (_index < 0 || _index >= this.mList.size())
            return null;
        return this.mList.get(_index);
    }

    public BudgettSource GetBudgettSourceWithID(String _id)
    {
        for (BudgettSource source: this.mList)
        {
            if(source.getID().equalsIgnoreCase(_id))
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

    public ArrayList<BudgettSource> GetBudgettSourceList(BudgettEntryType _entryType)
    {
        ArrayList<BudgettSource> list = new ArrayList<>();
        for(BudgettSource source: this.mList)
        {
            if(source.getEntryType() == _entryType)
                list.add(source);
        }
        return list;
    }

    public void AddToList(BudgettSource _source)
    {
        this.mList.add(_source);
    }

    public void RemoveFromList(BudgettSource _source)
    {
        this.mList.remove(_source);
    }

    public void RemoveFromList(String _id)
    {
        for (int i = 0; i < this.mList.size(); i++)
            if (this.mList.get(i).getID().equalsIgnoreCase(_id))
                this.mList.remove(i);
    }

    public void RemoveFromListWithIndex(int _index)
    {
        this.mList.remove(_index);
    }

    public void Sort()
    {
        Collections.sort(this.mList, new Comparator<BudgettSource>()
        {
            @Override
            public int compare(BudgettSource s1, BudgettSource s2)
            {
                return s1.getSourceCode().compareTo(s2.getSourceCode());
            }
        });
    }

}
