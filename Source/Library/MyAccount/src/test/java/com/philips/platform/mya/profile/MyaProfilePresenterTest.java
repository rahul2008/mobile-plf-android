/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.profile;


import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.R;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserDataModel;
import com.philips.platform.myaplugin.user.UserDataModelProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyaProfilePresenterTest {

    private MyaProfileContract.View view;
    private Context context;
    private MyaProfilePresenter myaProfilePresenter;


    @Before
    public void setup() {
        view = mock(MyaProfileContract.View.class);
        context = mock(Context.class);
        when(view.getContext()).thenReturn(context);
        myaProfilePresenter = new MyaProfilePresenter(view);
    }

    @Test
    public void testGettingProfileItems() {
        AppInfraInterface appInfraInterface = mock(AppInfraInterface.class);
        when(context.getString(R.string.MYA_My_details)).thenReturn("some details");
        ArrayList arrayList = new ArrayList();
        AppConfigurationInterface.AppConfigurationError error = new AppConfigurationInterface.AppConfigurationError();
        AppConfigurationInterface appConfigurationInterface = mock(AppConfigurationInterface.class);
        when(appConfigurationInterface.getPropertyForKey("profile.menuItems", "mya", error)).thenReturn(arrayList);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterface);
        myaProfilePresenter.getProfileItems(appInfraInterface);
        verify(view).showProfileItems(anyMap());
    }

    @Test
    public void testSetUserName() {
        UserDataModelProvider userDataModelProvider = mock(UserDataModelProvider.class);
        UserDataModel userDataModel = new UserDataModel();
        userDataModel.setEmail("some_email");
        userDataModel.setGivenName("some_name");
        userDataModel.setFamilyName("some_family_name");
        userDataModel.setMobileNumber("98278383939");
        userDataModel.setGender("male");
        Date birthday = new Date();
        userDataModel.setBirthday(birthday);
        userDataModel.setEmail("some_email");
        when(userDataModelProvider.getData(DataModelType.USER)).thenReturn(userDataModel);
        myaProfilePresenter.setUserName(userDataModelProvider);
        verify(view).setUserName(userDataModel.getGivenName());
    }
}