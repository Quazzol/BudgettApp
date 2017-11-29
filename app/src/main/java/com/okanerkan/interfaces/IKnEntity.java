package com.okanerkan.interfaces;

import com.okanerkan.exceptions.IncorrectTypeException;
import com.okanerkan.exceptions.TableNotFoundException;
import com.okanerkan.exceptions.ValidationException;

/**
 * Created by OkanErkan on 28.11.2017.
 */

public interface IKnEntity
{
    boolean ExistInDB();
    boolean Save() throws TableNotFoundException, ValidationException, IncorrectTypeException;
    boolean Insert() throws TableNotFoundException, ValidationException, IncorrectTypeException;
    boolean Update() throws TableNotFoundException, ValidationException, IncorrectTypeException;
    boolean Delete() throws TableNotFoundException, IncorrectTypeException;
    IKnEntity Load() throws TableNotFoundException, IncorrectTypeException;
}
