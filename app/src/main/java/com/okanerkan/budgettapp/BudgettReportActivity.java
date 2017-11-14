package com.okanerkan.budgettapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.okanerkan.dll.GridItem;
import com.okanerkan.dll.GridViewAdapter;
import com.okanerkan.dll.ReportViewAdapter;

import java.util.ArrayList;

public class BudgettReportActivity extends AppCompatActivity
{
    private Animation mShowViewAnimation;
    private Animation mHideViewAnimation;
    private LinearLayout mFilterLayout;
    private ListView mReportView;

    //region Overrides

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_report);

        this.InitializeProperties();
        this.CreateFloatingButton();
        this.BindData();
        this.CreateFilterControls();
    }

    //endregion

    //region Initialize Methods

    private void InitializeProperties()
    {
        this.mReportView = (ListView) this.findViewById(R.id.ListViewReport);
        this.mFilterLayout = (LinearLayout) this.findViewById(R.id.LinearLayoutFilter);
        this.mHideViewAnimation = AnimationUtils.loadAnimation(this, R.anim.hide_view_animation);
        this.mHideViewAnimation.setAnimationListener(new AnimationListenerExt(this.mFilterLayout, false));
        this.mShowViewAnimation = AnimationUtils.loadAnimation(this, R.anim.show_view_animation);
        this.mShowViewAnimation.setAnimationListener(new AnimationListenerExt(this.mFilterLayout, true));
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

    private void CreateFilterControls()
    {
        GridView gridViewSource = (GridView) findViewById(R.id.GridViewSource);
        GridViewAdapter sourceAdapter = new GridViewAdapter(this, new ArrayList<GridItem>());
        gridViewSource.setAdapter(sourceAdapter);

        GridView gridViewCategory = (GridView) findViewById(R.id.GridViewCategory);
        GridViewAdapter categoryAdapter = new GridViewAdapter(this, new ArrayList<GridItem>());
        gridViewCategory.setAdapter(categoryAdapter);
    }

    //endregion

    // region AnimationListener

    private class AnimationListenerExt implements Animation.AnimationListener
    {
        private View mView;
        private boolean mIsShow;

        public AnimationListenerExt(View _view, boolean _isShow)
        {
            this.mView = _view;
            this.mIsShow = _isShow;
        }

        @Override
        public void onAnimationStart(Animation animation)
        {
            if (this.mIsShow)
            {
                this.mView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            if (!this.mIsShow)
            {
                this.mView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {

        }
    }

    //endregion

}
