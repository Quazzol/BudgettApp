package com.okanerkan.globals;

import android.widget.Toast;

/**
 * Created by OkanErkan on 1.12.2017.
 */

public class Message
{
    public static void Show(String _message)
    {
        Toast.makeText(KNGlobal.FixedContext(), _message, Toast.LENGTH_LONG).show();
    }

    public static void Show(int _resID)
    {
        Toast.makeText(KNGlobal.FixedContext(), _resID, Toast.LENGTH_LONG).show();
    }
}
