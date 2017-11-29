package com.okanerkan.globals;

import android.content.SharedPreferences;

import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettAccount;
import com.okanerkan.sqlite.model.BudgettUser;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by OkanErkan on 30.10.2017.
 */

public class Globals
{
    public static BudgettDatabaseHelper DBHelper;
    private static BudgettUser mCurrentUser = null;
    private static BudgettAccount mCurrentAccount = null;

    public static BudgettUser CurrentUser()
    {
        try
        {
            if (mCurrentUser == null)
            {
                SharedPreferences prefs = this.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
                String username = prefs.getString("Username", "");
                String password = prefs.getString("Password", "");

                if (username.equalsIgnoreCase("") || password.equalsIgnoreCase(""))
                {
                    return null;
                }

                BudgettUser user = new BudgettUser();
                user.setName(username);
                user.setPassword(password);
                if (user.Login())
                {
                    mCurrentUser = user;
                }
            }
            return mCurrentUser;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static BudgettAccount CurrentAccount()
    {
        try
        {
            if (CurrentUser() != null && mCurrentAccount == null)
            {
                BudgettAccount account = new BudgettAccount();
                account.setID(CurrentUser().getAccounID());
                mCurrentAccount = (BudgettAccount)account.Load();
            }
            return mCurrentAccount;
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
