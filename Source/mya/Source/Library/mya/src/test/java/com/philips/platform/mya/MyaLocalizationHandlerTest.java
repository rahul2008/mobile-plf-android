/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class MyaLocalizationHandlerTest {

    private MyaLocalizationHandler myaLocalizationHandler;

    @Before
    public void setup() {
        myaLocalizationHandler = new MyaLocalizationHandler();
    }

    @Test
    public void ShouldgetLocalisedList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("MYA_My_details");
        TreeMap<String,String> map = new TreeMap<>();
        Application application = RuntimeEnvironment.application;
        Map<String, String> localisedList = myaLocalizationHandler.getLocalisedList(application, arrayList, map);
        assertEquals(arrayList.size(),1);
        assertEquals(localisedList.get("MYA_My_details"),application.getString(R.string.MYA_My_details));
        assertEquals(myaLocalizationHandler.getStringResourceByName(application,"MYA_My_details"),application.getString(R.string.MYA_My_details));
    }


}