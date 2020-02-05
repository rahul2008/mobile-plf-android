/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya.profile;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MyaProfilePresenterTest {

    private MyaProfileContract.View view;
    private Context context;
    private MyaProfilePresenter myaProfilePresenter;

    @Before
    public void setup() {
        view = mock(MyaProfileContract.View.class);
        context = mock(Context.class);
        FragmentActivity fragmentActivityMock = mock(FragmentActivity.class);
        when(view.getFragmentActivity()).thenReturn(fragmentActivityMock);
        myaProfilePresenter = new MyaProfilePresenter(view);
        MyaProfileFragment myaProfileFragment = new MyaProfileFragment();
        myaProfilePresenter.onViewActive(myaProfileFragment);
        myaProfilePresenter.onViewInactive();
        assertNull(myaProfilePresenter.getView());
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
        MyaHelper.getInstance().setMyaLaunchInput(new MyaLaunchInput(context));
        myaProfilePresenter.getProfileItems(appInfraInterface, getArguments());
        verify(view).showProfileItems(ArgumentMatchers.<String, String>anyMap());
    }

    @Test
    public void testHandleOnClickProfileItem() {

        String key = "MYA_My_details";
        assertFalse(myaProfilePresenter.handleOnClickProfileItem(key, null));
        assertFalse(myaProfilePresenter.handleOnClickProfileItem("some_item", null));
    }

    private Bundle getArguments() {
        Bundle arguments = new Bundle();
        UserDataInterface userDataInterface = mock(UserDataInterface.class);
        MyaLaunchInput value = new MyaLaunchInput(context);
        String[] settingsItems = {"settings1", "settings2"};
        value.setProfileMenuList(Arrays.asList(settingsItems));
        arguments.putSerializable(USER_PLUGIN, userDataInterface);
        return arguments;
    }
}