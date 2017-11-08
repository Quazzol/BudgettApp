package com.okanerkan.dll;

import android.util.Log;

import com.okanerkan.interfaces.IObservable;
import com.okanerkan.interfaces.IObserver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Quazzol on 4.11.2017.
 */

public class ObservableBase implements IObservable
{
    public ObservableBase()
    {
        this.mObserverList = new ArrayList<>();
        this.mChangedProperties = new ArrayList<>();
    }

    private static String TAG = "ObservableBase";

    protected ArrayList<IObserver> mObserverList;
    protected ArrayList<String> mChangedProperties;

    @Override
    public void AddObserver(IObserver observer)
    {
        this.mObserverList.add(observer);
    }

    @Override
    public void RemoveObserver(IObserver observer)
    {
        this.mObserverList.remove(observer);
    }

    @Override
    public void NotifyObservers(String propertyName)
    {
        for (IObserver observer: this.mObserverList)
        {
            observer.Update(propertyName);
        }
    }

    protected boolean SetValue(String propertyName, Object value)
    {
        try
        {
            Field _field = this.getClass().getDeclaredField("m" + propertyName);
            _field.setAccessible(true);
            _field.set(this, value);
            this.NotifyObservers(propertyName);
            this.mChangedProperties.add(propertyName);
            return true;
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
            return false;
        }
    }

    protected void Reset()
    {
        this.mChangedProperties = new ArrayList<>();
    }

    public ArrayList<String> getChangedProperties()
    {
        return this.mChangedProperties;
    }
}
