package com.okanerkan.budgettapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.okanerkan.enums.LoginResult;
import com.okanerkan.globals.KNGlobal;
import com.okanerkan.sqlite.model.BudgettAccount;
import com.okanerkan.sqlite.model.BudgettUser;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        KNGlobal.CreateKNGlobal(this);
        this.CheckShouldStayOnActivity();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.CheckShouldStayOnActivity();
    }

    private void CheckShouldStayOnActivity()
    {
        if (KNGlobal.CurrentUser() != null)
        {
            Intent intent = new Intent(getApplicationContext(), BudgettAppActivity.class);
            startActivity(intent);
        }
    }

    public void OnBtnLoginClick(View view)
    {

        String username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();

        if (!username.equals("") && !password.equals(""))
        {
            BudgettUser user = new BudgettUser();
            BudgettAccount account = new BudgettAccount();

            try
            {
                user.setName(username);
                user.setPassword(password);
                LoginResult loginResult = user.Login();

                if (loginResult == LoginResult.LOGIN_SUCCESS || loginResult == LoginResult.NO_USER)
                {
                    if (loginResult == LoginResult.NO_USER)
                    {
                        user.setAccountID(account.getID());
                        user.Save();
                        account.setOwnerID(user.getID());
                        account.setName("Hesap");
                        try
                        {
                            account.Save();
                        }
                        catch (Exception ex)
                        {
                            user.Delete();
                            throw ex;
                        }
                    }
                    SharedPreferences prefs = this.getSharedPreferences("com.okanerkan.budgettapp", MODE_PRIVATE);
                    prefs.edit().putString("Username", user.getName()).apply();
                    prefs.edit().putString("Password", user.getPassword()).apply();
                    this.CheckShouldStayOnActivity();
                }
                else
                {
                    Toast.makeText(this, "Cannot login! Check input values!", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception ex)
            {
                Log.e(TAG, ex.getMessage());
            }
        }
        else
        {
            Toast.makeText(this, "FieldsCannotBeEmpty", Toast.LENGTH_LONG).show();
        }
    }
}
