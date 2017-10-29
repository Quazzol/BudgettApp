package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.okanerkan.sqlite.model.BudgettType;
import com.okanerkan.sqlite.model.BudgettTypeList;

public class BudgettTypeCardActivity extends AppCompatActivity {

    Button mSaveButton;
    Button mDeleteButton;
    EditText mBudgettTypeCode;
    BudgettType mBudgettType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_type_card);
        this.SetProperties();
        this.AddEventHandlers();
    }

    private void SetProperties()
    {
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mDeleteButton = (Button) findViewById(R.id.btnDelete);
        this.mBudgettTypeCode = (EditText) findViewById(R.id.budgettType);

        Intent intent = getIntent();
        int index = intent.getIntExtra("BudgettTypeIndex", -1);
        this.mBudgettType = BudgettTypeList.GetList().GetBudgettTypeWithIndex(index);
        if (this.mBudgettType == null)
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
        if (this.mBudgettType == null)
            this.finish();
    }

}
