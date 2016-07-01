/**
 * RequestData will help to perform network operations in UI.
 *
 * @author : naveen@philips.com
 * @since : 16 Jan 2015
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.request;



import android.os.Handler;
import android.os.Looper;

import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * RequestData class respnsobile for performing the metwork operation in UI Thread.
 * Once it performed the remote response will trigger through the respective listeners.
 */
public class RequestData {

    private static final String TAG = RequestData.class.getSimpleName();

    private ResponseCallback mResponseCallback = null;
    private String mResponse = null;
    private String mRequestUrl = null;
    private Handler mResponseHandler = null;


    public void setRequestUrl(final String url)
    {
        DigiCareLogger.i(TAG, "url : " + url);
        mRequestUrl = url;
    }

    public void setResponseCallback(final ResponseCallback responseCallback)
    {
        mResponseCallback = responseCallback;
        mResponseHandler = new Handler(Looper.getMainLooper());
    }

    public void execute() {
        final NetworkThread mNetworkThread = new NetworkThread();
        mNetworkThread.setPriority(Thread.MAX_PRIORITY);
        mNetworkThread.start();
    }

    protected void notifyResponseHandler() {
        if (mResponse != null) {

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseCallback.onResponseReceived(mResponse);
                }
            });

        }
    }

    /**
     * NetworkThread class is an high priority thhread to perform the UI operations.
     */
    class NetworkThread extends Thread {

        @Override
        public void run() {
            try {
                final URL obj = new URL(mRequestUrl);
                final HttpURLConnection mHttpUrlConnection = (HttpURLConnection) obj
                        .openConnection();
                mHttpUrlConnection.setRequestMethod("GET");
                InputStream mInputStream = mHttpUrlConnection.getInputStream();
                Reader mReader = new InputStreamReader(mInputStream, "UTF-8");
                final BufferedReader in = new BufferedReader(mReader);
                String inputLine = null;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                mResponse = response.toString();
            } catch (IOException e) {
                DigiCareLogger.e(
                        TAG,
                        "Failed to fetch Response Data : "
                                + e.getLocalizedMessage());
            } finally {
                DigiCareLogger.d(TAG, "Response: [" + mResponse + "]");
                notifyResponseHandler();
            }
        }
    }

}
