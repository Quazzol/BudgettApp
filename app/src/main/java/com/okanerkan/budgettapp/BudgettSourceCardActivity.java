package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model.BudgettSourceList;

public class BudgettSourceCardActivity extends AppCompatActivity
{
    Button mSaveButton;
    Button mDeleteButton;
    RadioGroup mEntryTypeRadio;
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
        this.mEntryTypeRadio = (RadioGroup) findViewById(R.id.rdgEntryType);
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mDeleteButton = (Button) findViewById(R.id.btnDelete);
        this.mBudgettSourceCode = (EditText) findViewById(R.id.budgettSource);

        Intent intent = getIntent();
        int index = intent.getIntExtra("BudgettSourceIndex", -1);
        this.mBudgettSource = BudgettSourceList.GetList().GetBudgettSourceWithIndex(index);
        if (this.mBudgettSource == null)
            this.mDeleteButton.setText(R.string.BtnCancel);
        else
        {
            RadioButton radioButton = this.mEntryTypeRadio.findViewById(this.mBudgettSource.getEntryType().getValue());
            radioButton.setChecked(true);
            this.mBudgettSourceCode.setText(this.mBudgettSource.getSourceCode());
        }
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

    private BudgettEntryType GetEntryType()
    {
        int radioButtonID = this.mEntryTypeRadio.getCheckedRadioButtonId();
        View radioButton = this.mEntryTypeRadio.findViewById(radioButtonID);
        int id = this.mEntryTypeRadio.indexOfChild(radioButton);
        return BudgettEntryType.values()[id];
    }

    public void OnSaveButtonClicked(View view)
    {
        try
        {
            String sourceCode = this.mBudgettSourceCode.getText().toString();

            if (this.mBudgettSource == null)
            {
                this.mBudgettSource = new BudgettSource(-1, this.GetEntryType(), sourceCode);
                Globals.DBHelper.insertBudgettSource(this.mBudgettSource);
            }
            else
            {
                this.mBudgettSource.setEntryType(this.GetEntryType());
                this.mBudgettSource.setSourceCode(sourceCode);
                Globals.DBHelper.updateBudgettSource(this.mBudgettSource);
            }

            this.finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error", ex.getMessage());
        }
    }

    public void OnDeleteButtonClicked(View view)
    {
        if (this.mBudgettSource != null)
            Globals.DBHelper.deleteBudgettSource(this.mBudgettSource);
        this.finish();
    }
}
