package com.philips.platform.appframework.connectivity;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.registration.User;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivity.appliance.DeviceMeasurementPort;
import com.philips.platform.appframework.connectivity.models.Measurement;
import com.philips.platform.appframework.connectivity.models.MomentDetail;
import com.philips.platform.appframework.connectivity.models.UserMoment;
import com.philips.platform.appframework.connectivity.network.GetMomentRequest;
import com.philips.platform.appframework.connectivity.network.PostMomentRquest;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.core.utils.DataServicesConstants;

import java.net.URL;
import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConnectivityPresenter implements ConnectivityContract.UserActionsListener {
    private ConnectivityContract.View connectivityViewListener;
    private Context context;

    public ConnectivityPresenter(final ConnectivityContract.View connectivityViewListener, Context context) {
        this.connectivityViewListener = connectivityViewListener;
        this.context = context;
    }

    @Override
    public void postMoment(final User user, String dataCoreBaseUrl, final String momentValue) {
        PostMomentRquest postMomentRquest = new PostMomentRquest(getDummyUserMoment(momentValue), dataCoreBaseUrl, user, postMomentResponseListener);
        postMomentRquest.executeRequest(context.getApplicationContext());
    }

    @Override
    public void getMoment(final User user, String dataCoreBaseUrl,final String momentId) {
        GetMomentRequest getMomentRequest = new GetMomentRequest(getMomentResponseListener, dataCoreBaseUrl, user, momentId);
        getMomentRequest.executeRequest(context.getApplicationContext());
    }

    @Override
    public void setUpApplicance(@NonNull BleReferenceAppliance appliance) {
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

    private PostMomentRquest.PostMomentResponseListener postMomentResponseListener = new PostMomentRquest.PostMomentResponseListener() {
        @Override
        public void onPostMomentSuccess(final String momentId) {
            connectivityViewListener.updateUIOnPostMomentSuccess(momentId);
        }

        @Override
        public void onPostMomentError(final VolleyError error) {
            connectivityViewListener.updateUIOnPostMomentError(error);
        }
    };

    private GetMomentRequest.GetMomentResponseListener getMomentResponseListener = new GetMomentRequest.GetMomentResponseListener() {
        @Override
        public void onGetMomentSuccess(final String momentValue) {
            connectivityViewListener.updateUIOnGetMomentSuccess(momentValue);
        }

        @Override
        public void onGetMomentError(final VolleyError error) {
            connectivityViewListener.updateUIOnGetMomentError(error);
        }
    };

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
     * Will load data core base url from service discovery
     * @param context
     */
    public void loadDataCoreURLFromServiceDiscovery(Context context) {

        AppInfraInterface appInfra = ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
        ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference(DataServicesConstants.BASE_URL_KEY, new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES errorvalues, String errorText) {
                        connectivityViewListener.serviceDiscoveryError(errorvalues,errorText);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url.toString().isEmpty()) {
                            connectivityViewListener.serviceDiscoveryError(ERRORVALUES.SERVER_ERROR, "Empty Url from Service discovery");
                        } else {
                            connectivityViewListener.onDataCoreBasrUrlLoad(url.toString());
                        }
                    }
                });
    }
}
