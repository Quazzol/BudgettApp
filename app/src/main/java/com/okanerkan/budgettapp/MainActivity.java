package com.okanerkan.budgettapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model.BudgettSourceList;
import com.okanerkan.sqlite.model.BudgettTypeList;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button mSaveButton;
    private Button mResetButton;
    private RadioGroup mEntryTypeRadio;
    private EditText mEntryDateEdit;
    private Spinner mSourceSpinner;
    private Spinner mTypeSpinner;
    private EditText mAmountEdit;
    private BudgettItem mBudgettItem;

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
        this.mEntryTypeRadio = (RadioGroup) findViewById(R.id.rdgEntryType);
        this.mEntryDateEdit = (EditText) findViewById(R.id.txtEntryDate);
        this.mSourceSpinner = (Spinner) findViewById(R.id.spnBudgettSource);
        this.mTypeSpinner = (Spinner) findViewById(R.id.spnExpenseType);
        this.mAmountEdit = (EditText) findViewById(R.id.txtAmount);

        this.CreateBudgettItem();

        this.LoadSpinners();
    }

    private void CreateBudgettItem()
    {
        Calendar currentTime = Calendar.getInstance();

        this.mBudgettItem = new BudgettItem();
        this.mBudgettItem.setEntryType(this.GetEntryType());
        this.SetDateValue(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH) + 1, currentTime.get(Calendar.DAY_OF_MONTH));
    }

    private void LoadSpinners()
    {
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
        this.mEntryDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OnDateEditClicked();
            }
        });
    }

    public void OnResetButtonClicked()
    {

    }

    public void OnSaveButtonClicked()
    {
        try
        {
            this.mBudgettItem.setEntryType(this.GetEntryType());
            this.mBudgettItem.setBudgettType(this.GetBudgettTypeID());
            this.mBudgettItem.setBudgettSource(this.GetBudgettSourceID());
            this.mBudgettItem.setAmount(this.GetAmount());

            Globals.DBHelper.insertBudgettItem(this.mBudgettItem);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error", ex.getMessage());
        }
    }

    private void OnDateEditClicked()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.mBudgettItem.getEntryDate());

        DatePickerDialog datePicker;
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                SetDateValue(year, monthOfYear + 1, dayOfMonth);
            }
        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setTitle(R.string.PickADate);
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, this.getResources().getString(R.string.BtnSet), datePicker);
        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, this.getResources().getString(R.string.BtnCancel), datePicker);
        datePicker.show();
    }

    private void SetDateValue(int year, int month, int day)
    {
        String date =  String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
        this.mEntryDateEdit.setText(date);
        this.mBudgettItem.setEntryDate(year, month, day);
    }

    private BudgettEntryType GetEntryType()
    {
        int radioButtonID = this.mEntryTypeRadio.getCheckedRadioButtonId();
        View radioButton = this.mEntryTypeRadio.findViewById(radioButtonID);
        int id = this.mEntryTypeRadio.indexOfChild(radioButton);
        return BudgettEntryType.values()[id];
    }

    private int GetBudgettTypeID()
    {
        return BudgettTypeList.GetList().GetBudgettListID(this.mTypeSpinner.getSelectedItemPosition());
    }

    private int GetBudgettSourceID()
    {
        return BudgettSourceList.GetList().GetBudgettSourceID(this.mSourceSpinner.getSelectedItemPosition());
    }

    private double GetAmount()
    {
        return Double.parseDouble(this.mAmountEdit.getText().toString());
    }
}
