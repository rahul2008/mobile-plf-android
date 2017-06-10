package com.philips.cdp.wifirefuapp.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.wifirefuapp.R;
import com.philips.cdp.wifirefuapp.devicesetup.SampleApplianceFactory;
import com.philips.cdp.wifirefuapp.devicesetup.SampleKpsConfigurationInfo;
import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UappLaunchFragment extends Fragment implements BackEventListener ,DevicePairingListener,SubjectProfileListener,FragmentViewListener{


    public static String TAG = UappLaunchFragment.class.getSimpleName();
    private FragmentLauncher fragmentLauncher;
    private TextView welcomeTextView;
    private DiscoveryManager<?> discoveryManager;
    private ArrayAdapter<Appliance> applianceAdapter;
    private View view;
    private Activity activity;
    private Button consentButton,fetchPairedDevices;
    private CommCentral commCentral;
    private UappLaunchFragmentPresenter uappLaunchFragmentPresenter;
    private PairDevicePojo pairDevicePojo;

    @Override
    public void onAttach(Context context) {
        activity = (Activity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        consentButton = (Button) view.findViewById(R.id.consentButton);
        fetchPairedDevices = (Button) view.findViewById(R.id.fetchListOfPairedDevices);
        fetchPairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DataServicesManager.getInstance().getPairedDevices(UappLaunchFragment.this);
                //DataServicesManager.getInstance().createSubjectProfile("Fwuser","2013-05-05","Male",3.456,"2015-10-01T12:11:10.123+0100",UappLaunchFragment.this);
                //DataServicesManager.getInstance().getSubjectProfiles(UappLaunchFragment.this);
                //DataServicesManager.getInstance().deleteSubjectProfile("12a1a43a-68c7-4a10-90b3-1223259fff7a",UappLaunchFragment.this);
                //getActivity().getSupportFragmentManager().beginTransaction().add(new ConsentDialogFragment(), "ConsentFragmentUApp").commit();
            }
        });
        consentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getActivity().getSupportFragmentManager().beginTransaction().replace(fragmentLauncher.getParentContainerResourceID(),new ConsentDialogFragment(),"WiFiConsentFragment").commit();
                uappLaunchFragmentPresenter.onPairDevice(getPairPojo());
            }
        });
        initiliazeDiComm();
        return view;
    }
    private void initiliazeDiComm() {
        final CloudController cloudController = setupCloudController();
        final LanTransportContext lanTransportContext = new LanTransportContext(getActivity().getApplicationContext());
        final SampleApplianceFactory applianceFactory = new SampleApplianceFactory(lanTransportContext);
        this.commCentral = new CommCentral(applianceFactory, lanTransportContext);
        if(DiscoveryManager.getInstance() == null){
            DICommClientWrapper.initializeDICommLibrary(getActivity().getApplicationContext(), applianceFactory, null, cloudController);
        }

    }

    @NonNull
    private CloudController setupCloudController() {
        final CloudController cloudController = new DefaultCloudController(getActivity().getApplicationContext(), new SampleKpsConfigurationInfo());

        String ICPClientVersion = cloudController.getICPClientVersion();
        DICommLog.i(DICommLog.ICPCLIENT, "ICPClientVersion :" + ICPClientVersion);

        return cloudController;
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentLauncher.getActionbarListener().updateActionBar("Sample", true);
    }

    @Override
    public void onResume() {
        super.onResume();
        uappLaunchFragmentPresenter = new UappLaunchFragmentPresenter(UappLaunchFragment.this);
        setUpDiscoveryManager();
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
                //CurrentApplianceManager.getInstance().setCurrentAppliance(applianceAdapter.getItem(position));
                uappLaunchFragmentPresenter.onPairDevice(getPairDevicePojo(discoveryManager.getAllDiscoveredAppliances().get(position)));
            }
        });

        discoveryManager = DiscoveryManager.getInstance();
        discoveryManager.addDiscoveryEventListener(discoveryEventListener);
        discoveryManager.start();

        applianceAdapter.clear();
        applianceAdapter.addAll(discoveryManager.getAllDiscoveredAppliances());

    }

    //TODO : Remove, done to test the device pair as discovery is not working
    private PairDevicePojo getPairPojo(){
        pairDevicePojo = new PairDevicePojo();
        pairDevicePojo.setDeviceID("1c5a6bfffecc9128");
        pairDevicePojo.setDeviceType("Manual ProductStub");
        return pairDevicePojo;
    }
    private PairDevicePojo getPairDevicePojo(Appliance appliance) {
        pairDevicePojo = new PairDevicePojo();
        pairDevicePojo.setDeviceID(appliance.getNetworkNode().getCppId());
        pairDevicePojo.setDeviceType(appliance.getDeviceType());
        return pairDevicePojo;
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
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> list) {
        Log.d(TAG,"::::Subject profile list response : "+list.get(list.size()-1).getGuid());
    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {
            Log.d(TAG,"::::Size of paired devices : "+list.size());
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }
}