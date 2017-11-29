package com.okanerkan.exceptions;

/**
 * Created by OkanErkan on 29.11.2017.
 */

public class IncorrectTypeException extends Exception
{
    public IncorrectTypeException(String _typeName)
    {
        super(_typeName + " not found!");
    }
}
