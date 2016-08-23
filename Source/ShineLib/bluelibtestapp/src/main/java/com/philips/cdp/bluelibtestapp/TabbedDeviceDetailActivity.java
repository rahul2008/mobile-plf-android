/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.bluelibtestapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSyncBase;
import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.List;

//TODO: refactor to remove deprecation
public class TabbedDeviceDetailActivity extends AppCompatActivity implements ActionBar.TabListener, SHNDeviceImpl.SHNDeviceListener {
    private static final String TAG = "TabbedDeviceDetailActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private TextView textViewStateValue;
    private Button buttonConnect;
    private SHNDevice shnSelectedDevice;
    private boolean capabilitiesAreConfigured;

    private int failureAttempts;
    private int successAttempts;

    private View.OnClickListener handleConnectButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (shnSelectedDevice.getState() == SHNDeviceImpl.State.Disconnected) {
                buttonConnect.setEnabled(false);
                SHNLogger.d(TAG, "onClick: " + shnSelectedDevice.getState());

                shnSelectedDevice.connect();
                mSectionsPagerAdapter.clear();
                setupOfflineTabs(shnSelectedDevice);
            } else if (shnSelectedDevice.getState() == SHNDeviceImpl.State.Connected) {
                buttonConnect.setEnabled(false);
                shnSelectedDevice.disconnect();
            }
        }
    };


    private ActionBar getActionBarHelper() {
        return getSupportActionBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SHNLogger.d(TAG, "onCreate");
        setContentView(R.layout.activity_tabbed_device_detail);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (actionBar != null) {
                    actionBar.setSelectedNavigationItem(position);
                }
            }
        });

        TextView textViewNameValue = (TextView) findViewById(R.id.textViewNameValue);
        TextView textViewAddressValue = (TextView) findViewById(R.id.textViewAddressValue);
        textViewStateValue = (TextView) findViewById(R.id.textViewStateValue);
        buttonConnect = (Button) findViewById(R.id.connect);
        if (buttonConnect != null) {
            buttonConnect.setOnClickListener(handleConnectButtonClick);
        }

        TestApplication testApplication = (TestApplication) getApplication();
        shnSelectedDevice = testApplication.getSelectedDevice();

        setupOfflineTabs(shnSelectedDevice);

        if (textViewNameValue != null) {
            textViewNameValue.setText(shnSelectedDevice.getName());
        }
        if (textViewAddressValue != null) {
            textViewAddressValue.setText(shnSelectedDevice.getAddress());
        }

        shnSelectedDevice.registerSHNDeviceListener(this);
    }

    private void setupOfflineTabs(SHNDevice device) {
        setupDeviceInformationCapability(device);
    }

    @Override
    public void onStop() {
        super.onStop();
        SHNLogger.d(TAG, "onStop");
        if (isFinishing()) {
            shnSelectedDevice.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SHNLogger.d(TAG, "onDestroy");
        shnSelectedDevice.unregisterSHNDeviceListener(this);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    // implements SHNDeviceImpl.SHNDeviceListener
    @Override
    public void onStateUpdated(SHNDevice shnDevice) {
        final SHNDevice.State currentDeviceState = shnDevice.getState();
        SHNLogger.d(TAG + "Test", "onStateUpdated: " + currentDeviceState);
        textViewStateValue.setText(currentDeviceState.toString());
        setupUIForCurrentDeviceState(shnDevice, currentDeviceState);
    }

    private void setupUIForCurrentDeviceState(SHNDevice shnDevice, SHNDevice.State currentDeviceState) {
        switch (currentDeviceState) {
            case Disconnected:
                // connect to the device once the state switches to Disconnected
                shnSelectedDevice.connect();
                buttonConnect.setText(R.string.connect);
                buttonConnect.setEnabled(true);

                mSectionsPagerAdapter.clear();
                capabilitiesAreConfigured = false;
                break;
            case Connected:
                getCapabilities();

                buttonConnect.setText(R.string.disconnect);
                buttonConnect.setEnabled(true);

                if (!capabilitiesAreConfigured) {
                    setupBatteryCapability(shnDevice);
                    setupFirmwareUpdateCapability(shnDevice);
                    capabilitiesAreConfigured = true;
                }
                break;
            case Connecting:
            case Disconnecting:
                buttonConnect.setEnabled(false);
                capabilitiesAreConfigured = false;
                break;
        }
    }

    private void getCapabilities() {
        SHNCapabilityBattery capabilityBattery = (SHNCapabilityBattery) shnSelectedDevice.getCapabilityForType(SHNCapabilityType.BATTERY);
        if (capabilityBattery != null) {
            capabilityBattery.getBatteryLevel(new SHNIntegerResultListener() {
                @Override
                public void onActionCompleted(int value, SHNResult result) {
                    SHNLogger.i(TAG, "Battery level " + value);
                }
            });
        }

        SHNCapabilityLogSynchronization logSyShnCapability = (SHNCapabilityLogSynchronization) shnSelectedDevice.getCapabilityForType(SHNCapabilityType.LOG_SYNCHRONIZATION);

        if (logSyShnCapability != null) {
            logSyShnCapability.setSHNCapabilityLogSynchronizationListener(new SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener() {
                @Override
                public void onStateUpdated(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization) {

                }

                @Override
                public void onProgressUpdate(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, float progress) {

                }

                @Override
                public void onLogSynchronized(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNLog shnLog, SHNResult shnResult) {
                    SHNLogger.i(TAG, "onLogSynchronized result " + shnResult + " total size: " + shnLog.getLogItems().size());
                    shnSelectedDevice.disconnect();

                    successAttempts++;
                    updateCount();
                }

                @Override
                public void onLogSynchronizationFailed(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNResult shnResult) {
                    SHNLogger.i(TAG, "onLogSynchronizationFailed result " + shnResult);
                    shnSelectedDevice.disconnect();

                    successAttempts++;
                    updateCount();
                }

                @Override
                public void onIntermediateLogSynchronized(SHNCapabilityLogSynchronization shnCapabilityLogSynchronization, SHNLog shnLog) {
                    SHNLogger.i(TAG, "onIntermediateLogSynchronized result " + " size: " + shnLog.getLogItems().size());
                }
            });
            logSyShnCapability.startSynchronizationFromToken(logSyShnCapability.getLastSynchronizationToken());
        }
    }

    private void updateCount() {
        TextView textView = (TextView) findViewById(R.id.textViewCount);
        String string = String.format(getString(R.string.count), failureAttempts, successAttempts);
        assert textView != null;
        textView.setText(string);
    }

    @Override
    public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
        failureAttempts++;
        updateCount();
    }

    private void setupDeviceInformationCapability(SHNDevice shnDevice) {
        if (shnDevice.getCapabilityForType(SHNCapabilityType.DeviceInformation) != null) {
            DeviceInformationFragment deviceInformationFragment = DeviceInformationFragment.newInstance();
            mSectionsPagerAdapter.addFragment(deviceInformationFragment, "DeviceInfo");
        }
    }

    private void setupBatteryCapability(SHNDevice shnDevice) {
        if (shnDevice.getCapabilityForType(SHNCapabilityType.BATTERY) != null) {
            BatteryFragment batteryFragment = BatteryFragment.newInstance();
            mSectionsPagerAdapter.addFragment(batteryFragment, "Battery");
        }
    }

    private void setupFirmwareUpdateCapability(SHNDevice shnDevice) {
        if (shnDevice.getCapabilityForType(SHNCapabilityType.FIRMWARE_UPDATE) != null) {
            FirmwareUpdateFragment firmwareUpdateFragment = FirmwareUpdateFragment.newInstance();
            mSectionsPagerAdapter.addFragment(firmwareUpdateFragment, "Update");
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String tabName) {
            ActionBar actionBar = getActionBarHelper();
            fragments.add(fragment);
            notifyDataSetChanged();
            mViewPager.setOffscreenPageLimit(fragments.size());
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(tabName)
                            .setTabListener(TabbedDeviceDetailActivity.this));
        }

        public void clear() {
            mViewPager.setCurrentItem(0);
            fragments.clear();
            ActionBar actionBar = getActionBarHelper();
            actionBar.removeAllTabs();
            notifyDataSetChanged();
            mViewPager.setOffscreenPageLimit(fragments.size());
        }
    }
}
