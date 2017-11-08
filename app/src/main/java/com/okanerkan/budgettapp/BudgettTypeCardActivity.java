package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.okanerkan.dll.BindingManager;
import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model.BudgettType;
import com.okanerkan.sqlite.model_list.BudgettSourceList;
import com.okanerkan.sqlite.model_list.BudgettTypeList;

public class BudgettTypeCardActivity extends AppCompatActivity {

    private Button mSaveButton;
    private Button mDeleteButton;
    private RadioGroup mEntryTypeRadio;
    private EditText mBudgettTypeCode;
    private BudgettType mBudgettType;
    private BindingManager mBindingManager;

    private static String TAG = "TypeCard";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_type_card);
        this.SetProperties();
        this.CreateBudgetType();
        this.CreateBindingManager();
        this.AddEventHandlers();
    }

    private void SetProperties()
    {
        this.mEntryTypeRadio = (RadioGroup) findViewById(R.id.rdgEntryType);
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mDeleteButton = (Button) findViewById(R.id.btnDelete);
        this.mBudgettTypeCode = (EditText) findViewById(R.id.budgettType);
    }

    private void CreateBudgetType()
    {
        Intent intent = getIntent();
        int id = intent.getIntExtra("BudgettTypeID", -1);
        this.mBudgettType = BudgettTypeList.GetList().GetBudgettType(id);
        if (this.mBudgettType == null)
        {
            this.mDeleteButton.setText(R.string.BtnCancel);
            this.mBudgettType = new BudgettType();
        }
    }

    private void CreateBindingManager()
    {
        try
        {
            this.mBindingManager = new BindingManager(this.mBudgettType);
            this.mBindingManager.Add(this.mEntryTypeRadio);
            this.mBindingManager.Add(this.mBudgettTypeCode);
            this.mBindingManager.Initialize();
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
            if (this.mBudgettType.ExistInDB())
            {
                Globals.DBHelper.updateBudgettType(this.mBudgettType);
                this.finish();
                return;
            }
            Globals.DBHelper.insertBudgettType(this.mBudgettType);

            this.CreateBudgetType();
            Toast.makeText(this, R.string.MSGSaved, Toast.LENGTH_LONG).show();
            this.mBindingManager.Rebind(this.mBudgettType);
            this.mBindingManager.BindValues();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage());
        }
    }

    public void OnDeleteButtonClicked(View view)
    {
        try
        {
            if (this.mBudgettType != null)
                Globals.DBHelper.deleteBudgettType(this.mBudgettType);
            this.finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, R.string.MSGCannotDelete, Toast.LENGTH_SHORT).show();
            Log.e(TAG, getResources().getString(R.string.MSGCannotDelete));
        }
    }

}
