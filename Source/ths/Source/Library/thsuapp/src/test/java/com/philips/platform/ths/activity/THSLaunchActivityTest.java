/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static com.philips.platform.ths.utility.THSConstants.KEY_ACTIVITY_THEME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSLaunchActivityTest {

    THSLaunchActivity mThsLaunchActivity;

    @Mock
    Toolbar toolbarMock;

    @Mock
    FragmentManager fragmentManagerMock;

    @Captor
    ArgumentCaptor<Fragment> fragmentArgumentCaptor;

    @Mock
    FragmentTransaction fragmentTransactionMock;

    Intent intent;

    @Mock
    Bundle bundleMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(fragmentManagerMock.beginTransaction()).thenReturn(fragmentTransactionMock);
        intent = new Intent();
        intent.putExtra(KEY_ACTIVITY_THEME, 0);
    }


    @Test
    public void shouldNotApplied_DefaultTheme() throws Exception {
        int themeIndex = intent.getIntExtra(KEY_ACTIVITY_THEME, 0);
        startActivity(intent);
        Assert.assertEquals(themeIndex, 0);
    }

    @Test
    public void shouldSaveInstanceIsNotnull() throws Exception {
        mThsLaunchActivity = buildActivity(THSLaunchActivity.class, intent).get();
        THSLaunchActivity spyActivity = Mockito.spy(mThsLaunchActivity);

        Mockito.doReturn(fragmentManagerMock).when(spyActivity).getSupportFragmentManager();
        spyActivity.onCreate(mock(Bundle.class));

    }


    private void startActivity(Intent intent) {
        mThsLaunchActivity = buildActivity(THSLaunchActivity.class, intent).get();
        THSLaunchActivity spyActivity = Mockito.spy(mThsLaunchActivity);
        Mockito.doReturn(fragmentManagerMock).when(spyActivity).getSupportFragmentManager();
        spyActivity.onCreate(null);
       // verify(fragmentTransactionMock).replace(anyInt(), any(THSBaseFragment.class), anyString());
    }

  @Test
    public void shouldCalled_OnDestryView() {
        startActivity(intent);
        Assert.assertNotNull(mThsLaunchActivity);
    }

    @Test
    public void shouldCalled_OnPause() {
        startActivity(intent);
        Assert.assertNotNull(mThsLaunchActivity);
    }



    @Test
    public void shouldCalled_onBackPressed() {
        startActivity(intent);
        mThsLaunchActivity.onBackPressed();
    }

    @Test
    public void setTitle() throws Exception {
        startActivity(intent);
        mThsLaunchActivity.setTitle("Amwell");
        final CharSequence title = mThsLaunchActivity.getTitle();
        assert title == "Amwell";
    }

    @Test
    public void setTitle1() throws Exception {
        startActivity(intent);
        mThsLaunchActivity.setTitle(R.string.ths_welcome);
        final CharSequence title = mThsLaunchActivity.getTitle();
        assert title == mThsLaunchActivity.getString(R.string.ths_welcome);
    }

    @Test
    public void updateActionBar() throws Exception {
        startActivity(intent);
        mThsLaunchActivity.toolbar = toolbarMock;
        mThsLaunchActivity.updateActionBar(R.string.ths_welcome,true);
        verify(toolbarMock).setNavigationIcon(any(Drawable.class));
    }

    @Test
    public void updateActionBar1() throws Exception {
        startActivity(intent);
        mThsLaunchActivity.toolbar = toolbarMock;
        mThsLaunchActivity.updateActionBar(mThsLaunchActivity.getString(R.string.ths_welcome),false);
        verify(toolbarMock).setNavigationIcon(null);
    }

}