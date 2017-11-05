package com.okanerkan.dll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.okanerkan.budgettapp.R;

import java.util.List;

/**
 * Created by OkanErkan on 3.11.2017.
 */

public class SideMenuAdapter extends BaseAdapter
{
    private List<SideMenuItem> mItems;
    private Context mContext;

    public SideMenuAdapter(List<SideMenuItem> _items, Context _context)
    {
        this.mItems = _items;
        this.mContext = _context;
    }

    @Override
    public int getCount()
    {
        return this.mItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.mItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();

            view = LayoutInflater.from(this.mContext).inflate(R.layout.sidemenu_item, null);

            holder.TitleTextView = (TextView) view.findViewById(R.id.txtSideMenuItemTitle);
            holder.TitleTextView.setText(this.mItems.get(position).getTitle());

            holder.IconImageView = (ImageView) view.findViewById(R.id.imgSideMenuItemIcom);
            holder.IconImageView.setImageResource(this.mItems.get(position).getIconID());

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }

    static class ViewHolder
    {
        public TextView TitleTextView;
        public ImageView IconImageView;
    }
}
