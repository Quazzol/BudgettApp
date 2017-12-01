package com.okanerkan.globals;

import android.content.Context;
import android.content.SharedPreferences;

import com.okanerkan.enums.LoginResult;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettAccount;
import com.okanerkan.sqlite.model.BudgettUser;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by OkanErkan on 30.10.2017.
 */

public class Globals
{
    private Context mContext = null;
    private BudgettDatabaseHelper mDBHelper = null;
    private BudgettUser mCurrentUser = null;
    private BudgettAccount mCurrentAccount = null;

    public Globals(Context _context)
    {
        this.mContext = _context;
        this.mDBHelper = new BudgettDatabaseHelper(this.mContext);
    }

    public BudgettDatabaseHelper DBHelper()
    {
        return this.mDBHelper;
    }

    public BudgettUser CurrentUser()
    {
        try
        {
            if (mCurrentUser == null)
            {
                SharedPreferences prefs = this.mContext.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
                String username = prefs.getString("Username", "");
                String password = prefs.getString("Password", "");

                if (username.equalsIgnoreCase("") || password.equalsIgnoreCase(""))
                {
                    return null;
                }

                BudgettUser user = new BudgettUser();
                user.setName(username);
                user.setPassword(password);
                if (user.Login() == LoginResult.LOGIN_SUCCESS)
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

    public BudgettAccount CurrentAccount()
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

    public void Reset()
    {
        this.mCurrentUser = null;
        this.mCurrentAccount = null;
        SharedPreferences prefs = this.mContext.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public Context GetContext()
    {
        return this.mContext;
    }
}
