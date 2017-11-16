package com.okanerkan.budgettapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.okanerkan.dll.AnimationListenerExt;
import com.okanerkan.dll.FilterUIManager;
import com.okanerkan.dll.GridItem;
import com.okanerkan.dll.GridViewAdapter;
import com.okanerkan.dll.ReportViewAdapter;
import com.okanerkan.sqlite.model.BudgettCategory;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;
import com.okanerkan.sqlite.model_list.BudgettSourceList;

import java.util.ArrayList;

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
        this.CreateFilterControls();
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

    private void CreateFilterControls()
    {
        GridView gridViewSource = (GridView) findViewById(R.id.GridViewSource);
        ArrayList<GridItem> items = new ArrayList<GridItem>();
        for (BudgettSource source : BudgettSourceList.GetList().GetBudgettSourceList())
        {
            items.add(new GridItem(source.getID(), source.getSourceCode()));
        }
        GridViewAdapter sourceAdapter = new GridViewAdapter(this, items);
        gridViewSource.setAdapter(sourceAdapter);

        GridView gridViewCategory = (GridView) findViewById(R.id.GridViewCategory);
        items = new ArrayList<GridItem>();
        for (BudgettCategory category : BudgettCategoryList.GetList().GetBudgettCategoryList())
        {
            items.add(new GridItem(category.getID(), category.getCategoryCode()));
        }
        GridViewAdapter categoryAdapter = new GridViewAdapter(this, items);
        gridViewCategory.setAdapter(categoryAdapter);
    }

    //endregion
}
