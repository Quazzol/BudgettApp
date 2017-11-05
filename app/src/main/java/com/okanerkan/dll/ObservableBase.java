package com.okanerkan.dll;

import com.okanerkan.interfaces.IObservable;
import com.okanerkan.interfaces.IObserver;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Quazzol on 4.11.2017.
 */

public class ObservableBase implements IObservable
{
    public ObservableBase()
    {
        this.mObserverList = new ArrayList<>();
    }

    protected ArrayList<IObserver> mObserverList;

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

    protected boolean SetValue(String propertyName, Object value) throws Exception
    {
        Field _field = this.getClass().getDeclaredField(propertyName);
        _field.setAccessible(true);
        _field.set(this, value);
        this.NotifyObservers(propertyName);
        return true;
    }
}
