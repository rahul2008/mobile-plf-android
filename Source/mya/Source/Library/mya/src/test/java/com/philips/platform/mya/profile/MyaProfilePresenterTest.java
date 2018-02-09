/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.profile;


import android.content.Context;
import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.R;
import com.philips.platform.mya.details.MyaDetailsFragment;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.myaplugin.uappadaptor.DataModel;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.user.UserDataModelProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.philips.platform.mya.launcher.MyaInterface.MYA_LAUNCH_INPUT;
import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
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
        MyaProfileFragment myaProfileFragment = new MyaProfileFragment();
        myaProfilePresenter.onViewActive(myaProfileFragment);
        myaProfilePresenter.onViewInactive();
        assertNull(myaProfilePresenter.getView());
        assertNotNull(myaProfilePresenter.getMyaDetailsFragment());
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
        myaProfilePresenter.processUi(appInfraInterface,getArguments(null));
        verify(view).showProfileItems(anyMap());
    }

    @Test
    public void testHandleOnClickProfileItem() {
        final MyaDetailsFragment myaDetailsFragment = mock(MyaDetailsFragment.class);
        myaProfilePresenter = new MyaProfilePresenter(view) {
            @Override
            MyaDetailsFragment getMyaDetailsFragment() {
                return myaDetailsFragment;
            }
        };
        String key="MYA_My_details";
        assertTrue(myaProfilePresenter.handleOnClickProfileItem(key, null));
        verify(myaDetailsFragment).setArguments(null);
        verify(view).showPassedFragment(myaDetailsFragment);
        assertFalse(myaProfilePresenter.handleOnClickProfileItem("some_item",null));
    }

    private Bundle getArguments(DataModel userDataModel) {
        Bundle arguments = new Bundle();
        UserDataModelProvider userDataModelProvider = mock(UserDataModelProvider.class);
        when(userDataModelProvider.getData(DataModelType.USER)).thenReturn(userDataModel);
        MyaLaunchInput value = new MyaLaunchInput(context, null);
        String[] settingsItems = {"settings1","settings2"};
        value.setProfileConfigurableItems(Arrays.asList(settingsItems));
        arguments.putSerializable(MYA_LAUNCH_INPUT, value);
        arguments.putSerializable(USER_PLUGIN, userDataModelProvider);
        return arguments;
    }
}