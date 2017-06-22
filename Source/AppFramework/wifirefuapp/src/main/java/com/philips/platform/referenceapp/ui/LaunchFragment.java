/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.referenceapp.ui;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.philips.platform.referenceapp.R;
import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.platform.referenceapp.devicesetup.SampleApplianceFactory;
import com.philips.platform.referenceapp.devicesetup.SampleKpsConfigurationInfo;
import com.philips.platform.referenceapp.pojo.PairDevice;
import com.philips.platform.referenceapp.states.GetPairedDevicesState;
import com.philips.platform.referenceapp.states.StateContext;
import com.philips.platform.referenceapp.uappdependencies.WifiCommLibUappInterface;
import com.philips.platform.referenceapp.utils.NetworkChangeListener;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;
import java.util.List;

public class LaunchFragment extends Fragment implements BackEventListener, LaunchFragmentViewListener,
        DeviceStatusListener, NetworkChangeListener.INetworkChangeListener {
    public static String TAG = LaunchFragment.class.getSimpleName();

    private Context mContext;
    private FragmentLauncher mFragmentLauncher;
    private LaunchFragmentPresenter mLaunchFragmentPresenter;

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

    private AlertDialog.Builder mAlertDialog;
    private StateContext mStateContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDiComm();

        mNetworkChangeListener = new NetworkChangeListener();

        mStateContext = new StateContext();
        mStateContext.setState(new GetPairedDevicesState(mFragmentLauncher, this));
        mStateContext.start();
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mLaunchFragmentPresenter = new LaunchFragmentPresenter(LaunchFragment.this);
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
                mLaunchFragmentPresenter.pairDevice(getDeviceDetails(mAppliancesList.get(position)), LaunchFragment.this);
//                mLaunchFragmentPresenter.pairDevice(getTestDeviceDetails(), LaunchFragment.this);
            }
        });

        mPairedDevicesListView = (ListView) view.findViewById(R.id.paired_devices);
        mPairedDevicesListView.setAdapter(mPairedDevicesAdapter);
        mPairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                mLaunchFragmentPresenter.unPairDevice(mPairedDevicesAdapter.getItem(position), LaunchFragment.this);
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
    public void onStart() {
        super.onStart();
        mFragmentLauncher.getActionbarListener().updateActionBar("Device Pairing", true);
    }

    @Override
    public void onResume() {
        super.onResume();

        //For testing
        /*List<String> test = new ArrayList<>();
        test.add("1c5a6bfffecc9127");
        updateDiscoveredDevices(test);*/

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

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.mFragmentLauncher = fragmentLauncher;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(WifiCommLibUappInterface.WELCOME_MESSAGE);
            System.out.print(message);
        }
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public FragmentLauncher getFragmentLauncher() {
        return mFragmentLauncher;
    }

    //For testing
    /*private PairDevice getTestDeviceDetails() {
        mPairDevice = new PairDevice();
        mPairDevice.setDeviceID("1c5a6bfffecc9127");
        mPairDevice.setDeviceType("urn:philips-com:device:DiProduct:1");
        return mPairDevice;
    }*/

    private PairDevice getDeviceDetails(Appliance appliance) {
        mPairDevice = new PairDevice();
        mPairDevice.setDeviceID(appliance.getNetworkNode().getCppId());
        mPairDevice.setDeviceType(appliance.getDeviceType());
        return mPairDevice;
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
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mContext, R.style.alertDialogStyle);
            mAlertDialog.setCancelable(false);
            mAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        mAlertDialog.setMessage(message);
        AlertDialog alert = mAlertDialog.create();
        alert.show();
    }

    @Override
    public void onConnectionLost() {
        mStateContext.getState().dismissProgressDialog();
        showAlertDialog("Please check your connection and try again.");
    }

    @Override
    public void onConnectionAvailable() {
        Toast.makeText(mContext, "Connection is back", Toast.LENGTH_SHORT).show();
    }
}