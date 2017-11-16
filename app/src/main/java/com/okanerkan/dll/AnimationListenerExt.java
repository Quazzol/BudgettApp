package com.okanerkan.dll;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by OkanErkan on 16.11.2017.
 */

public class AnimationListenerExt implements Animation.AnimationListener
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
