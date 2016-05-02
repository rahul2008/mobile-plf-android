/**
 * @author naveen@philips.com
 * <p/>
 * <p> Common class to receive the response from the remote network URL. </p>
 */

package com.philips.cdp.digitalcare;



import android.os.Handler;
import android.os.Looper;

import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestData {

    private final String TAG = RequestData.class.getSimpleName();

    private ResponseCallback mResponseCallback = null;
    private String mResponse = null;
    private String mRequestUrl = null;
    private Handler mResponseHandler = null;


    public RequestData(String url, ResponseCallback responseCallback) {
        DigiCareLogger.i(TAG, "url : " + url);
        mRequestUrl = url;
        mResponseCallback = responseCallback;
        mResponseHandler = new Handler(Looper.getMainLooper());
    }

    public void execute() {
        NetworkThread mNetworkThread = new NetworkThread();
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

    class NetworkThread extends Thread {

        @Override
        public void run() {
            try {
                URL obj = new URL(mRequestUrl);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) obj
                        .openConnection();
                mHttpURLConnection.setRequestMethod("GET");
                InputStream mInputStream = mHttpURLConnection.getInputStream();
                Reader mReader = new InputStreamReader(mInputStream, "UTF-8");
                BufferedReader in = new BufferedReader(mReader);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                mResponse = response.toString();
            } catch (Exception e) {
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
