/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import android.app.Application;

import com.philips.platform.mya.runner.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
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