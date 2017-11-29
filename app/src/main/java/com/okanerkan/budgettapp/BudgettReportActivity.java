package com.okanerkan.budgettapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.ScrollView;

import com.okanerkan.ui.AnimationListenerExt;
import com.okanerkan.ui.FilterUIManager;
import com.okanerkan.ui.ReportViewAdapter;
import com.okanerkan.interfaces.IFilterUser;


public class BudgettReportActivity extends AppCompatActivity implements IFilterUser
{
    private Animation mShowViewAnimation;
    private Animation mHideViewAnimation;
    private ScrollView mFilterLayout;
    private ListView mReportView;
    private FilterUIManager mFilterUIManager;

    //region Overrides

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_report);

        this.InitializeProperties();
        this.CreateFloatingButton();
        this.BindData();
        this.HideSoftKeyboard();
    }

    //endregion

    //region Initialize Methods

    private void InitializeProperties()
    {
        this.mReportView = (ListView) this.findViewById(R.id.ListViewReport);
        this.mFilterLayout = (ScrollView) this.findViewById(R.id.ScrollViewFilter);
        this.mHideViewAnimation = AnimationUtils.loadAnimation(this, R.anim.hide_view_animation);
        this.mHideViewAnimation.setAnimationListener(new AnimationListenerExt(this.mFilterLayout, false));
        this.mShowViewAnimation = AnimationUtils.loadAnimation(this, R.anim.show_view_animation);
        this.mShowViewAnimation.setAnimationListener(new AnimationListenerExt(this.mFilterLayout, true));
        this.mFilterUIManager = new FilterUIManager(this.mFilterLayout);
        this.mFilterUIManager.AddFilterChanged(this);
    }

    private void CreateFloatingButton()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.actionButtonFilter);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OnFloatingButtonClicked();
            }
        });
    }

    private void BindData()
    {
        ReportViewAdapter reportAdapter = new ReportViewAdapter(this);
        this.mReportView.setAdapter(reportAdapter.Load(null));
    }

    //endregion

    @Override
    public void FilterApplied(String _filter)
    {
        ReportViewAdapter reportAdapter = new ReportViewAdapter(this);
        this.mReportView.setAdapter(reportAdapter.Load(_filter));
        this.OnFloatingButtonClicked();
        this.HideSoftKeyboard();
    }

    private void OnFloatingButtonClicked()
    {
        this.mFilterLayout.startAnimation(this.mFilterLayout.getVisibility() == View.GONE ? this.mShowViewAnimation : this.mHideViewAnimation);
    }

    private void HideSoftKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            if (imm != null)
            {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
