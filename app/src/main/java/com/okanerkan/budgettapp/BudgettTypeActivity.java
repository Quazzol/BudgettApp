package com.okanerkan.budgettapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BudgettTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_type);
        this.CreateListView();
    }

    private void CreateListView()
    {
        try {
            String[] testNames = {"AS", "CD"};

            ListView listView = (ListView) findViewById(R.id.listViewBudgettType);

            if (listView == null) return;

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                }
            });
            listView.setAdapter(new ArrayAdapter<String>(
                                this,
                                R.layout.editable_listview_item,
                                R.id.txtListViewItemName,
                                testNames));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
