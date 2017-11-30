package com.okanerkan.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okanerkan.budgettapp.R;
import com.okanerkan.globals.TimeStampHelper;
import com.okanerkan.enums.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model_list.BudgettItemList;
import com.okanerkan.sqlite.model_list.BudgettSourceList;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;

import java.util.Locale;

/**
 * Created by Quazzol on 9.11.2017.
 */

public class ReportViewAdapter extends BaseAdapter implements View.OnClickListener
{
    public ReportViewAdapter(Context _context)
    {
        super();
        this.mContext = _context;
        this.mList = new BudgettItemList();

        SharedPreferences prefs = this.mContext.getSharedPreferences("com.okanerkan.budgettapp", Context.MODE_PRIVATE);
        String currencyCode = prefs.getString("CurrencyCode", "");
        this.mUserCurrencyCode = currencyCode.isEmpty() ? "$" : currencyCode;
    }

    //region Members

    private static class ViewHolder
    {
        ImageView mEntryTypeImageView;
        TextView mEntryDateTextView;
        TextView mSourceTextView;
        TextView mCategoryTextView;
        TextView mAmountTextView;
    }

    private Context mContext;
    private BudgettItemList mList;
    private String mUserCurrencyCode = "$";

    //endregion

    @Override
    public void onClick(View _view)
    {
        int position = (Integer) _view.getTag();
        BudgettItem budgettItem = (BudgettItem) getItem(position);
        Snackbar.make(_view, budgettItem.getBudgettNote(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
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
        BudgettItem budgettItem = (BudgettItem) this.getItem(position);
        ReportViewAdapter.ViewHolder viewHolder;
        final View resultView;

        if (convertView == null)
        {
            viewHolder = new ReportViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            viewHolder.mEntryTypeImageView = (ImageView) convertView.findViewById(R.id.imgEntryType);
            viewHolder.mEntryDateTextView = (TextView) convertView.findViewById(R.id.txtEntryDate);
            viewHolder.mSourceTextView = (TextView) convertView.findViewById(R.id.txtSource);
            viewHolder.mCategoryTextView = (TextView) convertView.findViewById(R.id.txtCategory);
            viewHolder.mAmountTextView = (TextView) convertView.findViewById(R.id.txtAmount);

            resultView = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ReportViewAdapter.ViewHolder) convertView.getTag();
            resultView = convertView;
        }

        viewHolder.mEntryTypeImageView.setImageResource(budgettItem.getEntryType() == BudgettEntryType.INCOME ? R.drawable.up_arrow : R.drawable.down_arrow);
        viewHolder.mEntryDateTextView.setText(TimeStampHelper.GetDateAsString(budgettItem.getEntryDate()));
        viewHolder.mSourceTextView.setText(BudgettSourceList.GetList().GetBudgettSource(budgettItem.getSourceID()).getSourceCode());
        viewHolder.mCategoryTextView.setText(BudgettCategoryList.GetList().GetBudgettCategory(budgettItem.getCategoryID()).getCategoryCode());
        viewHolder.mAmountTextView.setText(String.format(Locale.getDefault(), "%.2f %s", budgettItem.getAmount(), this.mUserCurrencyCode));

        return resultView;
    }

    public ReportViewAdapter Load(String _filter)
    {
        this.mList.LoadList(_filter);
        return this;
    }
}
