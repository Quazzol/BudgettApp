package com.okanerkan.exceptions;

/**
 * Created by OkanErkan on 29.11.2017.
 */

public class TableNotFoundException extends Exception
{
    public TableNotFoundException(String _tableName)
    {
        super(_tableName + " not found!");
    }
}