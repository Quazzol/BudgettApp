package com.okanerkan.ui;

import android.content.Context;

import com.okanerkan.budgettapp.R;

/**
 * Created by OkanErkan on 17.11.2017.
 */

public enum FilterDatePeriod
{
    TODAY(R.string.Today),
    YESTERDAY(R.string.Yesterday),
    THISWEEK(R.string.ThisWeek),
    LASTWEEK(R.string.LastWeek),
    THISMONTH(R.string.ThisMonth),
    LASTMONTH(R.string.LastMonth),
    LAST3MONTHS(R.string.LastThreeMonths);

    private int value;
    private FilterDatePeriod(int value)
    {
        this.value = value;
    }

    public int getValue() { return this.value; }
    public String getText(Context _context) { return _context.getResources().getString(this.value); }
}
