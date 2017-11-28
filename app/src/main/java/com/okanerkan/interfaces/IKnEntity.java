package com.okanerkan.interfaces;

/**
 * Created by OkanErkan on 28.11.2017.
 */

public interface IKnEntity
{
    boolean ExistInDB();
    boolean Save();
    boolean Insert();
    boolean Update();
    boolean Delete();
    IKnEntity Load();
}
