package com.okanerkan.enums;

import com.okanerkan.interfaces.IRadioGroupSource;

/**
 * Created by OkanErkan on 30.11.2017.
 */

public enum LoginResult implements IRadioGroupSource
{
    LOGIN_FAILED(0),
    LOGIN_SUCCESS(1),
    NO_USER(2);

    private int value;
    private LoginResult(int value)
    {
        this.value = value;
    }

    @Override
    public int getValue() { return this.value; }
}
