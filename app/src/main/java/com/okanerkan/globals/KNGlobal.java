package com.okanerkan.globals;

import android.content.Context;

import com.okanerkan.budgettapp.R;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettAccount;
import com.okanerkan.sqlite.model.BudgettUser;

/**
 * Created by OkanErkan on 30.11.2017.
 */

public class KNGlobal
{
    private static Globals mGlobal = null;

    public static Globals CreateKNGlobal(Context _context)
    {
        if (mGlobal == null)
        {
            mGlobal = new Globals(_context);
        }
        return mGlobal;
    }

    public static Context FixedContext()
    {
        return mGlobal.GetContext();
    }

    public static BudgettDatabaseHelper DBHelper()
    {
        return mGlobal.DBHelper();
    }

    public static BudgettUser CurrentUser()
    {
        return mGlobal.CurrentUser();
    }

    public static BudgettAccount CurrentAccount()
    {
        return mGlobal.CurrentAccount();
    }

    public static void Reset()
    {
        mGlobal.Reset();
    }
}
