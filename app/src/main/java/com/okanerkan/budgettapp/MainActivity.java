package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("TESTTT");
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
}
