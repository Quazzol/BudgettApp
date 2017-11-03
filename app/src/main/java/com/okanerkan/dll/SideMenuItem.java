package com.okanerkan.dll;

/**
 * Created by OkanErkan on 3.11.2017.
 */

public class SideMenuItem
{
    private int mIconID;
    private String mTitle;

    public SideMenuItem() {}

    public SideMenuItem(int _iconID, String _title)
    {
        this.mIconID = _iconID;
        this.mTitle = _title;
    }

    public int getIconID() { return this.mIconID; }
    public void setIconID(int _iconID) { this.mIconID = _iconID; }

    public String getTitle() { return this.mTitle; }
    public void setTitle(String _title) { this.mTitle = _title; }

}
