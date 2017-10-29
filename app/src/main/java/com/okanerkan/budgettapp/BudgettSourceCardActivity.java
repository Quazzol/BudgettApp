package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BudgettSourceCardActivity extends AppCompatActivity
{
    Button mSaveButton;
    Button mDeleteButton;
    EditText mBudgettSourceCode;
    BudgettSource mBudgettSource;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_source_card);
        this.SetProperties();
        this.AddEventHandlers();
    }

    private void SetProperties()
    {
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mDeleteButton = (Button) findViewById(R.id.btnDelete);
        this.mBudgettSourceCode = (EditText) findViewById(R.id.budgettSource);

        Intent intent = getIntent();
        int index = intent.getIntExtra("BudgettSourceIndex", -1);
        this.mBudgettSource = BudgettSourceList.GetList().GetBudgettSourceWithIndex(index);
        if (this.mBudgettSource == null)
            this.mDeleteButton.setText(R.string.BtnCancel);
    }

    private void AddEventHandlers()
    {
        this.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveButtonClicked(view);
            }
        });

        this.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnDeleteButtonClicked(view);
            }
        });
    }

    public void OnSaveButtonClicked(View view)
    {
    }

    public void OnDeleteButtonClicked(View view)
    {
        if (this.mBudgettSource == null)
            this.finish();
    }
}
