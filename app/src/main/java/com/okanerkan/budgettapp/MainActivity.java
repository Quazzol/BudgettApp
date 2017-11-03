package com.okanerkan.budgettapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.okanerkan.dll.SideMenuAdapter;
import com.okanerkan.dll.SideMenuItem;
import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model.BudgettSourceList;
import com.okanerkan.sqlite.model.BudgettTypeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button mSaveButton;
    private Button mResetButton;
    private RadioGroup mEntryTypeRadio;
    private EditText mEntryDateEdit;
    private Spinner mSourceSpinner;
    private Spinner mTypeSpinner;
    private EditText mAmountEdit;
    private ListView mSideMenuListView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private BudgettItem mBudgettItem;
    private List<SideMenuItem> mMenuItems;

    //region Override
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
        this.getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.menuItemSettings)
        {
            Intent intent = new Intent(getApplicationContext(), UserSettingsActivity.class);
            startActivity(intent);
        }

        if (this.mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);

        /*
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
        */
    }
    //endregion

    //region Initialize Methods
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
        this.mSideMenuListView = (ListView) findViewById(R.id.listViewSideMenu);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.MainLayout);
        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, R.string.BtnNew, R.string.BtnCancel);

        this.CreateBudgettItem();
        this.LoadSideMenu();
        this.LoadSpinners();
    }

    private void CreateBudgettItem()
    {
        Calendar currentTime = Calendar.getInstance();

        this.mBudgettItem = new BudgettItem();
        this.mBudgettItem.setEntryType(this.GetEntryType());
        this.SetDateValue(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH) + 1, currentTime.get(Calendar.DAY_OF_MONTH));
    }

    private void LoadSideMenu()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String menuTitles[] = this.getResources().getStringArray(R.array.SideMenuItemList);
        TypedArray menuIcons = this.getResources().obtainTypedArray(R.array.SideMenuIconList);

        this.mMenuItems = new ArrayList<SideMenuItem>();

        for (int i = 0; i < menuTitles.length; i++)
        {
            this.mMenuItems.add(new SideMenuItem(menuIcons.getResourceId(i, 0), menuTitles[i]));
        }

        this.mSideMenuListView.setAdapter(new SideMenuAdapter(this.mMenuItems, getApplicationContext()));
        this.mDrawerToggle.syncState();
        this.mDrawerLayout.addDrawerListener(this.mDrawerToggle);

        menuIcons.recycle();
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
        this.mSideMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                OnSideMenuItemClicked();
            }
        });
    }
    //endregion

    //region EventHandlers
    public void OnSideMenuItemClicked()
    {
        this.mDrawerLayout.closeDrawer(this.mSideMenuListView);
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
        String amount = this.mAmountEdit.getText().toString();
        return amount.equalsIgnoreCase("") ? 0 : Double.parseDouble(amount);
    }

    //endregion
}
