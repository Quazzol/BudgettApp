package com.okanerkan.budgettapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = this.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
        String username = prefs.getString("Username", "");

        if (!username.equals(""))
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void OnBtnLoginClick(View view)
    {
        String username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();

        if (!username.equals("") && !password.equals(""))
        {
            SharedPreferences prefs = this.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
            prefs.edit().putString("Username", username).apply();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
