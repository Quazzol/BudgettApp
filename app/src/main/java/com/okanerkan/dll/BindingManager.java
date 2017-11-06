package com.okanerkan.dll;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.okanerkan.exceptions.ViewTypeNotFoundException;
import com.okanerkan.interfaces.IObservable;
import com.okanerkan.interfaces.IObserver;
import com.okanerkan.interfaces.IRadioGroupSource;
import com.okanerkan.interfaces.ISpinnerSource;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quazzol on 4.11.2017.
 */

public class BindingManager implements IObserver
{
    public BindingManager(IObservable observable)
    {
        if (observable == null)
        {
            throw new NullPointerException("Observable cannot be null!");
        }

        this.mObservable = observable;
        this.mViewList = new HashMap<View, String>();
    }

    private static String TAG = "BindingManager";
    private IObservable mObservable;
    private HashMap<View, String> mViewList;

    public void Add(View _view) throws Exception
    {
        this.Add(_view, _view.getTag().toString());
    }

    public void Add(View _view, String _propertyName) throws Exception
    {
        if (_view instanceof EditText)
        {
            ((EditText) _view).addTextChangedListener(new BindingManagerTextWatcher(this, (EditText) _view));
        }
        else if (_view instanceof  NumberPicker)
        {
            ((NumberPicker) _view).setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
            {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1)
                {
                    OnNumberPickerValueChanged(numberPicker);
                }
            });
        }
        else if (_view instanceof  Spinner)
        {
            ((Spinner) _view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        else if (_view instanceof RadioGroup)
        {
            ((RadioGroup) _view).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
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
            throw new ViewTypeNotFoundException(_view.getClass());
        }

        this.mViewList.put(_view, _propertyName);
    }

    public void Remove(View _view)
    {
        this.mViewList.remove(_view);
    }

    @Override
    public void Update(String _propertyName)
    {
        try
        {
            View view = null;
            for (Map.Entry<View, String> kvp : this.mViewList.entrySet())
            {
                if (kvp.getValue().equalsIgnoreCase(_propertyName))
                {
                    view = kvp.getKey();
                    break;
                }
            }

            Method method = this.GetterMethod(_propertyName);

            if (view == null || method == null)
            {
                throw new NoSuchMethodException();
            }
            Object propertyValue = method.invoke(this.mObservable);
            this.SetValueOnView(view, propertyValue);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void SetValueOnView(View _view, Object _value) throws Exception
    {
        if (_view instanceof DateEditText)
        {
            // TODO implement
            ((DateEditText) _view).setText("");
        }
        else if (_view instanceof EditText)
        {
            ((EditText) _view).setText(_value.toString());
        }
        else if (_view instanceof NumberPicker)
        {
            ((NumberPicker) _view).setValue((int) _value);
        }
        else if (_view instanceof Spinner)
        {
            Spinner spinner = (Spinner) _view;
            spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition(_value));
        }
        else if (_view instanceof RadioGroup)
        {
            RadioGroup rdg = (RadioGroup) _view;
            int count = rdg.getChildCount();
            for (int i = 0; i < count; i++)
            {
                View child = rdg.getChildAt(i);
                if (child instanceof RadioButton)
                {
                    RadioButton rbtn = (RadioButton) child;
                    rbtn.setChecked(i == ((IRadioGroupSource) _value).getValue());
                }
            }
        }
        else
        {
            throw new ViewTypeNotFoundException(_view.getClass());
        }
    }

    private void OnEditViewTextChanged(EditText _view)
    {
        try
        {
            String propertyName = this.mViewList.get(_view);
            Method method = this.SetterMethod(propertyName);
            Editable viewValue = _view.getText();

            if (viewValue == null || method == null)
            {
                throw new NoSuchMethodException();
            }
            method.invoke(this.mObservable, viewValue.toString());
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
            Method method = this.SetterMethod(propertyName);

            if(method == null)
            {
                throw new NoSuchMethodException();
            }
            method.invoke(this.mObservable, _view.getValue());
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
            Method method = this.SetterMethod(propertyName);
            Object selectedItem = _view.getSelectedItem();

            if (selectedItem == null || method == null)
            {
                throw new NoSuchMethodException();
            }
            method.invoke(this.mObservable, ((ISpinnerSource) selectedItem).GetID());
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
            int selectedIndex = _view.indexOfChild(_view.findViewById(_checkedID));
            String propertyName = this.mViewList.get(_view);
            Method method = this.SetterMethod(propertyName);

            if (method == null)
            {
                throw new NoSuchMethodException();
            }

            method.invoke(this.mObservable, selectedIndex);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Nullable
    private Method GetterMethod(String _propertyName)
    {
        for(Method m : this.mObservable.getClass().getMethods())
        {
            if (m.getName().contains(_propertyName) && m.getParameterTypes().length == 0)
            {
                return m;
            }
        }

        return null;
    }

    @Nullable
    private Method SetterMethod(String _propertyName)
    {
        for(Method m : this.mObservable.getClass().getMethods())
        {
            if (m.getName().contains(_propertyName) && m.getParameterTypes().length == 1)
            {
                return m;
            }
        }

        return null;
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
