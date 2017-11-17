package com.philips.platform.appframework.connectivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.DeviceMeasurementPort;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivity.models.Measurement;
import com.philips.platform.appframework.connectivity.models.MomentDetail;
import com.philips.platform.appframework.connectivity.models.UserMoment;
import com.philips.platform.appframework.connectivity.network.GetMomentRequest;
import com.philips.platform.appframework.connectivity.network.PostMomentRquest;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.utils.DataServicesConstants;

import java.net.URL;
import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConnectivityPresenter implements ConnectivityContract.UserActionsListener {
    public static final String TAG = ConnectivityPresenter.class.getSimpleName();
    private ConnectivityContract.View connectivityViewListener;
    private Context context;
    private String dataCoreBaseUrl;
    private User user;
    private String momentValue;

    public ConnectivityPresenter(final ConnectivityContract.View connectivityViewListener, User user, Context context) {
        this.connectivityViewListener = connectivityViewListener;
        this.context = context;
        this.user = user;
    }

    /**
     * Send moment to data core and get it back
     *
     * @param momentValue
     */
    @Override
    public void processMoment(String momentValue) {
        RALog.d(TAG, " process moment in data core ");
        this.momentValue = momentValue;
        if (canSync(user)) {
            if (TextUtils.isEmpty(momentValue)) {
                connectivityViewListener.onProcessMomentError("Moment value can not be empty");
            } else {
                connectivityViewListener.onProcessMomentProgress("Posting data in datacore, Please wait...");
                if (TextUtils.isEmpty(dataCoreBaseUrl)) {
                    loadDataCoreURLFromServiceDiscovery(context);
                } else {
                    postMoment();
                }
            }
        } else {
            connectivityViewListener.onProcessMomentError("Datacore is not reachable");
        }
    }

    /**
     * Check for prerequisite of posting moment
     *
     * @param user
     * @return
     */
    protected boolean canSync(User user) {
        if (user.getHsdpAccessToken() == null || RegistrationConfiguration.getInstance().getHSDPInfo() == null || !BaseAppUtil.isNetworkAvailable(context)) {
            return false;
        }
        RALog.d(TAG, " can sync data from data core or not ");
        return true;
    }

    /**
     * Rest api call to send moment to data core
     */
    public void postMoment() {
        PostMomentRquest postMomentRquest = new PostMomentRquest(getDummyUserMoment(momentValue), dataCoreBaseUrl, user, new PostMomentRquest.PostMomentResponseListener() {
            @Override
            public void onPostMomentSuccess(String momentId) {
                connectivityViewListener.onProcessMomentProgress("Getting moment from datacore, Please wait...");
                getMoment(momentId);
            }

            @Override
            public void onPostMomentError(VolleyError error) {
                connectivityViewListener.onProcessMomentError("Error while posting moment value");
            }
        });
        postMomentRquest.executeRequest(context.getApplicationContext());
    }

    /**
     * Rest api call to get moment from server based on moment id
     *
     * @param momentId
     */
    public void getMoment(String momentId) {
        GetMomentRequest getMomentRequest = new GetMomentRequest(new GetMomentRequest.GetMomentResponseListener() {
            @Override
            public void onGetMomentSuccess(String momentId) {
                connectivityViewListener.onProcessMomentSuccess(momentValue);
            }

            @Override
            public void onGetMomentError(VolleyError error) {
                connectivityViewListener.onProcessMomentError(error.getMessage());
            }
        }, dataCoreBaseUrl, user, momentId);
        getMomentRequest.executeRequest(context.getApplicationContext());
    }

    @Override
    public void setUpApplicance(@NonNull RefAppBleReferenceAppliance appliance) {
        if (appliance == null) {
            throw new IllegalArgumentException("Cannot create bleReferenceAppliance for provided NetworkNode.");
        }


        // Setup device measurement port
        appliance.getDeviceMeasurementPort().addPortListener(new DICommPortListener<DeviceMeasurementPort>() {

            @Override
            public void onPortUpdate(DeviceMeasurementPort deviceMeasurementPort) {
                connectivityViewListener.updateConnectionStateText(context.getString(R.string.RA_Connectivity_Connection_Status_Disconnected));
                if (deviceMeasurementPort != null && deviceMeasurementPort.getPortProperties() != null) {
                    connectivityViewListener.updateDeviceMeasurementValue(Integer.toString(deviceMeasurementPort.getPortProperties().measurementvalue));
                } else {
                    connectivityViewListener.onDeviceMeasurementError(Error.NOT_AVAILABLE, "");
                }
            }

            @Override
            public void onPortError(DeviceMeasurementPort deviceMeasurementPort, Error error, final String s) {
                connectivityViewListener.updateConnectionStateText(context.getString(R.string.RA_Connectivity_Connection_Status_Disconnected));
                connectivityViewListener.onDeviceMeasurementError(error, s);
            }
        });

    }

    /**
     * make dummy user moment
     *
     * @return
     */
    public UserMoment getDummyUserMoment(String editTextValue) {
        MomentDetail momentDetail = new MomentDetail();
        momentDetail.setType("Example");
        momentDetail.setValue(editTextValue);

        Measurement measurement = new Measurement();
        ArrayList<MomentDetail> momentDetailList = new ArrayList<MomentDetail>();
        momentDetailList.add(momentDetail);
        measurement.setMomentDetailArrayList(momentDetailList);
        measurement.setTimestamp("2015-08-14T07:07:14.000Z");
        measurement.setType("Duration");
        measurement.setUnit("sec");
        measurement.setValue(editTextValue);

        ArrayList<Measurement> measurementsList = new ArrayList<Measurement>();
        measurementsList.add(measurement);

        UserMoment userMoment = new UserMoment();
        userMoment.setMomentDetailArrayList(momentDetailList);
        userMoment.setMeasurementArrayList(measurementsList);
        userMoment.setTimestamp("2015-08-13T14:54:25+0200");
        userMoment.setType("Example");
        return userMoment;
    }

    /**
     * loads data core base url from service discovery
     *
     * @param context
     */
    public void loadDataCoreURLFromServiceDiscovery(Context context) {
        RALog.d(TAG, " loads data core base url from service discovery");
        AppInfraInterface appInfra = getAppInfra();
        ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference(DataServicesConstants.BASE_URL_KEY, new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES errorvalues, String errorText) {
                        connectivityViewListener.onProcessMomentError(errorText);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url.toString().isEmpty()) {
                            connectivityViewListener.onProcessMomentError("Empty Url from Service discovery");
                        } else {
                            dataCoreBaseUrl = url.toString();
                            postMoment();
                        }
                    }
                });
    }

    protected AppInfraInterface getAppInfra(){
        return ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
    }
}
