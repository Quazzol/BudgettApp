package com.okanerkan.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.okanerkan.budgettapp.R;

import java.util.ArrayList;

/**
 * Created by Quazzol on 9.11.2017.
 */

public class GridViewAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<GridItem> mList;

    public GridViewAdapter(Context _context, ArrayList<GridItem> _list)
    {
        super();
        this.mContext = _context;
        this.mList = _list;
    }

    @Override
    public int getCount()
    {
        return this.mList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return this.mList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final GridItem item = this.mList.get(position);

        if (convertView == null)
        {
            final LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
            convertView = layoutInflater.inflate(R.layout.gridview_cell, null);
        }

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chkBoxGridCell);
        checkBox.setText(item.getCheckBoxText());
        checkBox.setChecked(item.getIsChecked());
        checkBox.setTag(item.getID());

        return convertView;
    }

}