package com.okanerkan.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.okanerkan.budgettapp.R;
import com.okanerkan.globals.Globals;
import com.okanerkan.globals.TimeStampHelper;
import com.okanerkan.interfaces.IFilterUser;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettCategory;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;
import com.okanerkan.sqlite.model_list.BudgettSourceList;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by OkanErkan on 16.11.2017.
 */

public class FilterUIManager
{
    public FilterUIManager(ViewGroup _reportView)
    {
        this.mReportView = _reportView;
        this.InitializeProperties();
        this.CreateCheckBoxControls();
        this.LoadSpinners();
        this.AddHandlers();
    }

    //region Members

    private final String TAG = "FilterUIManager";

    private ViewGroup mReportView;

    private FilterUIManager.ViewHolder mDateViewHolder;
    private Spinner mPreparedDateSpinner;
    private DateEditText mStartDateEdit;
    private DateEditText mEndDateEdit;

    private KnRadioGroup mEntryTypeRadioGroup;

    private FilterUIManager.ViewHolder mSourceViewHolder;
    private GridView mSourceGridView;

    private FilterUIManager.ViewHolder mCategoryViewHolder;
    private GridView mCategoryGridView;

    private EditText mNoteEdit;

    private Button mApplyFilterButton;
    private Button mResetFilterButton;

    private ArrayList<IFilterUser> mFilterUsers;

    private class ViewHolder
    {
        Switch mSwitchControl;
        LinearLayout mLayoutArea;
        Animation mHideViewAnimation;
        Animation mShowViewAnimation;
    }

    //endregion

    //region Initialize Methods

    private void InitializeProperties()
    {
        this.mFilterUsers = new ArrayList<IFilterUser>();

        this.mDateViewHolder = this.CreateViewHolder(R.id.SwitchUseDateFilter, R.id.LinearLayoutDateArea);
        this.mPreparedDateSpinner = (Spinner) this.mReportView.findViewById(R.id.SpinnerPreparedDate);
        this.mStartDateEdit = (DateEditText) this.mReportView.findViewById(R.id.DateEditStartDate);
        this.mEndDateEdit = (DateEditText) this.mReportView.findViewById(R.id.DateEditEndDate);

        this.mEntryTypeRadioGroup = (KnRadioGroup) this.mReportView.findViewById(R.id.rdgEntryType);

        this.mSourceViewHolder = this.CreateViewHolder(R.id.SwitchUseSourceFilter, R.id.LinearLayoutSourceArea);
        this.mSourceGridView = (GridView) this.mReportView.findViewById(R.id.GridViewSource);

        this.mCategoryViewHolder = this.CreateViewHolder(R.id.SwitchUseCategoryFilter, R.id.LinearLayoutCategoryArea);
        this.mCategoryGridView = (GridView) this.mReportView.findViewById(R.id.GridViewCategory);

        this.mNoteEdit = (EditText) this.mReportView.findViewById(R.id.EditTextNote);

        this.mApplyFilterButton = (Button) this.mReportView.findViewById(R.id.btnFilter);
        this.mResetFilterButton = (Button) this.mReportView.findViewById(R.id.btnReset);
    }

    private FilterUIManager.ViewHolder CreateViewHolder(int _switchID, int _areaID)
    {
        FilterUIManager.ViewHolder _viewHolder = new FilterUIManager.ViewHolder();
        _viewHolder.mSwitchControl = (Switch) this.mReportView.findViewById(_switchID);
        _viewHolder.mLayoutArea = (LinearLayout) this.mReportView.findViewById(_areaID);
        _viewHolder.mHideViewAnimation = AnimationUtils.loadAnimation(this.mReportView.getContext(), R.anim.hide_view_animation);
        _viewHolder.mHideViewAnimation.setAnimationListener(new AnimationListenerExt(_viewHolder.mLayoutArea, false));
        _viewHolder.mShowViewAnimation = AnimationUtils.loadAnimation(this.mReportView.getContext(), R.anim.show_view_animation);
        _viewHolder.mShowViewAnimation.setAnimationListener(new AnimationListenerExt(_viewHolder.mLayoutArea, true));

        return _viewHolder;
    }

    private void AddHandlers()
    {
        this.AddSwitchChangedHandler(this.mDateViewHolder);
        this.AddSwitchChangedHandler(this.mSourceViewHolder);
        this.AddSwitchChangedHandler(this.mCategoryViewHolder);
        this.mPreparedDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                OnPreparedDateChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        this.mApplyFilterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OnApplyFilterClicked();
            }
        });

        this.mResetFilterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OnResetButtonClicked();
            }
        });
    }

    private void AddSwitchChangedHandler(final FilterUIManager.ViewHolder _viewHolder)
    {
        _viewHolder.mSwitchControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                _viewHolder.mLayoutArea.startAnimation(_viewHolder.mLayoutArea.getVisibility() == View.GONE ? _viewHolder.mShowViewAnimation : _viewHolder.mHideViewAnimation);
            }
        });
    }

    private void CreateCheckBoxControls()
    {
        ArrayList<GridItem> items = new ArrayList<GridItem>();
        for (BudgettSource source : BudgettSourceList.GetList().GetBudgettSourceList())
        {
            items.add(new GridItem(source.getID(), source.getSourceCode()));
        }
        GridViewAdapter sourceAdapter = new GridViewAdapter(this.mReportView.getContext(), items);
        this.mSourceGridView.setAdapter(sourceAdapter);

        items = new ArrayList<GridItem>();
        for (BudgettCategory category : BudgettCategoryList.GetList().GetBudgettCategoryList())
        {
            items.add(new GridItem(category.getID(), category.getCategoryCode()));
        }
        GridViewAdapter categoryAdapter = new GridViewAdapter(this.mReportView.getContext(), items);
        this.mCategoryGridView.setAdapter(categoryAdapter);
    }

    private void LoadSpinners()
    {
        Context context = this.mReportView.getContext();
        ArrayList<PreparedDateSource> list = new ArrayList<PreparedDateSource>();
        list.add(new PreparedDateSource(FilterDatePeriod.TODAY, context));
        list.add(new PreparedDateSource(FilterDatePeriod.YESTERDAY, context));
        list.add(new PreparedDateSource(FilterDatePeriod.THISWEEK, context));
        list.add(new PreparedDateSource(FilterDatePeriod.LASTWEEK, context));
        list.add(new PreparedDateSource(FilterDatePeriod.THISMONTH, context));
        list.add(new PreparedDateSource(FilterDatePeriod.LASTMONTH, context));
        list.add(new PreparedDateSource(FilterDatePeriod.LAST3MONTHS, context));

        ArrayAdapter<PreparedDateSource> sourceAdapter = new ArrayAdapter<PreparedDateSource>(this.mReportView.getContext(),
                android.R.layout.simple_spinner_item,
                list);

        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mPreparedDateSpinner.setAdapter(sourceAdapter);
    }

    //endregion

    //region Event Methods

    private void OnPreparedDateChanged()
    {
        PreparedDateSource selectedItem = (PreparedDateSource) this.mPreparedDateSpinner.getSelectedItem();
        this.mEndDateEdit.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());

        switch (selectedItem.getDatePeriod())
        {
            case TODAY:
                this.mStartDateEdit.setTimestamp(TimeStampHelper.GetToday());
                this.mEndDateEdit.setTimestamp(TimeStampHelper.GetTomorrow());
                break;
            case YESTERDAY:
                this.mStartDateEdit.setTimestamp(TimeStampHelper.GetYesterday());
                this.mEndDateEdit.setTimestamp(TimeStampHelper.GetToday());
                break;
            case THISWEEK:
                this.mStartDateEdit.setTimestamp(TimeStampHelper.GetStartOfThisWeek());
                this.mEndDateEdit.setTimestamp(TimeStampHelper.GetTomorrow());
                break;
            case LASTWEEK:
                this.mStartDateEdit.setTimestamp(TimeStampHelper.GetStartOfLastWeek());
                this.mEndDateEdit.setTimestamp(TimeStampHelper.GetStartOfThisWeek());
                break;
            case THISMONTH:
                this.mStartDateEdit.setTimestamp(TimeStampHelper.GetStartOfThisMonth());
                this.mEndDateEdit.setTimestamp(TimeStampHelper.GetTomorrow());
                break;
            case LASTMONTH:
                this.mStartDateEdit.setTimestamp(TimeStampHelper.GetStartOfLastMonth());
                this.mEndDateEdit.setTimestamp(TimeStampHelper.GetStartOfThisMonth());
                break;
            case LAST3MONTHS:
                this.mStartDateEdit.setTimestamp(TimeStampHelper.GetStartOfTheeMonthAgo());
                this.mEndDateEdit.setTimestamp(TimeStampHelper.GetYesterday());
                break;
        }
    }

    private void OnApplyFilterClicked()
    {
        try
        {
            String filter = " WHERE ";
            if (this.mDateViewHolder.mSwitchControl.isChecked())
            {
                filter += "(" + BudgettDatabaseHelper.KEY_ITEM_ENTRY_DATE + " >= " + this.mStartDateEdit.getTimeStamp() + " AND ";
                filter += BudgettDatabaseHelper.KEY_ITEM_ENTRY_DATE + " <= " + this.mEndDateEdit.getTimeStamp() + ") AND ";
            }

            RadioButton selectedRadioButton = (RadioButton) this.mReportView.findViewById(this.mEntryTypeRadioGroup.getCheckedRadioButtonId());
            int selectedTag = Integer.parseInt(selectedRadioButton.getTag().toString());
            if (selectedTag != 2)
            {
                filter += "(" + BudgettDatabaseHelper.KEY_ENTRY_TYPE + " = " + selectedTag + ") AND ";
            }

            if (this.mSourceViewHolder.mSwitchControl.isChecked())
            {
                String filterStr = "(";
                for (int i = 0; i < this.mSourceGridView.getChildCount(); i++)
                {
                    CheckBox chkBox = (CheckBox) ((ViewGroup) this.mSourceGridView.getChildAt(i)).getChildAt(0);
                    if (chkBox.isChecked())
                    {
                        filterStr += BudgettDatabaseHelper.KEY_ITEM_SOURCE_ID + " = " + chkBox.getTag() + " OR ";
                    }
                }
                if (filterStr.length() > 1)
                {
                    filter += filterStr.substring(0, filterStr.length() - 4) + ") AND ";
                }
            }

            if (this.mCategoryViewHolder.mSwitchControl.isChecked())
            {
                String filterStr = "(";
                for (int i = 0; i < this.mCategoryGridView.getChildCount(); i++)
                {
                    CheckBox chkBox = (CheckBox) ((ViewGroup) this.mCategoryGridView.getChildAt(i)).getChildAt(0);
                    if (chkBox.isChecked())
                    {
                        filterStr += BudgettDatabaseHelper.KEY_ITEM_CATEGORY_ID + " = " + chkBox.getTag() + " OR ";
                    }
                }
                if (filterStr.length() > 1)
                {
                    filter += filterStr.substring(0, filterStr.length() - 4) + ") AND ";
                }
            }

            if (!this.mNoteEdit.getText().toString().isEmpty())
            {
                filter += "(" + BudgettDatabaseHelper.KEY_ITEM_NOTE + " LIKE %'" + this.mNoteEdit.getText().toString() + "'%) AND ";
            }
            filter = filter.substring(0, filter.length() - 5);

            this.RaiseFilterChanged(filter);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void OnResetButtonClicked()
    {
        this.mPreparedDateSpinner.setSelection(0);
        this.mDateViewHolder.mSwitchControl.setChecked(false);

        this.mEntryTypeRadioGroup.ResetToDefault();

        for (int i = 0; i < this.mSourceGridView.getChildCount(); i++)
        {
            ((CheckBox)((ViewGroup) this.mSourceGridView.getChildAt(i)).getChildAt(0)).setChecked(false);
        }
        this.mSourceViewHolder.mSwitchControl.setChecked(false);

        for (int i = 0; i < this.mCategoryGridView.getChildCount(); i++)
        {
            ((CheckBox)((ViewGroup) this.mCategoryGridView.getChildAt(i)).getChildAt(0)).setChecked(false);
        }
        this.mCategoryViewHolder.mSwitchControl.setChecked(false);

        this.mNoteEdit.setText("");
    }

    //endregion

    public void AddFilterChanged(IFilterUser _filterUser)
    {
        this.mFilterUsers.add(_filterUser);
    }

    private void RaiseFilterChanged(String _filter)
    {
        for (IFilterUser filterUser : this.mFilterUsers)
        {
            filterUser.FilterApplied(_filter);
        }
    }

    private class PreparedDateSource
    {
        public PreparedDateSource(FilterDatePeriod _datePeriod, Context _context)
        {
            this.mDatePeriod = _datePeriod;
            this.mText = this.mDatePeriod.getText(_context);
        }

        private FilterDatePeriod mDatePeriod;
        public FilterDatePeriod getDatePeriod() { return this.mDatePeriod; }

        private String mText;

        public String toString()
        {
            return this.mText;
        }
    }

}
