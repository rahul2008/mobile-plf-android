/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.platform.dprdemo.R;
import com.philips.platform.dprdemo.devicesetup.SampleApplianceFactory;
import com.philips.platform.dprdemo.devicesetup.SampleKpsConfigurationInfo;
import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.states.GetPairedDevicesState;
import com.philips.platform.dprdemo.states.StateContext;

import java.util.ArrayList;
import java.util.List;

public class PairingFragment extends DevicePairingBaseFragment implements IDevicePairingListener {
    private Context mContext;
    private PairingFragmentPresenter mLaunchFragmentPresenter;

    private DiscoveryManager<?> mDiscoveryManager;
    private PairDevice mPairDevice;

    private ListView mAvailableDevicesListView;
    private ListView mPairedDevicesListView;

    private ArrayAdapter<String> mAvailableDevicesAdapter;
    private ArrayAdapter<String> mPairedDevicesAdapter;

    private List<String> mAvailableDevicesList;
    private List<String> mDiscoveredDevices;
    private List<Appliance> mAppliancesList;
    private List<String> mPairedDevicesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDiComm();
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
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setText(getItem(position));
                return view;
            }
        };

        mPairedDevicesAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setText(getItem(position));
                return view;
            }
        };

        mAvailableDevicesListView = (ListView) view.findViewById(R.id.available_devices);
        mAvailableDevicesListView.setAdapter(mAvailableDevicesAdapter);
        mAvailableDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                /*if (mAppliancesList != null && mAppliancesList.size() > 0) {
                    PairDevice pairDeviceDetails = getDeviceDetails(mAvailableDevicesList.get(position));
                    if (pairDeviceDetails != null) {
                        mLaunchFragmentPresenter.pairDevice(pairDeviceDetails, PairingFragment.this);
                    }
                }*/
                showProgressDialog(getString(R.string.pairing_device));
                mLaunchFragmentPresenter.pairDevice(getTestDeviceDetails(), PairingFragment.this);
            }
        });

        mPairedDevicesListView = (ListView) view.findViewById(R.id.paired_devices);
        mPairedDevicesListView.setAdapter(mPairedDevicesAdapter);
        mPairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                showProgressDialog(getString(R.string.unpairing_device));
                mLaunchFragmentPresenter.unPairDevice(mPairedDevicesAdapter.getItem(position), PairingFragment.this);
            }
        });

        mDiscoveryManager = DiscoveryManager.getInstance();

        return view;
    }

    private void initializeDiComm() {
        final CloudController cloudController = new DefaultCloudController(getActivity().getApplicationContext(), new SampleKpsConfigurationInfo());
        final LanTransportContext lanTransportContext = new LanTransportContext(getActivity().getApplicationContext());
        final SampleApplianceFactory applianceFactory = new SampleApplianceFactory(lanTransportContext);

        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(getActivity().getApplicationContext(), applianceFactory, null, cloudController);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //For testing
        List<String> test = new ArrayList<>();
        test.add("1c5a6bfffecc9127");
        updateDiscoveredDevices(test);

        if (isPairingFragmentVisible() && !isProgressShowing()) {
            showProgressDialog(getString(R.string.get_paired_device));
            StateContext mStateContext = new StateContext();
            mStateContext.setState(new GetPairedDevicesState(getActivity(), this));
            mStateContext.start();
        }

        mDiscoveryManager.addDiscoveryEventListener(discoveryEventListener);
        mDiscoveryManager.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDiscoveryManager.removeDiscoverEventListener(discoveryEventListener);
        mDiscoveryManager.stop();
    }

    private DiscoveryEventListener discoveryEventListener = new DiscoveryEventListener() {

        @Override
        public void onDiscoveredAppliancesListChanged() {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateDiscoveredDevices(getDiscoveredDevices(mDiscoveryManager.getAllDiscoveredAppliances()));
                }
            });
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //For testing
    private PairDevice getTestDeviceDetails() {
        mPairDevice = new PairDevice();
        mPairDevice.setDeviceID("1c5a6bfffecc9127");
        mPairDevice.setDeviceType("urn:philips-com:device:DiProduct:1");
        return mPairDevice;
    }

    private PairDevice getDeviceDetails(String id) {
        for (int i = 0; i < mAppliancesList.size(); i++) {
            if (mAppliancesList.get(i).getNetworkNode().getCppId().equalsIgnoreCase(id)) {
                mPairDevice = new PairDevice();
                mPairDevice.setDeviceID(mAppliancesList.get(i).getNetworkNode().getCppId());
                mPairDevice.setDeviceType(mAppliancesList.get(i).getDeviceType());
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
        mPairedDevicesList.add(deviceID);
        mPairedDevicesAdapter.add(deviceID);
    }

    public void removeFromPairedDevices(String deviceID) {
        mPairedDevicesList.remove(deviceID);
        mPairedDevicesAdapter.remove(deviceID);
    }

    public void addToAvailableDevices(String deviceID) {
        mAvailableDevicesList.add(deviceID);
        mAvailableDevicesAdapter.add(deviceID);
    }

    public void removeFromAvailableDevices(String deviceID) {
        mAvailableDevicesList.remove(deviceID);
        mAvailableDevicesAdapter.remove(deviceID);
    }

    public List<String> getDiscoveredDevices(ArrayList<? extends Appliance> discoveredAppliances) {
        List<String> devices = new ArrayList<>();

        if (discoveredAppliances.size() == 0) {
            showAlertDialog(getString(R.string.no_appliances_found));
        } else {
            for (int i = 0; i < discoveredAppliances.size(); i++) {
                mAppliancesList.add(discoveredAppliances.get(i));
                devices.add(discoveredAppliances.get(i).getNetworkNode().getCppId());
            }
        }
        return devices;
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
        mLaunchFragmentPresenter.pairDevice(getTestDeviceDetails(), PairingFragment.this);
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
}