package com.okanerkan.globals;

import android.icu.util.Calendar;

import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;

import java.util.Locale;

/**
 * Created by OkanErkan on 30.10.2017.
 */

public class Globals
{
    public static BudgettDatabaseHelper DBHelper;

    public static String GetDateAsString(long _timestamp)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(_timestamp);
        String dateText = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));

        return  dateText;
    }
}
