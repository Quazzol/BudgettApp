package com.okanerkan.budgettapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.okanerkan.dll.BindingManager;
import com.okanerkan.dll.Tuple;
import com.okanerkan.globals.Globals;
import com.okanerkan.globals.KNGlobal;
import com.okanerkan.interfaces.IObserver;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model.BudgettCategory;
import com.okanerkan.sqlite.model_list.BudgettItemList;
import com.okanerkan.sqlite.model_list.BudgettSourceList;
import com.okanerkan.sqlite.model_list.BudgettCategoryList;

import java.util.Locale;

public class BudgettAppActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    //region Members
    private Button mSaveButton;
    private Button mResetButton;
    private RadioGroup mEntryTypeRadio;
    private EditText mEntryDateEdit;
    private Spinner mSourceSpinner;
    private Spinner mTypeSpinner;
    private EditText mAmountEdit;
    private TextView mMonthlyIncomeText;
    private TextView mMonthlyExpenseText;

    private BudgettItem mBudgettItem;
    private BindingManager mBindingManager;
    private String mUserCurrencyCode = "$";
    private static String TAG = "BudgettAppActivity";
    //endregion

    //region Override
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_app);

        this.InitializeProperties();
        this.CreateBudgettItem();
        this.CreateBindingManager();
        this.CreateSideMenu();
        this.LoadSpinners();
        this.AddHandlers();
        this.UpdateMonthlyStatements();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.OnResetButtonClicked();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.budgett_app, menu);
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
            return true;
        }
        else if (id == R.id.menuItemLogout)
        {
            KNGlobal.Reset();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.menuItemBudgettSource)
        {
            Intent intent = new Intent(getApplicationContext(), BudgettSourceActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menuItemBudgettType)
        {
            Intent intent = new Intent(getApplicationContext(), BudgettCategoryActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menuItemReport)
        {
            Intent intent = new Intent(getApplicationContext(), BudgettReportActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menuItemFacebook)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.facebook.com/Budgett-App-657157924468286/"));
            startActivity(intent);
        }
        else if (id == R.id.menuItemWebsite)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.okanerkan.com"));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion

    //region Initialize Methods
    private void InitializeProperties()
    {
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mResetButton = (Button) findViewById(R.id.btnReset);
        this.mEntryTypeRadio = (RadioGroup) findViewById(R.id.rdgEntryType);
        this.mEntryDateEdit = (EditText) findViewById(R.id.txtEntryDate);
        this.mSourceSpinner = (Spinner) findViewById(R.id.spnBudgettSource);
        this.mTypeSpinner = (Spinner) findViewById(R.id.spnExpenseType);
        this.mAmountEdit = (EditText) findViewById(R.id.txtAmount);
        this.mMonthlyIncomeText = (TextView) findViewById(R.id.lblIncomeValue);
        this.mMonthlyExpenseText = (TextView) findViewById(R.id.lblExpenseValue);

        SharedPreferences prefs = this.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
        String currencyCode = prefs.getString("CurrencyCode", "");
        this.mUserCurrencyCode = currencyCode.isEmpty() ? "$" : currencyCode;
    }

    private void CreateBudgettItem()
    {
        this.mBudgettItem = new BudgettItem();
        this.mBudgettItem.AddObserver(new BudgettAppActivity.BudgettItemObserver(this));
    }

    private void CreateBindingManager()
    {
        try
        {
            this.mBindingManager = new BindingManager(this.mBudgettItem);
            this.mBindingManager.Add(this.mEntryTypeRadio);
            this.mBindingManager.Add(this.mEntryDateEdit);
            this.mBindingManager.Add(this.mSourceSpinner);
            this.mBindingManager.Add(this.mTypeSpinner);
            this.mBindingManager.Add(this.mAmountEdit);
            this.mBindingManager.Initialize();
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void CreateSideMenu()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.SideMenuOpen, R.string.SideMenuClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navViewSideMenu);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void LoadSpinners()
    {
        ArrayAdapter<BudgettSource> sourceAdapter = new ArrayAdapter<BudgettSource>(this,
                android.R.layout.simple_spinner_item,
                BudgettSourceList.GetList().GetBudgettSourceList(this.mBudgettItem.getEntryType()));

        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSourceSpinner.setAdapter(sourceAdapter);

        ArrayAdapter<BudgettCategory> typeAdapter = new ArrayAdapter<BudgettCategory>(this,
                android.R.layout.simple_spinner_item,
                BudgettCategoryList.GetList().GetBudgettCategoryList(this.mBudgettItem.getEntryType()));

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

    private void UpdateMonthlyStatements()
    {
        Tuple<Double, Double> incExp = new BudgettItemList().GetMonthlyStatements();
        this.mMonthlyIncomeText.setText(String.format(Locale.getDefault(), "%.2f %s", incExp.x, this.mUserCurrencyCode));
        this.mMonthlyExpenseText.setText(String.format(Locale.getDefault(),"%.2f %s", incExp.y, this.mUserCurrencyCode));
    }

    //endregion

    //region Methods

    private void HideSoftKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            if (imm != null)
            {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    //endregion

    //region EventHandlers
    public void OnResetButtonClicked()
    {
        try
        {
            this.CreateBudgettItem();
            this.mBindingManager.Rebind(this.mBudgettItem);
            this.mBindingManager.BindValues();
            this.LoadSpinners();
            this.mResetButton.requestFocus();
            this.HideSoftKeyboard();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage());
        }
    }

    public void OnSaveButtonClicked()
    {
        try
        {
            KNGlobal.DBHelper().insertBudgettItem(this.mBudgettItem);
            Toast.makeText(this, R.string.MSGSaved, Toast.LENGTH_LONG).show();
            this.UpdateMonthlyStatements();
            this.OnResetButtonClicked();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage());
        }
    }
    //endregion

    //region BudgettItemObserver
    private class BudgettItemObserver implements IObserver
    {
        public BudgettItemObserver(BudgettAppActivity _parent)
        {
            this.mParent = _parent;
        }

        private BudgettAppActivity mParent;

        @Override
        public void Update(String propertyName)
        {
            if (propertyName.equalsIgnoreCase("EntryType"))
            {
                this.mParent.LoadSpinners();
            }
        }
    }
    //endregion
}
