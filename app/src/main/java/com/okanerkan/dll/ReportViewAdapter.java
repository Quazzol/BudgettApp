package com.okanerkan.dll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okanerkan.budgettapp.R;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model_list.BudgettItemList;

import org.w3c.dom.Text;

/**
 * Created by Quazzol on 9.11.2017.
 */

public class ReportViewAdapter extends BaseAdapter
{
    private Context mContext;
    private BudgettItemList mList;

    public ReportViewAdapter(Context _context)
    {
        super();
        this.mContext = _context;
        this.mList = new BudgettItemList();
    }

    @Override
    public int getCount()
    {
        return this.mList.GetItemList().size();
    }

    @Override
    public Object getItem(int i)
    {
        return this.mList.GetItemList().get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final BudgettItem item = this.mList.GetItemList().get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
            convertView = layoutInflater.inflate(R.layout.gridrow_budgettitem, null);
        }

        final ImageView entryTypeImageView = (ImageView) convertView.findViewById(R.id.imgEntryType);
        final TextView entryDateTextView = (TextView) convertView.findViewById(R.id.txtEntryDate);
        final TextView sourceTextView = (TextView) convertView.findViewById(R.id.txtSource);
        final TextView typeTextView = (TextView) convertView.findViewById(R.id.txtType);
        final TextView amountTextView = (TextView) convertView.findViewById(R.id.txtAmount);

        entryDateTextView.setText(item.getEntryDate());

        return convertView;
    }

    public void Load(String _filter)
    {
        this.mList.LoadList(_filter);
    }
}
