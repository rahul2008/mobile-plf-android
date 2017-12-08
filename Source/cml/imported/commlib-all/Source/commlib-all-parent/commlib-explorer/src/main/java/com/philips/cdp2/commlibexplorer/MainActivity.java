/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlibexplorer.appliance.GenericAppliance;
import com.philips.cdp2.commlibexplorer.background.BackgroundConnectionService;
import com.philips.cdp2.commlibexplorer.fragments.StrategyChoiceFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;
    private Runnable permissionCallback;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: service connected to main activity");
            MainActivity.this.service = ((BackgroundConnectionService.BackgroundBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: service disconnected from main activity");
            service = null;
        }
    };
    private BackgroundConnectionService service;
    private boolean isBound;
    private GenericAppliance currentAppliance = null;
    private DICommPort currentPort = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment f = StrategyChoiceFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        doUnbindService();
        currentAppliance = null;
    }

    private void doUnbindService() {
        if (isBound) {
            Log.d(TAG, "doUnbindService");
            unbindService(mConnection);
            isBound = false;
        }
    }

    private void doBindService() {
        Log.d(TAG, "doBindService");
        Intent serviceIntent = new Intent(this, BackgroundConnectionService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    public void navigateTo(Fragment destination) {
        String classTag = destination.getClass().getName();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, destination, classTag);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == ACCESS_COARSE_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Handler().post(this.permissionCallback);
            } else {
                Log.wtf(TAG, "onRequestPermissionsResult: permission problem");
            }
        }
    }

    public void acquirePermission(@NonNull Runnable permissionCallback) {
        this.permissionCallback = permissionCallback;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        int stackSize = getSupportFragmentManager().getBackStackEntryCount();
        if (stackSize > 1) {
            super.onBackPressed();
            return;
        }
        finish();
    }

    public BackgroundConnectionService getBoundService() {
        return service;
    }

    // TODO Replace with CommCentral.getApplianceManager().storeAppliance() when it is availabe
    @Deprecated
    public void storeAppliance(GenericAppliance appliance) {
        currentAppliance = appliance;
    }

    // TODO Replace with CommCentral.getApplianceManager().getAvailableAppliances() when it is availabe
    @Deprecated
    public GenericAppliance getAppliance() {
        return currentAppliance;
    }

    public void storePort(DICommPort port) {
        currentPort = port;
    }

    public DICommPort getPort() {
        return currentPort;
    }
}
