/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.platform.dprdemo.R;
import com.philips.platform.dprdemo.devicesetup.SampleApplianceFactory;
import com.philips.platform.dprdemo.devicesetup.SampleKpsConfigurationInfo;
import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.states.GetPairedDevicesState;
import com.philips.platform.dprdemo.states.StateContext;
import com.philips.platform.dprdemo.utils.NetworkChangeListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;
import java.util.List;

public class PairingFragment extends Fragment implements BackEventListener,
        DeviceStatusListener, NetworkChangeListener.INetworkChangeListener {
    public static String TAG = PairingFragment.class.getSimpleName();

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

    private NetworkChangeListener mNetworkChangeListener;

    private AlertDialog.Builder mAlertDialogBuilder;
    private AlertDialog mAlertDialog;
    private StateContext mStateContext;
    private Button mBtnLogout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDiComm();

        mNetworkChangeListener = new NetworkChangeListener();
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pairing_layout, container, false);

        mBtnLogout = (Button) view.findViewById(R.id.btn_logout);
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
                mLaunchFragmentPresenter.pairDevice(getTestDeviceDetails(), PairingFragment.this);
            }
        });

        mPairedDevicesListView = (ListView) view.findViewById(R.id.paired_devices);
        mPairedDevicesListView.setAdapter(mPairedDevicesAdapter);
        mPairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                mLaunchFragmentPresenter.unPairDevice(mPairedDevicesAdapter.getItem(position), PairingFragment.this);
            }
        });

        mDiscoveryManager = DiscoveryManager.getInstance();

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStateContext.getState().showProgressDialog("Please wait");
                mBtnLogout.setEnabled(false);

                User user = new User(getActivity());
                if (!user.isUserSignIn()) return;

                user.logout(new LogoutHandler() {
                    @Override
                    public void onLogoutSuccess() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getApplicationContext(), "Logout Success", Toast.LENGTH_SHORT).show();
                                mStateContext.getState().dismissProgressDialog();
                                getActivity().finish();
                            }
                        });
                    }

                    @Override
                    public void onLogoutFailure(int i, String s) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getApplicationContext(), "Logout Failed", Toast.LENGTH_SHORT).show();
                                mStateContext.getState().dismissProgressDialog();
                                mBtnLogout.setEnabled(true);
                            }
                        });
                    }
                });
            }
        });

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
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        getActivity().setTitle("Pair Device");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        //For testing
        List<String> test = new ArrayList<>();
        test.add("1c5a6bfffecc9127");
        updateDiscoveredDevices(test);

        mStateContext = new StateContext();
        mStateContext.setState(new GetPairedDevicesState(getActivity(), this));
        mStateContext.start();

        mNetworkChangeListener.addListener(this);
        mContext.registerReceiver(mNetworkChangeListener, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        mDiscoveryManager.addDiscoveryEventListener(discoveryEventListener);
        mDiscoveryManager.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mNetworkChangeListener.removeListener(this);
        mContext.unregisterReceiver(mNetworkChangeListener);

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
    public boolean handleBackEvent() {
        return false;
    }

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
            showAlertDialog("No Appliances Found");
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
        updatePairedDevices(pairedDeviceList);
    }

    @Override
    public void onDevicePaired(String pairedDeviceID) {
        addToPairedDevices(pairedDeviceID);
        removeFromAvailableDevices(pairedDeviceID);
        showAlertDialog("Device Paired Successfully");
    }

    @Override
    public void onDeviceUnPaired(String unPairedDeviceID) {
        removeFromPairedDevices(unPairedDeviceID);
        if (mDiscoveredDevices != null && mDiscoveredDevices.size() > 0)
            addToAvailableDevices(unPairedDeviceID);
        showAlertDialog("Device UnPaired Successfully");
    }

    @Override
    public void onError(String errorMessage) {
        showAlertDialog(errorMessage);
    }

    @Override
    public void onInternetError() {
        showAlertDialog("Please check your connection and try again.");
    }

    public void showAlertDialog(String message) {
        if (mAlertDialogBuilder == null) {
            mAlertDialogBuilder = new AlertDialog.Builder(mContext, R.style.alertDialogStyle);
            mAlertDialogBuilder.setCancelable(false);
            mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }

        if (mAlertDialog == null)
            mAlertDialog = mAlertDialogBuilder.create();

        if (!mAlertDialog.isShowing() && !(getActivity().isFinishing())) {
            mAlertDialog.setMessage(message);
            mAlertDialog.show();
        }
    }

    @Override
    public void onConnectionLost() {
        mStateContext.getState().dismissProgressDialog();
        showAlertDialog("Please check your connection and try again.");
    }

    @Override
    public void onConnectionAvailable() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStateContext.getState().clearProgressDialog();
    }
}