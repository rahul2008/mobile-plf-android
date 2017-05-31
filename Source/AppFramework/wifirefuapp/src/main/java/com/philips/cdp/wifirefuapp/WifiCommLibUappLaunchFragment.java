package com.philips.cdp.wifirefuapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class WifiCommLibUappLaunchFragment extends Fragment implements BackEventListener ,DevicePairingListener{


    public static String TAG = WifiCommLibUappLaunchFragment.class.getSimpleName();
    private FragmentLauncher fragmentLauncher;
    private TextView welcomeTextView;
    private DiscoveryManager<?> discoveryManager;
    private ArrayAdapter<Appliance> applianceAdapter;
    private View view;
    private Activity activity;
    private Button consentButton;
    /*ListView availableDevicesListView;
    AvailableDevicesAdapter availableDevicesAdapter;
    ArrayList<Device> mDeviceAvailableLists = new ArrayList<Device>();

    ListView pairedDevicesListView;
    PairedDevicesAdapter pairedDevicesAdapter;
    ArrayList<Device> mDevicePairedList = new ArrayList<Device>();

    private ProgressDialog mProgressDialog;*/

    @Override
    public void onAttach(Context context) {
        activity = (Activity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
/*
        mProgressDialog =  new ProgressDialog(activity);
        availableDevicesListView = (ListView) view.findViewById(availableDevicesListView);
        mDeviceAvailableLists = getAvailableDevices();
        availableDevicesAdapter = new AvailableDevicesAdapter(activity, mDeviceAvailableLists,WifiCommLibUappLaunchFragment.this); // initialize with available devices
        availableDevicesListView.setAdapter(availableDevicesAdapter);

        pairedDevicesListView = (ListView) view.findViewById(R.id.discoveredDevicesListView);
        pairedDevicesAdapter = new PairedDevicesAdapter(activity, mDevicePairedList, WifiCommLibUappLaunchFragment.this);
        pairedDevicesListView.setAdapter(pairedDevicesAdapter);
*/

        consentButton = (Button) view.findViewById(R.id.consentButton);
        return view;
    }

/*    public void setPairedDevices(Device pairedDevice) {


        dialogToConnect();

        if (!mDevicePairedList.contains(pairedDevice)) {
            mDevicePairedList.add(pairedDevice);
            pairedDevicesAdapter.setData(mDevicePairedList);
        }
    }

    private void dialogToConnect(){
        final String[] status = {"Connecting...","Sending data to backend...","Recieving acknowledgmnet from backend..."};

        for(int iCount=0;iCount<status.length;iCount++){
            final String msg= status[iCount];


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mProgressDialog.setMessage(msg);
                    mProgressDialog.show();


                }
            }, 2000);


        }
        mProgressDialog.dismiss();

    }


    public void setAvailableDevices(Device availableDevice) {

        if (!mDeviceAvailableLists.contains(availableDevice)) {
            mDeviceAvailableLists.add(availableDevice);
            availableDevicesAdapter.setData(mDeviceAvailableLists);
        }
    }

    private ArrayList<Device> getAvailableDevices() {
        ArrayList<Device> availableDevices = new ArrayList<Device>();
        Device device1 = new Device("1", "uGrow");
        Device device2 = new Device("2", "baby Care");
        Device device3 = new Device("3", "Reference device");
        Device device4 = new Device("4", "TP link");
        availableDevices.add(device1);
        availableDevices.add(device2);
        availableDevices.add(device3);
        availableDevices.add(device4);
        return availableDevices;
    }*/
    @Override
    public void onStart() {
        super.onStart();
        fragmentLauncher.getActionbarListener().updateActionBar("Sample", true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpDiscoveryManager();
        discoveryManager.addDiscoveryEventListener(discoveryEventListener);
        discoveryManager.start();

        applianceAdapter.clear();
        applianceAdapter.addAll(discoveryManager.getAllDiscoveredAppliances());
    }

    @Override
    public void onPause() {
        super.onPause();
        discoveryManager.removeDiscoverEventListener(discoveryEventListener);
        discoveryManager.stop();
    }

    private void setUpDiscoveryManager() {
        applianceAdapter = new ArrayAdapter<Appliance>(activity, android.R.layout.simple_list_item_2, android.R.id.text1) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Appliance appliance = getItem(position);
                ((TextView) view.findViewById(android.R.id.text1)).setText(appliance.getName());
                ((TextView) view.findViewById(android.R.id.text2)).setText(String.format("%s - %s", appliance.getDeviceType(), appliance.getNetworkNode().getCppId()));
                return view;
            }
        };

        final ListView listViewAppliances = (ListView) view.findViewById(R.id.listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                CurrentApplianceManager.getInstance().setCurrentAppliance(applianceAdapter.getItem(position));
                getActivity().getSupportFragmentManager().beginTransaction().replace(fragmentLauncher.getParentContainerResourceID(),new WifiCommLinUappWifiDetailFragment(),"WifiDetailFragment").commit();
            }
        });

        discoveryManager = DiscoveryManager.getInstance();

        consentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataServicesManager.getInstance().getPairedDevices(WifiCommLibUappLaunchFragment.this);
            }
        });

        //((TextView) view.findViewById(R.id.textViewAppId)).setText(DICommClientWrapper.getAppId());
    }

    private DiscoveryEventListener discoveryEventListener = new DiscoveryEventListener() {

        @Override
        public void onDiscoveredAppliancesListChanged() {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    applianceAdapter.clear();
                    applianceAdapter.addAll(discoveryManager.getAllDiscoveredAppliances());
                }
            });

            for (Appliance appliance : discoveryManager.getAllDiscoveredAppliances()) {
                appliance.getDevicePort().addPortListener(devicePortListener);
                Map<String, Object> props = new HashMap<>();
                props.put("name", "Manual ProductStub");
                appliance.getDevicePort().putProperties(props);
            }
        }
    };

    private DICommPortListener<DevicePort> devicePortListener = new DICommPortListener<DevicePort>() {

        @Override
        public void onPortUpdate(final DevicePort port) {
            Log.d(TAG, "onPortUpdate() called with: " + "port = [" + ((DevicePort) port).getPortProperties().getName() + "]");

        }

        @Override
        public void onPortError(final DevicePort port, final Error error, final String errorData) {
            Log.d(TAG, "onPortError() called with: " + "port = [" + port + "], error = [" + error + "], errorData = [" + errorData + "]");
        }
    };

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(WifiCommLibUappInterface.WELCOME_MESSAGE);
        }
    }

    @Override
    public void onResponse(boolean b) {
        Log.d(TAG,"::::boolean response : "+b);
    }

    @Override
    public void onError(DataServicesError dataServicesError) {
        Log.d(TAG,"::::Error : "+dataServicesError.getErrorMessage());
    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {
            Log.d(TAG,"::::Size of paired devices : "+list.size());
    }
}