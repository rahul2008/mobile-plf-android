package com.philips.platform.mya.profile;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.details.MyaDetailsFragment;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

@PrepareForTest({ ThemeUtils.class })
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaProfileFragmentTest {

    private Context mContext;
    private MyaProfileFragment myaProfileFragment;

    @Mock
    private DefaultItemAnimator defaultItemAnimator;
    @Mock
    private RecyclerViewSeparatorItemDecoration recyclerViewSeparatorItemDecoration;
    @Mock
    private LinearLayoutManager linearLayoutManager;
    @Mock
    private ColorStateList colorStateList;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        PowerMockito.mockStatic(ThemeUtils.class);
        when(ThemeUtils.buildColorStateList((Context) any(), (Integer) any())).thenReturn(colorStateList);
        AppInfra appInfra = new AppInfra.Builder().build(mContext);
        MyaHelper.getInstance().setAppInfra(appInfra);
        myaProfileFragment = new MyaProfileFragment();
        SupportFragmentTestUtil.startFragment(myaProfileFragment);
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
        MyaDetailsFragment myaDetailsFragment = new MyaDetailsFragment();
        myaProfileFragment.showPassedFragment(myaDetailsFragment);
        assertEquals(myaDetailsFragment.getFragmentLauncher(), fragmentLauncher);
        assertEquals(myaDetailsFragment.getActionbarUpdateListener(), actionBarListener);
    }
}