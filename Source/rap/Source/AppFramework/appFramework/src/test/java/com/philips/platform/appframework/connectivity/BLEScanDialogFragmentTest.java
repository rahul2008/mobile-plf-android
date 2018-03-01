/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.communication.BleCommunicationStrategy;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;


@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class, sdk=25)
public class BLEScanDialogFragmentTest {

    private BLEScanDialogFragment bleScanDialogFragment;

    private ActivityController<TestActivity> activityController;

    @Mock
    private NetworkNode networkNode;

    @Mock
    private BleCommunicationStrategy bleCommunicationStrategy;

    @Mock
    private BLEScanDialogFragment.BLEScanDialogListener listener;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        activityController= Robolectric.buildActivity(TestActivity.class);
        final TestActivity testActivity = activityController.create().start().get();
        bleScanDialogFragment = new BLEScanDialogFragment();
        FragmentManager fm = testActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        final Set<Appliance> applianceSet = new HashSet<>();
        applianceSet.add(new RefAppBleReferenceAppliance(networkNode,bleCommunicationStrategy));
        bleScanDialogFragment.show(ft, "fragment");
        bleScanDialogFragment.setSavedApplianceList(applianceSet);
    }

    @Test
    public void getDeviceCountTest(){
        assertEquals(bleScanDialogFragment.getDeviceCount(), 1);
    }

    @Test
    public void dialogDimissTest(){
        bleScanDialogFragment.setBLEDialogListener(listener);
        ListView listview=(ListView)bleScanDialogFragment.getDialog().findViewById(R.id.device_listview);
        BleDeviceListAdapter adapter = (BleDeviceListAdapter) listview.getAdapter();
        View itemView = adapter.getView(0, null, listview);
        listview.performItemClick(itemView,0,adapter.getItemId(0));
        assertFalse(bleScanDialogFragment.isAdded());
    }

    @Test
    public void hideProgressBarTest(){
        ProgressBar progressBar= (ProgressBar)bleScanDialogFragment.getDialog().findViewById(R.id.scanning_progress_bar);
        bleScanDialogFragment.hideProgressBar();
        assertEquals(View.GONE,progressBar.getVisibility());
    }
    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
    }

}