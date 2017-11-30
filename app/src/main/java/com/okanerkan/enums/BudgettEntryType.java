package com.okanerkan.enums;

import com.okanerkan.interfaces.IRadioGroupSource;

/**
 * Created by OkanErkan on 10.10.2017.
 */

public enum BudgettEntryType implements IRadioGroupSource
{
    EXPENSE(0),
    INCOME(1);

    private int value;
    private BudgettEntryType(int value)
    {
        this.value = value;
    }

    @Override
    public int getValue() { return this.value; }
}
