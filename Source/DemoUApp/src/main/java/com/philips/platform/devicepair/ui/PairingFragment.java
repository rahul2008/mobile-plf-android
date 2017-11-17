/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.dicommclient.port.common.PairingPort;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.devicepair.R;
import com.philips.platform.devicepair.pojo.PairDevice;
import com.philips.platform.devicepair.states.GetPairedDevicesState;
import com.philips.platform.devicepair.states.StateContext;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairingFragment extends DevicePairingBaseFragment implements IDevicePairingListener, SynchronisationCompleteListener {
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;
    private Context mContext;
    private PairingFragmentPresenter mLaunchFragmentPresenter;

    private PairDevice mPairDevice;

    private ArrayAdapter<String> mAvailableDevicesAdapter;
    private ArrayAdapter<String> mPairedDevicesAdapter;

    private List<String> mAvailableDevicesList;
    private List<String> mDiscoveredDevices;
    private List<Appliance> mAppliancesList;
    private List<String> mPairedDevicesList;
    private CommCentral commCentral;
    private Runnable permissionCallback;

    private ApplianceManager.ApplianceListener applianceListener = new ApplianceManager.ApplianceListener() {

        @Override
        public void onApplianceFound(@NonNull Appliance appliance) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDiscoveredDevices(getDiscoveredDevices(commCentral.getApplianceManager().getAvailableAppliances()));
                }
            });
        }

        @Override
        public void onApplianceUpdated(@NonNull Appliance appliance) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDiscoveredDevices(getDiscoveredDevices(commCentral.getApplianceManager().getAvailableAppliances()));
                }
            });
        }

        @Override
        public void onApplianceLost(@NonNull Appliance appliance) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDiscoveredDevices(getDiscoveredDevices(commCentral.getApplianceManager().getAvailableAppliances()));
                }
            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataServicesManager.getInstance().synchronize();
        commCentral = DevicePairingUappInterface.getCommCentral();
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.pairing_fragment_title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.pairing_fragment_title);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pairing_layout, container, false);

        mLaunchFragmentPresenter = new PairingFragmentPresenter(getActivity());
        mPairedDevicesList = new ArrayList<>();
        mAvailableDevicesList = new ArrayList<>();
        mAppliancesList = new ArrayList<>();

        mAvailableDevicesAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1) {
            @NonNull
            public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setText(getItem(position));
                return view;
            }
        };

        mPairedDevicesAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1) {
            @NonNull
            public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setText(getItem(position));
                return view;
            }
        };

        Button mLogoutBtn = (Button) view.findViewById(R.id.logout);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        ListView mAvailableDevicesListView = (ListView) view.findViewById(R.id.available_devices);
        mAvailableDevicesListView.setAdapter(mAvailableDevicesAdapter);
        mAvailableDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (mAppliancesList != null && mAppliancesList.size() > 0) {
                    PairDevice pairDeviceDetails = getDeviceDetails(mAvailableDevicesList.get(position));
                    showProgressDialog(getString(R.string.pairing_device));
                    pairToDevice(mAppliancesList.get(position));
                    if (pairDeviceDetails != null) {
                        mLaunchFragmentPresenter.pairDevice(pairDeviceDetails, PairingFragment.this);
                    }
                }
            }
        });

        ListView mPairedDevicesListView = (ListView) view.findViewById(R.id.paired_devices);
        mPairedDevicesListView.setAdapter(mPairedDevicesAdapter);
        mPairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                showProgressDialog(getString(R.string.unpairing_device));
                mLaunchFragmentPresenter.unPairDevice(mPairedDevicesAdapter.getItem(position), PairingFragment.this);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isProgressShowing()) {
            showProgressDialog(getString(R.string.get_paired_device));
            StateContext mStateContext = new StateContext();
            mStateContext.setState(new GetPairedDevicesState(getActivity(), this));
            mStateContext.start();
        }

        commCentral.getApplianceManager().addApplianceListener(applianceListener);
        startDiscovery();
    }

    @Override
    public void onPause() {
        super.onPause();
        commCentral.getApplianceManager().removeApplianceListener(applianceListener);
        commCentral.stopDiscovery();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private PairDevice getDeviceDetails(String id) {
        for (int i = 0; i < mAppliancesList.size(); i++) {
            if (mAppliancesList.get(i).getNetworkNode().getCppId().equalsIgnoreCase(id)) {
                mPairDevice = new PairDevice();
                mPairDevice.setDeviceID(mAppliancesList.get(i).getNetworkNode().getCppId());
                mPairDevice.setDeviceType(mAppliancesList.get(i).getNetworkNode().getDeviceType());
                return mPairDevice;
            }
        }
        return null;
    }

    public void updatePairedDevices(List<String> pairedDevices) {
        mPairedDevicesAdapter.clear();
        mPairedDevicesList.clear();

        mPairedDevicesList.addAll(pairedDevices);
        mPairedDevicesAdapter.addAll(pairedDevices);

        for (int i = 0; i < pairedDevices.size(); i++) {
            removeFromAvailableDevices(pairedDevices.get(i));
        }
    }

    public void addToPairedDevices(String deviceID) {
        if (!mPairedDevicesList.contains(deviceID)) {
            mPairedDevicesList.add(deviceID);
            mPairedDevicesAdapter.add(deviceID);
        }
    }

    public void removeFromPairedDevices(String deviceID) {
        mPairedDevicesList.remove(deviceID);
        mPairedDevicesAdapter.remove(deviceID);
    }

    public void addToAvailableDevices(String deviceID) {
        if (!mAvailableDevicesList.contains(deviceID)) {
            mAvailableDevicesList.add(deviceID);
            mAvailableDevicesAdapter.add(deviceID);
        }
    }

    public void removeFromAvailableDevices(String deviceID) {
        mAvailableDevicesList.remove(deviceID);
        mAvailableDevicesAdapter.remove(deviceID);
    }

    public List<String> getDiscoveredDevices(Set<Appliance> discoveredAppliances) {
        List<Appliance> setAsList = new ArrayList<>(discoveredAppliances);
        List<String> devices = new ArrayList<>();

        if (discoveredAppliances.size() > 0) {
            for (int i = 0; i < setAsList.size(); i++) {
                mAppliancesList.add(setAsList.get(i));
                devices.add(setAsList.get(i).getNetworkNode().getCppId());
            }
        }
        return devices;
    }

    private void pairToDevice(Appliance appliance) {
        PairingPort pairingPort = appliance.getPairingPort();
        String permission[] = new String[0];
        String secretKeyGen = "";
        pairingPort.triggerPairing("cphuser", "", new User(mContext).getHsdpUUID(),
                secretKeyGen, "urn:cdp|datareceiver_stg", permission);
    }

    public void updateDiscoveredDevices(List<String> discoveredDevices) {
        mDiscoveredDevices = discoveredDevices;
        updateAvailableDevices();
    }

    public void updateAvailableDevices() {
        for (int i = 0; i < mDiscoveredDevices.size(); i++) {
            if (!(mAvailableDevicesList.contains(mDiscoveredDevices.get(i)) || mPairedDevicesList.contains(mDiscoveredDevices.get(i)))) {
                mAvailableDevicesList.add(mDiscoveredDevices.get(i));
            }
        }

        updateAvailableDevicesList();
    }

    public void updateAvailableDevicesList() {
        mAvailableDevicesAdapter.clear();
        mAvailableDevicesAdapter.addAll(mAvailableDevicesList);
    }

    @Override
    public void onGetPairedDevices(List<String> pairedDeviceList) {
        dismissProgressDialog();
        updatePairedDevices(pairedDeviceList);
    }

    @Override
    public void onDevicePaired(String pairedDeviceID) {
        dismissProgressDialog();
        addToPairedDevices(pairedDeviceID);
        removeFromAvailableDevices(pairedDeviceID);
        showAlertDialog(getString(R.string.pairing_success));
    }

    @Override
    public void onDeviceUnPaired(String unPairedDeviceID) {
        dismissProgressDialog();
        removeFromPairedDevices(unPairedDeviceID);
        if (mDiscoveredDevices != null && mDiscoveredDevices.size() > 0)
            addToAvailableDevices(unPairedDeviceID);
        showAlertDialog(getString(R.string.un_pairing_success));
    }

    @Override
    public void onError(String errorMessage) {
        dismissProgressDialog();
        showAlertDialog(errorMessage);
    }

    @Override
    public void onAccessTokenExpiry() {
        DataServicesManager.getInstance().synchronize();
    }

    @Override
    public void onInternetError() {
        dismissProgressDialog();
        showAlertDialog(getString(R.string.check_connection));
    }

    @Override
    public void onConsentNotAccepted() {
        dismissProgressDialog();
        launchConsents();
    }

    @Override
    public void onConsentsAccepted() {
        dismissProgressDialog();
        launchSubjectProfile();
    }

    @Override
    public void onProfileNotCreated() {
        dismissProgressDialog();
        launchSubjectProfile();
    }

    @Override
    public void onProfileCreated() {
        showProgressDialog(getString(R.string.pairing_device));
        mLaunchFragmentPresenter.pairDevice(mPairDevice, PairingFragment.this);
    }

    private void launchConsents() {
        ConsentsFragment consentsFragment = new ConsentsFragment();
        consentsFragment.setDeviceStatusListener(this);
        showFragment(consentsFragment);
    }

    private void launchSubjectProfile() {
        CreateSubjectProfileFragment createProfileFragment = new CreateSubjectProfileFragment();
        createProfileFragment.setDeviceStatusListener(this);
        showFragment(createProfileFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearProgressDialog();
    }

    @Override
    public void onSyncComplete() {
//        mLaunchFragmentPresenter.pairDevice(getTepastDeviceDetails(), PairingFragment.this);
    }

    @Override
    public void onSyncFailed(Exception e) {
        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLaunchFragmentPresenter.pairDevice(getTestDeviceDetails(), PairingFragment.this);
            }
        }, 10000);*/
    }

    public void logOut() {
        User user = new User(getContext());
        if (!user.isUserSignIn()) return;

        user.logout(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void onLogoutFailure(int i, String s) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void startDiscovery() {
        try {
            commCentral.startDiscovery();
        } catch (MissingPermissionException e) {

            acquirePermission(new Runnable() {
                @Override
                public void run() {
                    startDiscovery();
                }
            });
        }
    }

    private void acquirePermission(@NonNull Runnable permissionCallback) {
        this.permissionCallback = permissionCallback;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().post(this.permissionCallback);
                }
            }
        }
    }

}