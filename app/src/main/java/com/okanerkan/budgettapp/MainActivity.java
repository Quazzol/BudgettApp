package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettSourceList;
import com.okanerkan.sqlite.model.BudgettTypeList;

public class MainActivity extends AppCompatActivity {

    private Button mSaveButton;
    private Button mResetButton;
    private Spinner mSourceSpinner;
    private Spinner mTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.InitializeProperties();
        this.AddHandlers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menuItemBudgettSource)
        {
            Intent intent = new Intent(getApplicationContext(), BudgettSourceActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.menuItemBudgettType)
        {
            Intent intent = new Intent(getApplicationContext(), BudgettTypeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void InitializeProperties()
    {
        Globals.DBHelper = new BudgettDatabaseHelper(getApplicationContext());
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mResetButton = (Button) findViewById(R.id.btnReset);
        this.mSourceSpinner = (Spinner) findViewById(R.id.spnBudgettSource);
        this.mTypeSpinner = (Spinner) findViewById(R.id.spnExpenseType);

        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<String>(this,
                                                                android.R.layout.simple_spinner_item,
                                                                BudgettSourceList.GetList().GetBudgettSourceNames());

        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSourceSpinner.setAdapter(sourceAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                BudgettTypeList.GetList().GetBudgettTypeNames());

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mTypeSpinner.setAdapter(typeAdapter);
    }

    private void AddHandlers()
    {
        this.mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnResetButtonClicked();
            }
        });
        this.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveButtonClicked();
            }
        });
    }

    public void OnResetButtonClicked()
    {

    }

    public void OnSaveButtonClicked()
    {

    }
}
