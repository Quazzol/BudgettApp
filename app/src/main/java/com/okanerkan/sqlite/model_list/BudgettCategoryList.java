package com.okanerkan.sqlite.model_list;

import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.model.BudgettCategory;
import com.okanerkan.sqlite.model.BudgettEntryType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Quazzol on 29.10.2017.
 */

public class BudgettCategoryList
{
    private static BudgettCategoryList mSingleton;
    private ArrayList<BudgettCategory> mList;

    private BudgettCategoryList()
    {
        this.mList = new ArrayList<BudgettCategory>();
    }

    public static BudgettCategoryList GetList()
    {
        if (BudgettCategoryList.mSingleton == null)
        {
            BudgettCategoryList.mSingleton = new BudgettCategoryList();
            BudgettCategoryList.mSingleton.LoadListFromDB();
        }
        return BudgettCategoryList.mSingleton;
    }

    private void LoadListFromDB()
    {
        this.mList = Globals.DBHelper.getAllBudgettCategory();
        this.Sort();
    }

    public String GetBudgettListID(int _index)
    {
        if (_index < 0 || _index >= this.mList.size())
            return "";
        return this.mList.get(_index).getID();
    }

    public BudgettCategory GetBudgettCategoryWithIndex(int _index)
    {
        if (_index < 0 || _index >= this.mList.size())
            return null;
        return this.mList.get(_index);
    }

    public BudgettCategory GetBudgettCategoryWithID(String _id)
    {
        for (BudgettCategory category: this.mList)
        {
            if(category.getID().equalsIgnoreCase(_id))
                return category;
        }
        return null;
    }

    public BudgettCategory GetBudgettCategory(String _categoryCode)
    {
        for (BudgettCategory category: this.mList)
        {
            if(category.getCategoryCode().equals(_categoryCode))
                return category;
        }
        return null;
    }

    public ArrayList<BudgettCategory> GetBudgettCategoryList()
    {
        return this.mList;
    }

    public ArrayList<BudgettCategory> GetBudgettCategoryList(BudgettEntryType _entryType)
    {
        ArrayList<BudgettCategory> list = new ArrayList<>();
        for(BudgettCategory category: this.mList)
        {
            if(category.getEntryType() == _entryType)
                list.add(category);
        }
        return list;
    }

    public void AddToList(BudgettCategory _category)
    {
        this.mList.add(_category);
        this.Sort();
    }

    public void RemoveFromList(BudgettCategory _category)
    {
        this.mList.remove(_category);
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

    private void Sort()
    {
        Collections.sort(this.mList, new Comparator<BudgettCategory>()
        {
            @Override
            public int compare(BudgettCategory _c1, BudgettCategory _c2)
            {
                return _c1.getCategoryCode().compareTo(_c2.getCategoryCode());
            }
        });
    }
}
