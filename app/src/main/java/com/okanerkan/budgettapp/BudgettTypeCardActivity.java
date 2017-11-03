package com.okanerkan.budgettapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.okanerkan.globals.Globals;
import com.okanerkan.sqlite.model.BudgettEntryType;
import com.okanerkan.sqlite.model.BudgettSource;
import com.okanerkan.sqlite.model.BudgettType;
import com.okanerkan.sqlite.model.BudgettTypeList;

public class BudgettTypeCardActivity extends AppCompatActivity {

    Button mSaveButton;
    Button mDeleteButton;
    RadioGroup mEntryTypeRadio;
    EditText mBudgettTypeCode;
    BudgettType mBudgettType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgett_type_card);
        this.SetProperties();
        this.AddEventHandlers();
    }

    private void SetProperties()
    {
        this.mEntryTypeRadio = (RadioGroup) findViewById(R.id.rdgEntryType);
        this.mSaveButton = (Button) findViewById(R.id.btnSave);
        this.mDeleteButton = (Button) findViewById(R.id.btnDelete);
        this.mBudgettTypeCode = (EditText) findViewById(R.id.budgettType);

        Intent intent = getIntent();
        int index = intent.getIntExtra("BudgettTypeIndex", -1);
        this.mBudgettType = BudgettTypeList.GetList().GetBudgettTypeWithIndex(index);
        if (this.mBudgettType == null)
            this.mDeleteButton.setText(R.string.BtnCancel);
        else
        {
            RadioButton radioButton = this.mEntryTypeRadio.findViewById(this.mBudgettType.getEntryType().getValue());
            radioButton.setChecked(true);
            this.mBudgettTypeCode.setText(this.mBudgettType.getTypeCode());
        }
    }

    private void AddEventHandlers()
    {
        this.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnSaveButtonClicked(view);
            }
        });

        this.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnDeleteButtonClicked(view);
            }
        });
    }

    private BudgettEntryType GetEntryType()
    {
        int radioButtonID = this.mEntryTypeRadio.getCheckedRadioButtonId();
        View radioButton = this.mEntryTypeRadio.findViewById(radioButtonID);
        int id = this.mEntryTypeRadio.indexOfChild(radioButton);
        return BudgettEntryType.values()[id];
    }

    public void OnSaveButtonClicked(View view)
    {
        try
        {
            String typeCode = this.mBudgettTypeCode.getText().toString();

            if (this.mBudgettType == null)
            {
                this.mBudgettType = new BudgettType(-1, this.GetEntryType(), typeCode);
                Globals.DBHelper.insertBudgettType(this.mBudgettType);
            }
            else
            {
                this.mBudgettType.setEntryType(this.GetEntryType());
                this.mBudgettType.setTypeCode(typeCode);
                Globals.DBHelper.updateBudgettType(this.mBudgettType);
            }

            this.finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error", ex.getMessage());
        }
    }

    public void OnDeleteButtonClicked(View view)
    {
        if (this.mBudgettType != null)
            Globals.DBHelper.deleteBudgettType(this.mBudgettType);
        this.finish();
    }

}
