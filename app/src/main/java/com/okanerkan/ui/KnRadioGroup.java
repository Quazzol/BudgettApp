package com.okanerkan.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by OkanErkan on 20.11.2017.
 */

public class KnRadioGroup extends RadioGroup
{
    public KnRadioGroup(Context context)
    {
        super(context);
    }

    public KnRadioGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void ResetToDefault()
    {
        this.SetCheckedOnIndex(0);
    }

    public void SetCheckedOnIndex(int _index)
    {
        int count = this.getChildCount();
        for (int i = 0; i < count; i++)
        {
            View child = this.getChildAt(i);
            if (child instanceof RadioButton)
            {
                RadioButton rbtn = (RadioButton) child;
                rbtn.setChecked(i == _index);
            }
        }
    }
}
