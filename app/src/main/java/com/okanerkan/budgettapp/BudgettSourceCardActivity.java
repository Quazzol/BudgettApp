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

import com.okanerkan.dll.BindingManager;
import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model.BudgettSourceList;

public class BudgettSourceCardActivity extends AppCompatActivity
{
    private Button mSaveButton;
    private Button mDeleteButton;
    private RadioGroup mEntryTypeRadio;
    private EditText mBudgettSourceCode;
    private BudgettSource mBudgettSource;
    private BindingManager mBindingManager;

    private static String TAG = "SourceCard";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_source_card);
        this.SetProperties();
        this.CreateBindingManager();
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
        {
            this.mDeleteButton.setText(R.string.BtnCancel);
            this.mBudgettSource = new BudgettSource();
        }
    }

    private void CreateBindingManager()
    {
        try
        {
            this.mBindingManager = new BindingManager(this.mBudgettSource);
            this.mBindingManager.Add(this.mEntryTypeRadio);
            this.mBindingManager.Add(this.mBudgettSourceCode);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
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

    public void OnSaveButtonClicked(View view)
    {
        try
        {
            if (this.mBudgettSource.ExistInDB())
            {
                Globals.DBHelper.updateBudgettSource(this.mBudgettSource);
            }
            else
            {
                Globals.DBHelper.insertBudgettSource(this.mBudgettSource);
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
