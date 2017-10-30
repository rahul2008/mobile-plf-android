/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import android.os.AsyncTask;

import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.faqs.THSFaqPresenter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class THSRestClient extends AsyncTask<String, Void, String> {
    THSBasePresenter mThsBasePresenter;

    public THSRestClient(THSBasePresenter thsFaqPresenter) {
        mThsBasePresenter = thsFaqPresenter;
    }

    protected String doInBackground(String... urls) {
        String jsonString;
        try {
            HttpURLConnection urlConnection = null;
            URL url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();

        } catch (Exception e) {


            return null;
        }
        return jsonString;
    }

    protected void onPostExecute(String feed) {
        if (mThsBasePresenter instanceof THSFaqPresenter) {
            ((THSFaqPresenter) mThsBasePresenter).parseJson(feed);
        }
    }

}
