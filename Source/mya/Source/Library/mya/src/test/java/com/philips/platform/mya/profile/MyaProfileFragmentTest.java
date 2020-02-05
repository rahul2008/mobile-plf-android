/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya.profile;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MyaProfileFragmentTest {

    private Context mContext;
    private MyaProfileFragment myaProfileFragment;

    @Mock
    private DefaultItemAnimator defaultItemAnimator;

    @Mock
    private RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration;

    @Mock
    private LinearLayoutManager linearLayoutManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        AppInfra appInfra = new AppInfra.Builder().build(mContext);
        MyaHelper.getInstance().setAppInfra(appInfra);
        AppTaggingInterface appTaggingInterfaceMock = mock(AppTaggingInterface.class);
        MyaHelper.getInstance().setAppTaggingInterface(appTaggingInterfaceMock);
        myaProfileFragment = new MyaProfileFragment();
//        SupportFragmentTestUtil.startFragment(myaProfileFragment);
        myaProfileFragment.init(defaultItemAnimator, recyclerViewSeparatorItemDecoration, linearLayoutManager);
    }

    @Test
    public void testEquals_getActionbarTitleResId() throws Exception {
        assertEquals(R.string.MYA_My_account, myaProfileFragment.getActionbarTitleResId());
    }

    @Test
    public void testNotNull_getActionbarTitle() throws Exception {
        assertNotNull(myaProfileFragment.getActionbarTitle(mContext));
    }

    @Test
    public void notNullgetBackButtonState() throws Exception {
        assertNotNull(myaProfileFragment.getBackButtonState());
    }

    @Test
    public void ShouldListProfileItems() {
        TreeMap<String, String> profileList = new TreeMap<>();
        profileList.put("MYA_My_details", "My details");
        myaProfileFragment.showProfileItems(profileList);
        RecyclerView recyclerView = myaProfileFragment.getView().findViewById(R.id.profile_recycler_view);
        assertEquals(recyclerView.getLayoutManager(), linearLayoutManager);
        assertEquals(recyclerView.getItemAnimator(), defaultItemAnimator);
        assertTrue(recyclerView.getAdapter() instanceof MyaProfileAdaptor);
        assertEquals(recyclerView.getAdapter().getItemCount(), 1);
    }

    @Test
    public void shouldsetUserName() {
        Label label = myaProfileFragment.getView().findViewById(R.id.mya_user_name);
        myaProfileFragment.setUserName("some_name");
        assertEquals(label.getText(), "some_name");
    }

    @Test
    public void ShouldonSaveInstanceState() {
        Bundle savedBundle = new Bundle();
        Bundle argumentBundle = new Bundle();
        argumentBundle.putInt("some_key", 100);
        myaProfileFragment.setArguments(argumentBundle);
        myaProfileFragment.onSaveInstanceState(savedBundle);
        Bundle profile_bundle = savedBundle.getBundle("profile_bundle");
        assertTrue(profile_bundle.equals(argumentBundle));
        assertEquals(profile_bundle.getInt("some_key"), 100);
    }

    @Test
    public void ShouldshowPassedFragment() {
        FragmentLauncher fragmentLauncher = mock(FragmentLauncher.class);
        ActionBarListener actionBarListener = mock(ActionBarListener.class);
        myaProfileFragment.setFragmentLauncher(fragmentLauncher);
        myaProfileFragment.setActionbarUpdateListener(actionBarListener);
    }
}