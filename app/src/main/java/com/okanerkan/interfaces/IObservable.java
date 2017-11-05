package com.okanerkan.interfaces;

/**
 * Created by Quazzol on 4.11.2017.
 */

public interface IObservable
{
    public void AddObserver(IObserver observer);
    public void RemoveObserver(IObserver observer);
    public void NotifyObservers(String propertyName);
}
