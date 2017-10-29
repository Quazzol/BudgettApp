package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.okanerkan.sqlite.model.BudgettSourceList;

public class BudgettSourceActivity extends AppCompatActivity
{
    ListView mListView;
    Button mNewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_source);
        this.mListView = (ListView) findViewById(R.id.listViewBudgettSource);
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
                    BudgettSourceList.GetList().GetBudgettSourceNames()));
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
        Intent intent = new Intent(getApplicationContext(), BudgettSourceCardActivity.class);
        intent.putExtra("BudgettSourceIndex", _id);
        startActivity(intent);
    }
}
