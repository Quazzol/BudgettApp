package com.okanerkan.dll;

/**
 * Created by OkanErkan on 14.11.2017.
 */

public class GridItem extends ObservableBase
{
    public GridItem(String _boxText)
    {
        this.mCheckBoxText = _boxText;
    }

    private boolean mIsChecked = false;
    private String mCheckBoxText;

    public boolean getIsChecked() { return this.mIsChecked; }
    public void setIsChecked(boolean _isChecked) { this.SetValue("IsChecked", _isChecked); }

    public String getCheckBoxText() { return this.mCheckBoxText; }
    public void setCheckBoxText(String _boxText) { this.SetValue("CheckBoxText", _boxText); }
}
