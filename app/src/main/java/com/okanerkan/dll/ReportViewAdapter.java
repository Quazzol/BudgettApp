package com.okanerkan.dll;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okanerkan.budgettapp.R;
import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model_list.BudgettItemList;
import com.okanerkan.sqlite.model_list.BudgettSourceList;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;

import java.util.Locale;

/**
 * Created by Quazzol on 9.11.2017.
 */

public class ReportViewAdapter extends BaseAdapter
{
    private Context mContext;
    private BudgettItemList mList;
    private String mUserCurrencyCode = "$";

    public ReportViewAdapter(Context _context)
    {
        super();
        this.mContext = _context;
        this.mList = new BudgettItemList();

        SharedPreferences prefs = this.mContext.getSharedPreferences("com.okanerkan.budgettapp", this.mContext.MODE_PRIVATE);
        String currencyCode = prefs.getString("CurrencyCode", "");
        this.mUserCurrencyCode = currencyCode.isEmpty() ? "$" : currencyCode;
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

        entryTypeImageView.setImageResource(item.getEntryType() == BudgettEntryType.INCOME ? R.drawable.up_arrow : R.drawable.down_arrow);
        entryDateTextView.setText(Globals.GetDateAsString(item.getEntryDate()));
        sourceTextView.setText(BudgettSourceList.GetList().GetBudgettSource(item.getBudgettSource()).getSourceCode());
        typeTextView.setText(BudgettCategoryList.GetList().GetBudgettCategory(item.getBudgettType()).getCategoryCode());
        amountTextView.setText(String.format(Locale.getDefault(), "%.2f %s", item.getAmount(), this.mUserCurrencyCode));

        return convertView;
    }

    public void Load(String _filter)
    {
        this.mList.LoadList(_filter);
    }
}
