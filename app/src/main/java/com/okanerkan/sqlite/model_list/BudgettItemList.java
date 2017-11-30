package com.okanerkan.sqlite.model_list;

import com.okanerkan.dll.Tuple;
import com.okanerkan.globals.KNGlobal;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.enums.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettItem;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by OkanErkan on 30.10.2017.
 */

public class BudgettItemList
{
    public BudgettItemList()
    {
        this.mList = new ArrayList<>();
    }

    //region Members

    private ArrayList<BudgettItem> mList;

    //endregion

    //region Methods

    // TODO Implement filter classes and use them

    public ArrayList<BudgettItem> GetItemList()
    {
        return this.mList;
    }

    public BudgettItemList LoadList(String _filter)
    {
        this.mList = KNGlobal.DBHelper().getAllBudgettItem(_filter);
        return this;
    }

    public Tuple<Double, Double> GetMonthlyStatements()
    {
        String filter = " WHERE "
                + BudgettDatabaseHelper.KEY_ITEM_ENTRY_DATE + " >= "
                + this.StartOfMonth() + " AND "
                + BudgettDatabaseHelper.KEY_ITEM_ENTRY_DATE + " <= "
                + this.EndOfMonth();

        this.mList = KNGlobal.DBHelper().getAllBudgettItem(filter);

        Tuple<Double, Double> incExp = new Tuple<>(0.0, 0.0);
        for(BudgettItem item : this.mList)
        {
            if (item.getEntryType() == BudgettEntryType.INCOME)
            {
                incExp.x += item.getAmount();
            }
            else
            {
                incExp.y += item.getAmount();
            }
        }

        return incExp;
    }

    private long StartOfMonth()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return cal.getTimeInMillis();
    }

    private long EndOfMonth()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);

        return cal.getTimeInMillis();
    }
    //endregion
}
