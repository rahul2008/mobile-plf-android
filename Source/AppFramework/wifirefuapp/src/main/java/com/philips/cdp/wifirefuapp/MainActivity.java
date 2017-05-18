package com.philips.cdp.wifirefuapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.platform.uappframework.listener.BackEventListener;

import static com.philips.cdp.wifirefuapp.WifiCommLibUappInterface.WELCOME_MESSAGE;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private TextView mTitleTextView;

    private DiscoveryManager<?> discoveryManager;
    private ArrayAdapter<Appliance> applianceAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomActionBar();
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            MainActivityFragment sampleFragment = new MainActivityFragment();
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String message = extras.getString(WELCOME_MESSAGE);
                Bundle bundle = new Bundle();
                bundle.putString(WELCOME_MESSAGE, message);
                sampleFragment.setArguments(bundle);
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.parent_layout, sampleFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }

        setUpDiscoveryManager();
    }

    private void setUpDiscoveryManager() {
        applianceAdapter = new ArrayAdapter<Appliance>(this, android.R.layout.simple_list_item_2, android.R.id.text1) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Appliance appliance = getItem(position);
                ((TextView) view.findViewById(android.R.id.text1)).setText(appliance.getName());
                ((TextView) view.findViewById(android.R.id.text2)).setText(String.format("%s - %s", appliance.getDeviceType(), appliance.getNetworkNode().getCppId()));
                return view;
            }
        };

        final ListView listViewAppliances = (ListView) findViewById(R.id.listViewAppliances);
        listViewAppliances.setAdapter(applianceAdapter);
        listViewAppliances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                CurrentApplianceManager.getInstance().setCurrentAppliance(applianceAdapter.getItem(position));
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
            }
        });

        discoveryManager = DiscoveryManager.getInstance();

        ((TextView) findViewById(R.id.textViewAppId)).setText(DICommClientWrapper.getAppId());
    }

    private void initCustomActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the text view in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null); // layout which contains your button.

        mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);

        final FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        ImageView arrowImage = (ImageView) mCustomView
                .findViewById(R.id.arrow);
//        arrowImage.setBackground(getResources().getDrawable(R.drawable.prodreg_left_arrow));

        mActionBar.setCustomView(mCustomView, params);
        setTitle(getString(R.string.app_name));
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        discoveryManager.addDiscoveryEventListener(discoveryEventListener);
        discoveryManager.start();

        applianceAdapter.clear();
        applianceAdapter.addAll(discoveryManager.getAllDiscoveredAppliances());
    }

    @Override
    protected void onPause() {
        super.onPause();

        discoveryManager.removeDiscoverEventListener(discoveryEventListener);
        discoveryManager.stop();
    }

    private DiscoveryEventListener discoveryEventListener = new DiscoveryEventListener() {

        @Override
        public void onDiscoveredAppliancesListChanged() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    applianceAdapter.clear();
                    applianceAdapter.addAll(discoveryManager.getAllDiscoveredAppliances());
                }
            });

            for (Appliance appliance : discoveryManager.getAllDiscoveredAppliances()) {
                appliance.getWifiPort().addPortListener(wifiPortListener);
            }
        }
    };

    private DICommPortListener<WifiPort> wifiPortListener = new DICommPortListener<WifiPort>() {

        @Override
        public void onPortUpdate(final WifiPort port) {
            Log.d(TAG, "onPortUpdate() called with: " + "port = [" + port + "]");
            WifiPortProperties portProperties = port.getPortProperties();
            if (portProperties != null) {
                Log.d(TAG, String.format("WifiPortProperties: ipaddress=%s", portProperties.getIpaddress()));
            }
        }

        @Override
        public void onPortError(final WifiPort port, final Error error, final String errorData) {
            Log.d(TAG, "onPortError() called with: " + "port = [" + port + "], error = [" + error + "], errorData = [" + errorData + "]");
        }
    };

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean backState = false;
        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.parent_layout);
        if (currentFrag != null && currentFrag instanceof BackEventListener) {
            backState = ((BackEventListener) currentFrag).handleBackEvent();
        }

        if (!backState) {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState, final PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("test", true);
    }

    @Override
    public void setTitle(final CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        if (mTitleTextView != null)
            mTitleTextView.setText(titleId);
        else
            super.setTitle(titleId);
    }

}
