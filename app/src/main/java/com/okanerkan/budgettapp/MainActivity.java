package com.okanerkan.budgettapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.okanerkan.dll.BindingManager;
import com.okanerkan.dll.SideMenuAdapter;
import com.okanerkan.dll.SideMenuItem;
import com.okanerkan.dll.Tuple;
import com.okanerkan.globals.Globals;
import com.okanerkan.interfaces.IObserver;
import com.okanerkan.sqlite.helper.BudgettDatabaseHelper;
import com.okanerkan.sqlite.model.BudgettItem;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model_list.BudgettItemList;
import com.okanerkan.sqlite.model_list.BudgettSourceList;
import com.okanerkan.sqlite.model.BudgettType;
import com.okanerkan.sqlite.model_list.BudgettTypeList;

import java.util.ArrayList;
import java.util.List;

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
    private TextView mMonthlyIncomeText;
    private TextView mMonthlyExpenseText;
    private ActionBarDrawerToggle mDrawerToggle;

    private BudgettItem mBudgettItem;
    private List<SideMenuItem> mMenuItems;
    private BindingManager mBindingManager;
    private static String TAG = "MainActivity";

    //region Override
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.InitializeProperties();
        this.CreateBudgettItem();
        this.CreateBindingManager();
        this.LoadSideMenu();
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
        else if (id == R.id.menuItemBudgettSource)
        {
            Intent intent = new Intent(getApplicationContext(), BudgettSourceActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menuItemBudgettType)
        {
            Intent intent = new Intent(getApplicationContext(), BudgettTypeActivity.class);
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

        if (this.mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
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
        this.mMonthlyIncomeText = (TextView) findViewById(R.id.lblIncomeValue);
        this.mMonthlyExpenseText = (TextView) findViewById(R.id.lblExpenseValue);
        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, R.string.BtnNew, R.string.BtnCancel);
    }

    private void CreateBudgettItem()
    {
        this.mBudgettItem = new BudgettItem();
        this.mBudgettItem.AddObserver(new BudgettItemObserver(this));
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

    private void LoadSideMenu()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TypedArray menuIcons = this.getResources().obtainTypedArray(R.array.SideMenuIconList);

        this.mMenuItems = new ArrayList<SideMenuItem>();
        /*
        this.mMenuItems.add(new SideMenuItem(menuIcons.getResourceId(0, 0), getResources().getString(R.string.MenuItemBudgettSource)));
        this.mMenuItems.add(new SideMenuItem(menuIcons.getResourceId(1, 0), getResources().getString(R.string.MenuItemBudgettType)));
        this.mMenuItems.add(new SideMenuItem(menuIcons.getResourceId(2, 0), getResources().getString(R.string.MenuItemReport)));
        this.mMenuItems.add(new SideMenuItem(menuIcons.getResourceId(0, 0), getResources().getString(R.string.MenuItemFacebook)));
        this.mMenuItems.add(new SideMenuItem(menuIcons.getResourceId(3, 0), getResources().getString(R.string.MenuItemWebsite)));
        */

        this.mSideMenuListView.setAdapter(new SideMenuAdapter(this.mMenuItems, getApplicationContext()));
        this.mDrawerToggle.syncState();
        this.mDrawerLayout.addDrawerListener(this.mDrawerToggle);

        menuIcons.recycle();
    }

    private void LoadSpinners()
    {
        ArrayAdapter<BudgettSource> sourceAdapter = new ArrayAdapter<BudgettSource>(this,
                android.R.layout.simple_spinner_item,
                BudgettSourceList.GetList().GetBudgettSourceList(this.mBudgettItem.getEntryType()));

        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSourceSpinner.setAdapter(sourceAdapter);

        ArrayAdapter<BudgettType> typeAdapter = new ArrayAdapter<BudgettType>(this,
                android.R.layout.simple_spinner_item,
                BudgettTypeList.GetList().GetBudgettTypeList(this.mBudgettItem.getEntryType()));

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
        this.mMonthlyIncomeText.setText(String.format("%.2f", incExp.x));
        this.mMonthlyExpenseText.setText(String.format("%.2f", incExp.y));
    }

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
    public void OnSideMenuItemClicked(int position)
    {
        String title = this.mMenuItems.get(position).getTitle();

        if (title.equals(this.getResources().getString(R.string.MenuItemBudgettSource)))
        {
            Intent intent = new Intent(getApplicationContext(), BudgettSourceActivity.class);
            startActivity(intent);
        }
        else if (title.equals(this.getResources().getString(R.string.MenuItemBudgettType)))
        {
            Intent intent = new Intent(getApplicationContext(), BudgettTypeActivity.class);
            startActivity(intent);
        }
        else if (title.equals(this.getResources().getString(R.string.MenuItemReport)))
        {
        }
        else if (title.equals(this.getResources().getString(R.string.MenuItemFacebook)))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.facebook.com/Budgett-App-657157924468286/"));
            startActivity(intent);
        }
        else if (title.equals(this.getResources().getString(R.string.MenuItemWebsite)))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.okanerkan.com"));
            startActivity(intent);
        }

        this.mDrawerLayout.closeDrawer(this.mSideMenuListView);
    }

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
            Globals.DBHelper.insertBudgettItem(this.mBudgettItem);
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
    public class BudgettItemObserver implements IObserver
    {
        public BudgettItemObserver(MainActivity _parent)
        {
            this.mParent = _parent;
        }

        private MainActivity mParent;

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
