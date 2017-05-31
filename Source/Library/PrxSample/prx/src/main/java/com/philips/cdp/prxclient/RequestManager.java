package com.philips.cdp.prxclient;

import com.philips.cdp.prxclient.network.NetworkWrapper;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * Description : This is the entry class to start the PRX Request.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class RequestManager {

    private static final String TAG = RequestManager.class.getSimpleName();
    private PRXDependencies mPrxDependencies;

    public void init(PRXDependencies prxDependencies) {
        mPrxDependencies = prxDependencies;
    }

    public void executeRequest(PrxRequest prxRequest, ResponseListener listener) {
        makeRequest(prxRequest, listener);
    }

    public void cancelRequest(String requestTag) {
    }

    private void makeRequest(final PrxRequest prxRequest, final ResponseListener listener) {
        try {
            new NetworkWrapper(mPrxDependencies).executeCustomJsonRequest(prxRequest, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLibVersion() {
        String mAppVersion=null;
        try {
            mAppVersion = BuildConfig.VERSION_NAME;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mAppVersion != null && !mAppVersion.isEmpty()) {
            if (!mAppVersion.matches("[0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?")) {
                throw new IllegalArgumentException("AppVersion should in this format " +
                        "\" [0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?]\" ");
            }
        } else {
            throw new IllegalArgumentException("Prx Appversion cannot be null");
        }
        return mAppVersion;
    }


}
