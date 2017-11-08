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
import com.okanerkan.sqlite.model_list.BudgettSourceList;

public class BudgettSourceActivity extends AppCompatActivity
{
    ListView mExpenseListView;
    ListView mIncomeListView;
    Button mNewButton;
    Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_source);
        this.mExpenseListView = (ListView) findViewById(R.id.listViewExpenseBudgettSource);
        this.mIncomeListView = (ListView) findViewById(R.id.listViewIncomeBudgettSource);
        this.mNewButton = (Button) findViewById(R.id.btnNew);
        this.mCancelButton = (Button) findViewById(R.id.btnCancel);
        this.CreateListViews();
        this.AddHandlers();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.CreateListViews();
    }

    private void CreateListViews()
    {
        try
        {
            if (this.mIncomeListView == null || this.mExpenseListView == null)
            {
                return;
            }

            ArrayAdapter<BudgettSource> expenseSourceAdapter = new ArrayAdapter<BudgettSource>(this,
                    android.R.layout.simple_list_item_1,
                    BudgettSourceList.GetList().GetBudgettSourceList(BudgettEntryType.EXPENSE));

            expenseSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.mExpenseListView.setAdapter(expenseSourceAdapter);

            ArrayAdapter<BudgettSource> incomeSourceAdapter = new ArrayAdapter<BudgettSource>(this,
                    android.R.layout.simple_list_item_1,
                    BudgettSourceList.GetList().GetBudgettSourceList(BudgettEntryType.INCOME));

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
        this.mCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
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

        BudgettSource source = (BudgettSource) adapter.getItem(_index);
        if (source == null)
        {
            return;
        }

        this.OpenCard(source.getID());
    }

    private void OpenCard(int _id)
    {
        Intent intent = new Intent(getApplicationContext(), BudgettSourceCardActivity.class);
        intent.putExtra("BudgettSourceID", _id);
        startActivity(intent);
    }
}
