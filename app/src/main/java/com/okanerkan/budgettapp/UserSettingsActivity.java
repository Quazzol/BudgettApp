package com.okanerkan.budgettapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class UserSettingsActivity extends AppCompatActivity
{
    private Button mSaveButton;
    private Button mCancelButton;
    private Spinner mCurrencySpinner;
    private final String[] mCurrenctList = {"$", "€", "£", "¥", "₺"};

    private static String TAG = "SourceCard";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        this.SetProperties();
        this.LoadSpinner();
        this.SetCurrency();
        this.AddEventHandlers();
    }

    private void SetProperties()
    {
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mCancelButton = (Button) findViewById(R.id.btnCancel);
        this.mCurrencySpinner = (Spinner) findViewById(R.id.spnCurrencies);
    }

    private void LoadSpinner()
    {
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                this.mCurrenctList);

        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mCurrencySpinner.setAdapter(currencyAdapter);
    }

    private void SetCurrency()
    {
        SharedPreferences prefs = this.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
        String currencyCode = prefs.getString("CurrencyCode", "");
        if (currencyCode.isEmpty())
        {
            return;
        }

        ArrayAdapter adapter = (ArrayAdapter)this.mCurrencySpinner.getAdapter();
        this.mCurrencySpinner.setSelection(adapter.getPosition(currencyCode));
    }

    private void AddEventHandlers()
    {
        this.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveButtonClicked(view);
            }
        });

        this.mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnCancelButtonClicked(view);
            }
        });
    }

    public void OnSaveButtonClicked(View view)
    {
        try
        {
            String currency = this.mCurrencySpinner.getSelectedItem().toString();
            if (!currency.isEmpty())
            {
                SharedPreferences prefs = this.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
                prefs.edit().putString("CurrencyCode", currency).apply();
                Toast.makeText(this, R.string.MSGSaved, Toast.LENGTH_LONG).show();
            }

            this.finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage());
        }
    }

    public void OnCancelButtonClicked(View view)
    {
        try
        {
            this.finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, R.string.MSGCannotDelete, Toast.LENGTH_SHORT).show();
            Log.e(TAG, getResources().getString(R.string.MSGCannotDelete));
        }
    }
}
