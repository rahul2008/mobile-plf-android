/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MyaSettingsFragmentTest {
    private Context mContext;
    private MyaSettingsFragment myaSettingsFragment;


    @Mock
    private DefaultItemAnimator defaultItemAnimator;
    @Mock
    private RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration;
    @Mock
    private LinearLayoutManager linearLayoutManager;
    @Mock
    private MyaSettingsAdapter myaSettingsAdapter;
    @Mock
    private ConsentManagerInterface consentManagerInterface;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        myaSettingsFragment = new MyaSettingsFragment();
        ServiceDiscoveryInterface serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        AppTaggingInterface appTaggingInterfaceMock = mock(AppTaggingInterface.class);
        ArrayList<String> arrayList = new ArrayList<>();
        getArray(arrayList);
        AppInfra appInfra = mock(AppInfra.class);
        when(appInfra.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        MyaLaunchInput myaLaunchInput = new MyaLaunchInput(mContext);
        MyaHelper.getInstance().setAppInfra(appInfra);
        MyaHelper.getInstance().setMyaLaunchInput(myaLaunchInput);
        MyaHelper.getInstance().setAppTaggingInterface(appTaggingInterfaceMock);
//        SupportFragmentTestUtil.startFragment(myaSettingsFragment);
        myaLaunchInput.setSettingsMenuList(arrayList);
        myaSettingsFragment.init(defaultItemAnimator, recyclerViewSeparatorItemDecoration, linearLayoutManager);
    }

    private void getArray(ArrayList<String> arrayList) {
        arrayList.add("MYA_Country");
        arrayList.add("Mya_Privacy_Settings");
        arrayList.add("Mya_Privacy_Settings2");
    }

    @Test
    public void testEquals_getActionbarTitleResId() throws Exception{
        assertEquals(R.string.MYA_My_account,myaSettingsFragment.getActionbarTitleResId());
    }

    @Test
    public void testNotNull_getActionbarTitle() throws Exception {
        assertNotNull(myaSettingsFragment.getActionbarTitle(mContext));
    }

    @Test
    public void notNullgetBackButtonState() throws Exception {
        assertNotNull(myaSettingsFragment.getBackButtonState());
    }


    @Test
    public void ShouldonSaveInstanceState() {
        Bundle savedBundle = new Bundle();
        Bundle argumentBundle = new Bundle();
        argumentBundle.putInt("some_key", 100);
        myaSettingsFragment.setArguments(argumentBundle);
        myaSettingsFragment.onSaveInstanceState(savedBundle);
        Bundle settings_bundle = savedBundle.getBundle("settings_bundle");
        assertTrue(settings_bundle.equals(argumentBundle));
        assertEquals(settings_bundle.getInt("some_key"), 100);
    }

    @Test
    public void ShouldListSettingsItems() {
        TreeMap<String, SettingsModel> map = new TreeMap<>();
        SettingsModel settingsModel = new SettingsModel();
        map.put("MYA_My_details", settingsModel);
        myaSettingsFragment.showSettingsItems(map);
        RecyclerView recyclerView = myaSettingsFragment.getView().findViewById(R.id.mya_settings_recycler_view);
        assertEquals(recyclerView.getLayoutManager(), linearLayoutManager);
        assertEquals(recyclerView.getItemAnimator(), defaultItemAnimator);
        assertTrue(recyclerView.getAdapter() instanceof MyaSettingsAdapter);
        assertEquals(recyclerView.getAdapter().getItemCount(), 1);
    }

    //TODO - committed for time being
    /*@Test
    public void ShouldOnActivityCreated() {
        ArrayList<String> arrayList = new ArrayList<>();
        getArray(arrayList);
        AppInfra appInfra = mock(AppInfra.class);
        MyaLaunchInput myaLaunchInput = new MyaLaunchInput(mContext,null);
        AppConfigurationInterface appConfigurationInterface = mock(AppConfigurationInterface.class);
        when(appConfigurationInterface.getPropertyForKey("settings.menuItems", "mya", myaSettingsFragment.getError())).thenReturn(arrayList);
        when(appInfra.getConfigInterface()).thenReturn(appConfigurationInterface);
        myaLaunchInput.setSettingsMenuList(arrayList);
        MyaHelper.getInstance().setMyaLaunchInput(myaLaunchInput);
        ServiceDiscoveryInterface serviceDiscoveryInterface = mock(ServiceDiscoveryInterface.class);
        when(serviceDiscoveryInterface.getHomeCountry()).thenReturn("IN");
        when(appInfra.getServiceDiscovery()).thenReturn(serviceDiscoveryInterface);
        MyaHelper.getInstance().setAppInfra(appInfra);
        SupportFragmentTestUtil.startFragment(myaSettingsFragment);
        myaSettingsFragment.init(defaultItemAnimator, recyclerViewSeparatorItemDecoration, linearLayoutManager);

        myaSettingsFragment.onActivityCreated(null);
        RecyclerView recyclerView = myaSettingsFragment.getView().findViewById(R.id.mya_settings_recycler_view);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertTrue(adapter instanceof MyaSettingsAdapter);
        MyaSettingsAdapter myaSettingsAdapter = (MyaSettingsAdapter)adapter;
        Map<String, SettingsModel> settingsList = myaSettingsAdapter.getSettingsList();
        MyaHelper.getInstance().setMyaLaunchInput(myaLaunchInput);
        SettingsModel settingsModel = settingsList.get("MYA_Country");
        assertEquals(settingsModel.getFirstItem(),RuntimeEnvironment.application.getString(R.string.MYA_Country));
        assertEquals(settingsModel.getItemCount(),2);
        assertEquals(settingsModel.getSecondItem(),serviceDiscoveryInterface.getHomeCountry());

    }*/

    /*@Test
    public void ShouldShowDialogWhenCalled() {
        myaSettingsFragment.showDialog("some_title","some_message");
        View dialogView = myaSettingsFragment.getDialogView();
        Label textView = dialogView.findViewById(R.id.message_label);
        Label title_label = dialogView.findViewById(R.id.title_label);
        assertEquals("some_title",title_label.getText());
        assertEquals("some_message",textView.getText());
    }*/

    @Test
    public void ShouldDismissDialog() {
        myaSettingsFragment = new MyaSettingsFragment();
    }



}
