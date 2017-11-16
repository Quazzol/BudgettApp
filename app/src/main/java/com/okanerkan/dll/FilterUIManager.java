package com.okanerkan.dll;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.okanerkan.budgettapp.R;

/**
 * Created by OkanErkan on 16.11.2017.
 */

public class FilterUIManager
{
    public FilterUIManager(ViewGroup _reportView)
    {
        this.mReportView = _reportView;
        this.InitializeProperties();
        this.AddHandlers();
    }

    private ViewGroup mReportView;

    private FilterUIManager.ViewHolder mDateViewHolder;
    private FilterUIManager.ViewHolder mSourceViewHolder;
    private FilterUIManager.ViewHolder mCategoryViewHolder;

    private class ViewHolder
    {
        Switch mSwitchControl;
        LinearLayout mLayoutArea;
        Animation mHideViewAnimation;
        Animation mShowViewAnimation;
    }

    private void InitializeProperties()
    {
        this.mDateViewHolder = this.CreateViewHolder(R.id.SwitchUseDateFilter, R.id.LinearLayoutDateArea);
        this.mSourceViewHolder = this.CreateViewHolder(R.id.SwitchUseSourceFilter, R.id.LinearLayoutSourceArea);
        this.mCategoryViewHolder = this.CreateViewHolder(R.id.SwitchUseCategoryFilter, R.id.LinearLayoutCategoryArea);
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

}
