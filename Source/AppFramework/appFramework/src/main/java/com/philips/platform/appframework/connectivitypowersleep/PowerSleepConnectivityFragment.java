/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;


import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.platform.appframework.ConnectivityBaseFragment;
import com.philips.platform.appframework.ConnectivityDeviceType;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.BLEScanDialogFragment;
import com.philips.platform.appframework.connectivity.ConnectivityFragment;
import com.philips.platform.appframework.connectivity.ConnectivityUtils;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.philips.platform.baseapp.screens.utility.Constants.ILLEGAL_STATE_EXCEPTION;

public class PowerSleepConnectivityFragment extends ConnectivityBaseFragment implements View.OnClickListener, ConnectivityPowerSleepContract.View {
    public static final String TAG = ConnectivityFragment.class.getSimpleName();
    private ProgressDialog dialog = null;
    private Handler handler = new Handler();
    private WeakReference<PowerSleepConnectivityFragment> connectivityFragmentWeakReference;
    private Context mContext;
    private SleepScoreProgressView sleepScoreProgressView;
    private TextView sleepTime, deepSleepTime, sleepPercentageScore, syncUpdated;

    private Button insights;

    /**
     * Presenter object for Connectivity
     */
    private PowerSleepConnectivityPresenter connectivityPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_ConnectivityScreen_Menu_Title);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        RALog.d(TAG, "Connectivity Fragment Oncreate");
        super.onCreate(savedInstanceState);
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        connectivityFragmentWeakReference = new WeakReference<PowerSleepConnectivityFragment>(this);

        mBluetoothAdapter = getBluetoothAdapter();

    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        connectivityPresenter = new PowerSleepConnectivityPresenter(this);
        view = inflater.inflate(R.layout.overview_power_sleep, container, false);
        sleepScoreProgressView = (SleepScoreProgressView) view.findViewById(R.id.arc_progress);
        sleepTime = (TextView) view.findViewById(R.id.sleep_time_value);
        deepSleepTime = (TextView) view.findViewById(R.id.deep_sleep_time_value);
        insights = (Button) view.findViewById(R.id.insights);
        sleepPercentageScore = (TextView) view.findViewById(R.id.sleepoverview_score);
        syncUpdated = (TextView) view.findViewById(R.id.powersleep_updated);
        view.findViewById(R.id.powersleep_sync).setOnClickListener(this);
        insights.setOnClickListener(this);
        insights.setEnabled(false);
        insights.setAlpha(0.5f);
        mCommCentral = getCommCentral(ConnectivityDeviceType.POWER_SLEEP);
        setHasOptionsMenu(true);
        startAppTagging(TAG);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateActionBar();
    }

    protected void updateActionBar() {
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
    }


    @Override
    public void onClick(final View v) {
        ConnectivityUtils.hideSoftKeyboard(getActivity());
        switch (v.getId()) {
            case R.id.powersleep_sync:
                launchBlutoothActivity();
                break;
            case R.id.insights:
                //replaceFragment(new InsightsFragment(), InsightsFragment.TAG);
                break;
            default:
        }
    }

    private void replaceFragment(Fragment fragment, String tag) {

        int containerId = -1;
        if (getActivity() instanceof AbstractAppFrameworkBaseActivity) {
            containerId = ((AbstractAppFrameworkBaseActivity) getActivity()).getContainerId();
        }

        try {
            if (containerId == -1) return;
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            RALog.e(TAG + ILLEGAL_STATE_EXCEPTION, e.getMessage());
        }

    }

    private BleReferenceAppliance bleReferenceAppliance = null;

    /**
     * Start scanning nearby devices using given strategy
     */
    protected void startDiscovery() {

        if (bleReferenceAppliance != null) {
            fetchData(bleReferenceAppliance);
            return;
        }

        RALog.i(TAG, "Ble device discovery started ");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFragmentLive()) {
                    try {
                        bleScanDialogFragment = new BLEScanDialogFragment();
                        bleScanDialogFragment.setSavedApplianceList(mCommCentral.getApplianceManager().getAvailableAppliances());
                        bleScanDialogFragment.show(getActivity().getSupportFragmentManager(), "BleScanDialog");
                        bleScanDialogFragment.setBLEDialogListener(new BLEScanDialogFragment.BLEScanDialogListener() {
                            @Override
                            public void onDeviceSelected(final BleReferenceAppliance bleRefAppliance) {
                                bleReferenceAppliance = bleRefAppliance;
                                connectivityPresenter.setUpApplicance(bleRefAppliance);

                                fetchData(bleReferenceAppliance);
                            }
                        });

                        mCommCentral.startDiscovery();
                        handler.postDelayed(stopDiscoveryRunnable, 30000);
                    } catch (MissingPermissionException e) {
                        RALog.e(TAG, "Permission missing");
                    }
                }
            }
        }, 100);

    }

    private void fetchData(BleReferenceAppliance bleRefAppliance) {
        dialog = ProgressDialog.show(mContext, "", "Wait..While we fetch your sleep data from device ");
        dialog.setCancelable(true);
        bleRefAppliance.getSensorDataPort().reloadProperties();
        bleRefAppliance.getSessionDataPort().reloadProperties();
        bleRefAppliance.getSessionInfoPort().reloadProperties();
    }

    /**
     * Check if fragment is live
     *
     * @return
     */
    protected boolean isFragmentLive() {
        return connectivityFragmentWeakReference != null && isAdded();
    }

    @Override
    public void updateSessionData(long sleepTime, long numberOfInteruptions, long deepSleepTime) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        RALog.d(TAG, "Session data updated");
        insights.setEnabled(true);
        insights.setAlpha(1.0f);
        this.sleepTime.setText(TimeUnit.MILLISECONDS.toMinutes(sleepTime) + " mins");
        this.deepSleepTime.setText(TimeUnit.MILLISECONDS.toMinutes(deepSleepTime) + " mins");
        setLastSyncDate();
        setSleepProgressPercentage(TimeUnit.MILLISECONDS.toMinutes(deepSleepTime));
    }

    @Override
    public void showError(Error error, String s) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        RALog.d(TAG, "Device Error : " + s);
        Toast.makeText(getContext(), "Not able to fetch data", Toast.LENGTH_LONG).show();
    }

    private void setLastSyncDate() {
        SimpleDateFormat syncFormat = new SimpleDateFormat("MMM d, hh:mm a", Locale.ENGLISH);
        syncUpdated.setText(getString(R.string.label_last_synced, syncFormat.format(new Date(System.currentTimeMillis()))));
    }

    private void setSleepProgressPercentage(long targetScore) {
        int percentage = calculatePercentage(targetScore);
        this.sleepPercentageScore.setText(String.valueOf(percentage));
        targetScore = (360 * percentage) / 100;
        final ObjectAnimator scoreAnim = ObjectAnimator.ofFloat(sleepScoreProgressView, "scoreAngle", 0, targetScore);
        scoreAnim.setInterpolator(new AccelerateInterpolator());
        scoreAnim.setDuration(1500);
        scoreAnim.start();
    }

    private int calculatePercentage(long targetScore) {
        return (int) (targetScore * 100) / 120;
    }

    @Override
    public void onDestroy() {
        RALog.d(TAG, " Connectivity Fragment Destroyed");
        connectivityFragmentWeakReference = null;
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        //ConnectivityUtils.hideSoftKeyboard(getActivity());
        mCommCentral.getApplianceManager().removeApplianceListener(this.applianceListener);
        connectivityPresenter.removeSessionPortListener(bleReferenceAppliance);
        if (handler != null) {
            handler.removeCallbacks(stopDiscoveryRunnable);
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
    }

}
