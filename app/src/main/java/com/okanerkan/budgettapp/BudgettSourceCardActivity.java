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
import com.okanerkan.globals.ExceptionHandler;
import com.okanerkan.globals.Globals;
import com.okanerkan.globals.KNGlobal;
import com.okanerkan.globals.Message;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model_list.BudgettSourceList;

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
        this.CreateBudgettSource();
        this.LoadBudgettSource();
        this.SetCancelButtonText();
        this.CreateBindingManager();
        this.AddEventHandlers();
    }

    private void SetProperties()
    {
        this.mEntryTypeRadio = (RadioGroup) findViewById(R.id.rdgEntryType);
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mDeleteButton = (Button) findViewById(R.id.btnDelete);
        this.mBudgettSourceCode = (EditText) findViewById(R.id.budgettSource);
    }

    private void CreateBudgettSource()
    {
        this.mBudgettSource = new BudgettSource();
    }

    private void LoadBudgettSource()
    {
        try
        {
            Intent intent = getIntent();
            String id = intent.getStringExtra("BudgettSourceID");
            this.mBudgettSource.setID(id);
            this.mBudgettSource.Load();
        }
        catch (Exception ex)
        {
            ExceptionHandler.HandleException(TAG, ex);
        }
    }

    private void CreateBindingManager()
    {
        try
        {
            this.mBindingManager = new BindingManager(this.mBudgettSource);
            this.mBindingManager.Add(this.mEntryTypeRadio);
            this.mBindingManager.Add(this.mBudgettSourceCode);
            this.mBindingManager.Initialize();
        }
        catch (Exception ex)
        {
            ExceptionHandler.HandleException(TAG, ex);
        }
    }

    private void SetCancelButtonText()
    {
        this.mDeleteButton.setText(this.mBudgettSource.ExistInDB() ? R.string.Delete : R.string.Cancel);
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
            this.mBudgettSource.Save();
            this.CreateBudgettSource();
            this.SetCancelButtonText();
            this.mBindingManager.Rebind(this.mBudgettSource);
            this.mBindingManager.BindValues();
            Message.Show(R.string.MSGSaved);
            Toast.makeText(this, R.string.MSGSaved, Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ExceptionHandler.HandleException(TAG, ex);
        }
    }

    public void OnDeleteButtonClicked(View view)
    {
        try
        {
            this.mBudgettSource.Delete();
        }
        catch (Exception ex)
        {
            ExceptionHandler.HandleException(TAG, ex);
        }
        finally
        {
            this.finish();
        }
    }
}
