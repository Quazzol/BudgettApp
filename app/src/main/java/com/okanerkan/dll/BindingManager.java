package com.okanerkan.dll;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.okanerkan.exceptions.ViewTypeNotFoundException;
import com.okanerkan.interfaces.IObservable;
import com.okanerkan.interfaces.IObserver;
import com.okanerkan.interfaces.ISpinnerSource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quazzol on 4.11.2017.
 */

public class BindingManager implements IObserver
{
    public BindingManager(IObservable observable)
    {
        this.mObservable = observable;
        this.mViewList = new HashMap<View, String>();
    }

    private static String TAG = "BindingManager";
    private IObservable mObservable;
    private HashMap<View, String> mViewList;

    public void Add(View view) throws Exception
    {
        this.Add(view, view.getTag().toString());
    }

    public void Add(View view, String propertyName) throws Exception
    {
        Class className = view.getClass();
        if (className == EditText.class)
        {
            ((EditText) view).addTextChangedListener(new BindingManagerTextWatcher(this, (EditText) view));
        }
        else if (className == NumberPicker.class)
        {
            ((NumberPicker) view).setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
            {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1)
                {
                    OnNumberPickerValueChanged(numberPicker);
                }
            });
        }
        else if (className == Spinner.class)
        {
            ((Spinner) view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    OnSpinnerItemChanged((Spinner)view);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {

                }
            });
        }
        else if (className == RadioGroup.class)
        {
            ((RadioGroup) view).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedID)
                {
                    OnRadioGroupCheckChanged(radioGroup, checkedID);
                }
            });
        }
        else
        {
            throw new ViewTypeNotFoundException(className);
        }

        this.mViewList.put(view, propertyName);
    }

    public void Remove(View view)
    {
        this.mViewList.remove(view);
    }

    @Override
    public void Update(String propertyName)
    {
        try
        {
            View view = null;
            for (Map.Entry<View, String> kvp : this.mViewList.entrySet())
            {
                if (kvp.getValue().equalsIgnoreCase(propertyName))
                {
                    view = kvp.getKey();
                    break;
                }
            }

            if (view == null)
                return;

            Field field = this.mObservable.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            Object propertyValue = field.get(this.mObservable);
            this.SetValueOnView(view, propertyValue);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void SetValueOnView(View _view, Object _value) throws Exception
    {
        Class className = _view.getClass();
        if (className == EditText.class)
        {
            ((EditText) _view).setText(_value.toString());
        }
        else if (className == NumberPicker.class)
        {
            ((NumberPicker) _view).setValue((int) _value);
        }
        else if (className == Spinner.class)
        {

        }
        else if (className == RadioGroup.class)
        {

        }
        else
        {
            throw new ViewTypeNotFoundException(className);
        }
    }

    private void OnEditViewTextChanged(EditText _view)
    {
        try
        {
            String propertyName = this.mViewList.get(_view);
            Field field = this.mObservable.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            Editable viewValue = _view.getText();
            if (viewValue == null)
            {
                return;
            }

            field.set(this.mObservable, viewValue.toString());
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void OnNumberPickerValueChanged(NumberPicker _view)
    {
        try
        {
            String propertyName = this.mViewList.get(_view);
            Field field = this.mObservable.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(this.mObservable, _view.getValue());
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void OnSpinnerItemChanged(Spinner _view)
    {
        try
        {
            String propertyName = this.mViewList.get(_view);
            Field field = this.mObservable.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            Object selectedItem = _view.getSelectedItem();
            if (selectedItem == null)
            {
                return;
            }
            field.set(this.mObservable, ((ISpinnerSource) selectedItem).GetID());
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void OnRadioGroupCheckChanged(RadioGroup _view, int _checkedID)
    {
        try
        {
            int count = _view.getChildCount();
            ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
            for (int i=0;i<count;i++) {
                View o = radioGroup.getChildAt(i);
                if (o instanceof RadioButton) {
                    listOfRadioButtons.add((RadioButton)o);
                }
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    class BindingManagerTextWatcher implements TextWatcher
    {
        public BindingManagerTextWatcher(BindingManager _manager, EditText _view)
        {
            this.mManager = _manager;
            this.mEditText = _view;
        }

        private EditText mEditText;
        public EditText getEditText() { return this.mEditText; }

        private BindingManager mManager;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            this.mManager.OnEditViewTextChanged(this.mEditText);
        }

        @Override
        public void afterTextChanged(Editable editable)
        { }
    }
}
