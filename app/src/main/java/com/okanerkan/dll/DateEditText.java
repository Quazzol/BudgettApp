package com.okanerkan.dll;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import com.okanerkan.budgettapp.R;
import com.okanerkan.globals.Globals;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by OkanErkan on 6.11.2017.
 */

public class DateEditText extends android.support.v7.widget.AppCompatEditText
{
    public DateEditText(Context context)
    {
        super(context);
        this.SetDefault();
    }

    public DateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.SetDefault();
    }

    public DateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.SetDefault();
    }

    private void SetDefault()
    {
        this.mTimeStamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OnDateEditClicked();
            }
        });
    }

    private long mTimeStamp;
    public long getTimeStamp() { return this.mTimeStamp; }

    public void SetTimestamp(long _timestamp)
    {
        this.mTimeStamp = _timestamp;
        this.setText(Globals.GetDateAsString(this.mTimeStamp));
    }

    private void OnDateEditClicked()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.mTimeStamp);

        DatePickerDialog datePicker;
        datePicker = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                SetDateValue(year, monthOfYear + 1, dayOfMonth);
            }
        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setTitle(R.string.PickADate);
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, this.getResources().getString(R.string.BtnSet), datePicker);
        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, this.getResources().getString(R.string.BtnCancel), datePicker);
        datePicker.show();
    }

    private void SetDateValue(int year, int month, int day)
    {
        try
        {
            String dateText = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
            this.setText(dateText);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(dateText);
            this.mTimeStamp = new Timestamp(date.getTime()).getTime();
        }
        catch (Exception ex)
        {
            this.mTimeStamp = new Timestamp(System.currentTimeMillis()).getTime();
        }
    }
}
