package com.okanerkan.ui;

import com.okanerkan.dll.ObservableBase;

/**
 * Created by OkanErkan on 14.11.2017.
 */

public class GridItem extends ObservableBase
{
    public GridItem(String _id, String _boxText)
    {
        this.mID = _id;
        this.mCheckBoxText = _boxText;
    }

    private boolean mIsChecked = false;
    private String mID;
    private String mCheckBoxText;

    public boolean getIsChecked() { return this.mIsChecked; }
    public void setIsChecked(boolean _isChecked) { this.SetValue("IsChecked", _isChecked); }

    public String getID() { return this.mID; }
    public void setID(String _id) { this.SetValue("ID", _id); }

    public String getCheckBoxText() { return this.mCheckBoxText; }
    public void setCheckBoxText(String _boxText) { this.SetValue("CheckBoxText", _boxText); }
}
