package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.okanerkan.sqlite.model.BudgettTypeList;

public class BudgettTypeActivity extends AppCompatActivity
{
    ListView mListView;
    Button mNewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_type);
        this.mListView = (ListView) findViewById(R.id.listViewBudgettType);
        this.mNewButton = (Button) findViewById(R.id.btnNew);
        this.CreateListView();
        this.AddHandlers();
    }

    private void CreateListView()
    {
        try
        {
            if (this.mListView == null) return;

            this.mListView.setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    BudgettTypeList.GetList().GetBudgettTypeNames()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void AddHandlers()
    {
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenCard(i);
            }
        });
        this.mNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCard(-1);
            }
        });
    }

    private void OpenCard(int _id)
    {
        Intent intent = new Intent(getApplicationContext(), BudgettTypeCardActivity.class);
        intent.putExtra("BudgettTypeIndex", _id);
        startActivity(intent);
    }
}
