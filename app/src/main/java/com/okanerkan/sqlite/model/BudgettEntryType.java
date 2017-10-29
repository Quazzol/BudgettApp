package com.okanerkan.sqlite.model;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public enum BudgettEntryType
{
    EXPENSE(0),
    INCOME(1);

    private int value;

    private BudgettEntryType(int value)
    {
        this.value = value;
    }
}
