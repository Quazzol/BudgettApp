package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model.BudgettType;
import com.okanerkan.sqlite.model_list.BudgettSourceList;
import com.okanerkan.sqlite.model_list.BudgettTypeList;

public class BudgettTypeActivity extends AppCompatActivity
{
    ListView mExpenseListView;
    ListView mIncomeListView;
    Button mNewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_type);
        this.mExpenseListView = (ListView) findViewById(R.id.listViewExpenseBudgettType);
        this.mIncomeListView = (ListView) findViewById(R.id.listViewIncomeBudgettType);
        this.mNewButton = (Button) findViewById(R.id.btnNew);
        this.CreateListView();
        this.AddHandlers();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.CreateListView();
    }

    private void CreateListView()
    {
        try
        {
            if (this.mIncomeListView == null || this.mExpenseListView == null)
            {
                return;
            }

            ArrayAdapter<BudgettType> expenseSourceAdapter = new ArrayAdapter<BudgettType>(this,
                    android.R.layout.simple_list_item_1,
                    BudgettTypeList.GetList().GetBudgettTypeList(BudgettEntryType.EXPENSE));

            expenseSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.mExpenseListView.setAdapter(expenseSourceAdapter);

            ArrayAdapter<BudgettType> incomeSourceAdapter = new ArrayAdapter<BudgettType>(this,
                    android.R.layout.simple_list_item_1,
                    BudgettTypeList.GetList().GetBudgettTypeList(BudgettEntryType.INCOME));

            incomeSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.mIncomeListView.setAdapter(incomeSourceAdapter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void AddHandlers()
    {
        this.mExpenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenCard((ListView) adapterView, i);
            }
        });
        this.mIncomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenCard((ListView) adapterView, i);
            }
        });
        this.mNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCard(-1);
            }
        });
    }

    private void OpenCard(ListView _view, int _index)
    {
        if (_view == null)
        {
            return;
        }

        ArrayAdapter adapter = (ArrayAdapter) _view.getAdapter();
        if (adapter == null)
        {
            return;
        }

        BudgettType type = (BudgettType) adapter.getItem(_index);
        if (type == null)
        {
            return;
        }

        this.OpenCard(type.getID());
    }

    private void OpenCard(int _id)
    {
        Intent intent = new Intent(getApplicationContext(), BudgettTypeCardActivity.class);
        intent.putExtra("BudgettTypeID", _id);
        startActivity(intent);
    }
}
