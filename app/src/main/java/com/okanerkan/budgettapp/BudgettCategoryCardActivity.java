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
import com.okanerkan.sqlite.model.BudgettCategory;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;

public class BudgettCategoryCardActivity extends AppCompatActivity {

    private Button mSaveButton;
    private Button mDeleteButton;
    private RadioGroup mEntryTypeRadio;
    private EditText mBudgettTypeCode;
    private BudgettCategory mBugettCategory;
    private BindingManager mBindingManager;

    private static String TAG = "CategoryCard";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_category_card);
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
        int id = intent.getIntExtra("BudgettCategoryID", -1);
        this.mBugettCategory = BudgettCategoryList.GetList().GetBudgettCategory(id);
        if (this.mBugettCategory == null)
        {
            this.mDeleteButton.setText(R.string.BtnCancel);
            this.mBugettCategory = new BudgettCategory();
        }
    }

    private void CreateBindingManager()
    {
        try
        {
            this.mBindingManager = new BindingManager(this.mBugettCategory);
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
            if (this.mBugettCategory.ExistInDB())
            {
                Globals.DBHelper.updateBudgettType(this.mBugettCategory);
                this.finish();
                return;
            }
            Globals.DBHelper.insertBudgettType(this.mBugettCategory);

            this.CreateBudgetType();
            Toast.makeText(this, R.string.MSGSaved, Toast.LENGTH_LONG).show();
            this.mBindingManager.Rebind(this.mBugettCategory);
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
            if (this.mBugettCategory != null)
                Globals.DBHelper.deleteBudgettType(this.mBugettCategory);
            this.finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, R.string.MSGCannotDelete, Toast.LENGTH_SHORT).show();
            Log.e(TAG, getResources().getString(R.string.MSGCannotDelete));
        }
    }

}
