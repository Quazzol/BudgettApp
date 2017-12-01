package com.okanerkan.globals;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by OkanErkan on 1.12.2017.
 */

public class ExceptionHandler
{
    public static void HandleException(String _tag, Exception _ex)
    {
        Log.e(_tag, _ex.getMessage());
        Toast.makeText(KNGlobal.FixedContext(), _ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}
