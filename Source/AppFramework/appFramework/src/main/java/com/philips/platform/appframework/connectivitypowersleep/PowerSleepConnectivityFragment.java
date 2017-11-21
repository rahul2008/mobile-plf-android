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
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.registration.User;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.platform.appframework.AbstractConnectivityBaseFragment;
import com.philips.platform.appframework.ConnectivityDeviceType;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.BLEScanDialogFragment;
import com.philips.platform.appframework.connectivity.ConnectivityUtils;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.UIView;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PowerSleepConnectivityFragment extends AbstractConnectivityBaseFragment implements View.OnClickListener, ConnectivityPowerSleepContract.View, UIView, SynchronisationCompleteListener,DBChangeListener {
    public static final String TAG = PowerSleepConnectivityFragment.class.getSimpleName();
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

    private final String BLE_SCAN_DIALOG_TAG = "BleScanDialog";
    private final String SLEEP_PROGRESS_VIEW_PROPERTY = "scoreAngle";
    private final int PROGRESS_SCORE_MAX = 360;
    private final int PROGRESS_PERCENTAGE_MAX = 100;
    private final int IDEAL_DEEP_SLEEP_TIME = 120;
    private final int PROGRESS_DRAW_TIME = 1500;
    private User user;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_DLS_power_sleep_connectivity);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        RALog.d(TAG, "Connectivity Fragment Oncreate");
        super.onCreate(savedInstanceState);
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        connectivityFragmentWeakReference = new WeakReference<PowerSleepConnectivityFragment>(this);
        user=new User(getActivity());
        if(user.isUserSignIn()) {
            DataServicesManager.getInstance().registerSynchronisationCompleteListener(this);
            DataServicesManager.getInstance().synchronize();
        }else{
            showToast("Please sign in!");
        }
        mBluetoothAdapter = getBluetoothAdapter();
    }


    private View view;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateScreenWithLatestDataAvailable();
    }

    public void populateScreenWithLatestDataAvailable() {
        connectivityPresenter.getLatestPowerSleepSession(new DBFetchRequestListner<Moment>() {
            @Override
            public void onFetchSuccess(final List<? extends Moment> list) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(list!=null && list.size()>0) {
                            Moment moment=list.get(0);
                            Summary summary=getSummaryInfoFromMoment(moment);
                            updateSessionData(summary.getTotalSleepTime(),0,summary.getDeepSleepTime(),summary.getDate().getTime());
                        }else{
                            showToast("Data not available.");
                        }
                    }
                });

            }

            @Override
            public void onFetchFailure(final Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Error while fetching data from power sleep device. Error::"+e.getMessage());
                    }
                });
            }
        });
    }

    protected Summary getSummaryInfoFromMoment(Moment moment){
        Summary summary=null;
        try {
            ArrayList<? extends MeasurementGroup> measurementGroupParent = new ArrayList<>(moment.getMeasurementGroups());
            ArrayList<? extends MeasurementGroup> measurementGroupChild = new ArrayList<>(measurementGroupParent.get(0).getMeasurementGroups());
            ArrayList<? extends Measurement> measurements = new ArrayList<>(measurementGroupChild.get(0).getMeasurements());
            String deepSleepTime = null,totalSleepTime = null;

            for(int count=0;count<measurements.size();count++){
                switch (count){
                    case 0:
                       deepSleepTime=measurements.get(0).getValue();
                        break;
                    case 1:
                        totalSleepTime=measurements.get(1).getValue();
                        break;
                }
            }
            Float dstFloat=Float.parseFloat(deepSleepTime);
            long dst=dstFloat.longValue();

            Float tstFloat=Float.parseFloat(totalSleepTime);
            long tst=tstFloat.longValue();

            summary=new Summary(new Date(moment.getDateTime().getMillis()),dst,tst);
            return summary;
        } catch (ArrayIndexOutOfBoundsException e) {
            RALog.e(TAG,"Error while converting moment data to summary"+e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            RALog.e(TAG,"Error while converting moment data to summary"+e.getMessage());
        } catch (Exception e) {
            RALog.e(TAG,"Error while converting moment data to summary"+e.getMessage());
        }
        return summary;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        connectivityPresenter = getConnectivityPresenter();
        view = inflater.inflate(R.layout.overview_power_sleep, container, false);
        sleepScoreProgressView = (SleepScoreProgressView) view.findViewById(R.id.arc_progress);
        sleepTime = (TextView) view.findViewById(R.id.sleep_time_value);
        deepSleepTime = (TextView) view.findViewById(R.id.deep_sleep_time_value);
        insights = (Button) view.findViewById(R.id.insights);
        sleepPercentageScore = (TextView) view.findViewById(R.id.sleepoverview_score);
        syncUpdated = (TextView) view.findViewById(R.id.powersleep_updated);
        view.findViewById(R.id.powersleep_sync).setOnClickListener(this);
        insights.setOnClickListener(this);
        insights.setEnabled(true);
        insights.setAlpha(0.5f);
        mCommCentral = getCommCentral(ConnectivityDeviceType.POWER_SLEEP);
        setHasOptionsMenu(true);
        startAppTagging(TAG);
        return view;
    }

    protected PowerSleepConnectivityPresenter getConnectivityPresenter() {
        return new PowerSleepConnectivityPresenter(getActivity(), this, this);
    }

    @Override
    public void onClick(final View v) {
        ConnectivityUtils.hideSoftKeyboard(getActivity());
        switch (v.getId()) {
            case R.id.powersleep_sync:
                launchBluetoothActivity();
                break;
            case R.id.insights:
                connectivityPresenter.onEvent(R.id.insights);
                break;
        }
    }

    private BleReferenceAppliance bleReferenceAppliance = null;

    /**
     * Start scanning nearby devices using given strategy
     */
    protected void startDiscovery() {

        if (bleReferenceAppliance != null) {
            connectivityPresenter.synchroniseSessionData(bleReferenceAppliance);
            return;
        }

        RALog.i(TAG, "Ble device discovery started ");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFragmentLive()) {
                    try {
                        bleScanDialogFragment = new BLEScanDialogFragment();
                        if(mCommCentral.getApplianceManager()!=null) {
                            bleScanDialogFragment.setSavedApplianceList(mCommCentral.getApplianceManager().getAvailableAppliances());
                        }
                        bleScanDialogFragment.show(getActivity().getSupportFragmentManager(), BLE_SCAN_DIALOG_TAG);
                        bleScanDialogFragment.setCancelable(false);
                        bleScanDialogFragment.setBLEDialogListener(new BLEScanDialogFragment.BLEScanDialogListener() {
                            @Override
                            public void onDeviceSelected(final BleReferenceAppliance bleRefAppliance) {
                                bleReferenceAppliance = bleRefAppliance;
                                connectivityPresenter.synchroniseSessionData(bleRefAppliance);
                            }
                        });

                        mCommCentral.startDiscovery();
                        handler.postDelayed(stopDiscoveryRunnable, STOP_DISCOVERY_TIMEOUT);
                    } catch (MissingPermissionException e) {
                        RALog.e(TAG, "Permission missing");
                    }
                }
            }
        }, START_DISCOVERY_TIME);
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
    public void updateSessionData(final long sleepTime, final long numberOfInteruptions, final long deepSleepTime, final long timeStamp) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        RALog.d(TAG, "Session data updated");
        insights.setEnabled(true);
        insights.setAlpha(1.0f);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PowerSleepConnectivityFragment.this.sleepTime.setText(getString(R.string.label_sleep_time_format, TimeUnit.MILLISECONDS.toMinutes(sleepTime)));
                PowerSleepConnectivityFragment.this.deepSleepTime.setText(getString(R.string.label_sleep_time_format, TimeUnit.MILLISECONDS.toMinutes(deepSleepTime)));
                setLastSyncDate();
                setSleepProgressPercentage(TimeUnit.MILLISECONDS.toMinutes(deepSleepTime));
            }
        });

    }

    @Override
    public void showError(Error error, String s) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        RALog.d(TAG, "Device Error : " + error.getErrorMessage() + ", " + s);
        Toast.makeText(getContext(), getString(R.string.RA_DLS_data_fetch_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog() {
        dialog = ProgressDialog.show(mContext, "", getString(R.string.RA_DLS_data_fetch_wait));
        dialog.setCancelable(false);
    }

    @Override
    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private void setLastSyncDate() {
        SimpleDateFormat syncFormat = new SimpleDateFormat();
        syncUpdated.setText(getString(R.string.label_last_synced, syncFormat.format(new Date(System.currentTimeMillis()))));
    }

    private void setSleepProgressPercentage(long targetScore) {
        int percentage = calculatePercentage(targetScore);
        this.sleepPercentageScore.setText(String.valueOf(percentage));
        targetScore = (PROGRESS_SCORE_MAX * percentage) / PROGRESS_PERCENTAGE_MAX;
        final ObjectAnimator scoreAnim = ObjectAnimator.ofFloat(sleepScoreProgressView, SLEEP_PROGRESS_VIEW_PROPERTY, 0, targetScore);
        scoreAnim.setInterpolator(new AccelerateInterpolator());
        scoreAnim.setDuration(PROGRESS_DRAW_TIME);
        scoreAnim.start();
    }

    private int calculatePercentage(long targetScore) {
        return (int) (targetScore * PROGRESS_PERCENTAGE_MAX) / IDEAL_DEEP_SLEEP_TIME;
    }

    @Override
    public void onDestroy() {
        RALog.d(TAG, " Connectivity Fragment Destroyed");
        connectivityFragmentWeakReference = null;
        user=null;
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        //ConnectivityUtils.hideSoftKeyboard(getActivity());
        removeApplianceListener();
        if (handler != null) {
            handler.removeCallbacks(stopDiscoveryRunnable);
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
    }

    protected void removeApplianceListener() {
        if (mCommCentral != null && mCommCentral.getApplianceManager() != null) {
            mCommCentral.getApplianceManager().removeApplianceListener(this.applianceListener);
        }
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public void onSyncComplete() {
        populateScreenWithLatestDataAvailable();
    }

    @Override
    public void onSyncFailed(Exception e) {
    }

    @Override
    public void dBChangeSuccess(SyncType syncType) {
        DataServicesManager.getInstance().synchronize();
        populateScreenWithLatestDataAvailable();
    }

    @Override
    public void dBChangeFailed(Exception e) {

    }
}
