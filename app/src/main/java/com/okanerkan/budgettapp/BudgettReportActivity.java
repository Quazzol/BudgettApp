package com.okanerkan.budgettapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ScrollView;

import com.okanerkan.dll.AnimationListenerExt;
import com.okanerkan.dll.FilterUIManager;
import com.okanerkan.dll.ReportViewAdapter;


public class BudgettReportActivity extends AppCompatActivity
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
    }

    private void CreateFloatingButton()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.actionButtonFilter);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mFilterLayout.startAnimation(mFilterLayout.getVisibility() == View.GONE ? mShowViewAnimation : mHideViewAnimation);
            }
        });
    }

    private void BindData()
    {
        ReportViewAdapter reportAdapter = new ReportViewAdapter(this);
        this.mReportView.setAdapter(reportAdapter.Load(null));
    }

    //endregion
}
