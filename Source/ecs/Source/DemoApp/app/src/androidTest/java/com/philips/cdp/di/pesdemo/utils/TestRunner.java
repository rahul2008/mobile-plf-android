package com.philips.cdp.di.pesdemo.utils;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.philips.cdp.di.pesdemo.EcsDemoApplication;

/**
 * Created by F1sherKK on 14/04/16.
 */
public class TestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, EcsDemoApplication.class.getName(), context);
    }
}
