/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.lan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.VerboseRunnable;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

public class NetworkMonitor {

    public interface NetworkChangedListener {
        void onNetworkChanged(NetworkState networkState, String networkSsid);
    }

    public enum NetworkState {
        MOBILE,
        WIFI_WITH_INTERNET,
        NONE
    }

    private final Context context;
    private final Executor executor;
    private ConnectivityManager connectivityManager;

    private final WifiManager wifiManager;
    private final Set<NetworkChangedListener> networkChangedListeners = new CopyOnWriteArraySet<>();

    private final BroadcastReceiver networkChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DICommLog.d(DICommLog.NETWORKMONITOR, "onReceive connectivity action : " + intent.getAction());
            updateNetworkStateAsync();
        }
    };

    private NetworkState lastKnownState = NetworkState.NONE;
    private String lastKnownSsid = null;

    public NetworkMonitor(Context context, Executor executor) {
        this.context = context;
        this.executor = executor;

        connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) this.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    private void notifyListener(NetworkState state, String ssid) {
        DICommLog.v(DICommLog.NETWORKMONITOR, "Updating listener");
        for (NetworkChangedListener listener : networkChangedListeners) {
            listener.onNetworkChanged(state, ssid);
        }
    }

    public boolean addListener(NetworkChangedListener listener) {
        return networkChangedListeners.add(listener);
    }

    public boolean removeListener(NetworkChangedListener listener) {
        return networkChangedListeners.remove(listener);
    }

    public void startNetworkChangedReceiver() {
        // Start connectivity changed receiver
        IntentFilter filter = createIntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkChangedReceiver, filter);

        updateNetworkStateAsync();
    }

    @VisibleForTesting
    @NonNull
    protected IntentFilter createIntentFilter() {
        return new IntentFilter();
    }

    public void stopNetworkChangedReceiver() {
        try {
            context.unregisterReceiver(networkChangedReceiver);
        } catch (IllegalArgumentException e) {
            DICommLog.e(DICommLog.NETWORKMONITOR, "Error: " + e.getMessage());
        }
    }

    private void updateNetworkStateAsync() {
        executor.execute(new VerboseRunnable(new Runnable() {
            @Override
            public void run() {
                loadNetworkStateSynchronous();
            }
        }));
    }

    private void loadNetworkStateSynchronous() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            DICommLog.d(DICommLog.NETWORKMONITOR, "Network update - No connection");
            updateNetworkState(NetworkState.NONE, null);
            return;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        boolean isMobileData = activeNetwork.getType() != ConnectivityManager.TYPE_WIFI;
        if (isMobileData) {
            if (wifiInfo == null || wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
                // Assume internet access - don't waste data bandwidth
                DICommLog.d(DICommLog.NETWORKMONITOR, "Network update - Mobile connection");
                updateNetworkState(NetworkState.MOBILE, null);
                return;
            }
        }

        String ssid = getCurrentSsid(wifiInfo);
        // Assume internet access - checking for internet technically difficult (slow DNS timeout)
        DICommLog.d(DICommLog.NETWORKMONITOR, "Network update - Wifi with internet (" + (ssid == null ? "< unknown >" : ssid) + ")");
        updateNetworkState(NetworkState.WIFI_WITH_INTERNET, ssid);
    }

    private String getCurrentSsid(final WifiInfo wifiInfo) {
        String ssid = null;
        if (wifiInfo != null) {
            ssid = wifiInfo.getSSID();
        }

        if (ssid == null || ssid.isEmpty()) {
            return null;
        }
        return ssid;
    }

    /*
     * Synchronized to make mLastKnowState Threadsafe
     */
    public synchronized NetworkState getLastKnownNetworkState() {
        return lastKnownState;
    }

    public synchronized String getLastKnownNetworkSsid() {
        return lastKnownSsid;
    }

    private synchronized void updateNetworkState(NetworkState newState, String newSsid) {
        if (lastKnownState == newState && isLastKnowSsid(newSsid)) {
            DICommLog.d(DICommLog.NETWORKMONITOR, "Detected same networkState - no need to update listener");
            return;
        }

        if (lastKnownState == newState && !isLastKnowSsid(newSsid)) {
            DICommLog.d(DICommLog.NETWORKMONITOR, "Detected rapid change of Wifi networks - sending intermediate disconnect event");
            notifyListener(NetworkState.NONE, null);
        }

        if (lastKnownState == NetworkState.MOBILE && newState == NetworkState.WIFI_WITH_INTERNET
                || lastKnownState == NetworkState.WIFI_WITH_INTERNET && newState == NetworkState.MOBILE) {
            DICommLog.d(DICommLog.NETWORKMONITOR, "Detected rapid change between wifi and data - sending intermediate disconnect event");
            notifyListener(NetworkState.NONE, null);
        }

        DICommLog.d(DICommLog.NETWORKMONITOR, "NetworkState Changed");
        lastKnownState = newState;
        lastKnownSsid = newSsid;
        notifyListener(newState, newSsid);
    }

    private boolean isLastKnowSsid(String ssid) {
        return ssid == null && lastKnownSsid == null || ssid != null && ssid.equals(lastKnownSsid);
    }
}
