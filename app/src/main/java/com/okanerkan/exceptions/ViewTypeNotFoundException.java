package com.okanerkan.exceptions;

/**
 * Created by Quazzol on 5.11.2017.
 */

public class ViewTypeNotFoundException extends Exception
{
    public ViewTypeNotFoundException(Class _class)
    {
        super(_class.getName() + " not supported!");
    }

    public ViewTypeNotFoundException(String _propertyName)
    {
        super("Cannot add " + _propertyName + " to Binding Manager.");
    }
}
