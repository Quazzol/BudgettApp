package com.okanerkan.budgettapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.okanerkan.dll.ReportViewAdapter;

public class BudgettReportActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_report);

        GridView gridView = (GridView)findViewById(R.id.grdViewReport);
        ReportViewAdapter booksAdapter = new ReportViewAdapter(this);
        booksAdapter.Load(null);
        gridView.setAdapter(booksAdapter);
    }
}
