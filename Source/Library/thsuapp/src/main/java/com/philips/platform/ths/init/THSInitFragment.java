/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.init;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.faqs.THSFaqPojo;
import com.philips.platform.ths.faqs.THSFaqPresenter;
import com.philips.platform.ths.utility.THSRestClient;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class THSInitFragment extends THSBaseFragment{
    public static final String TAG = THSInitFragment.class.getSimpleName();
    THSInitPresenter mThsInitPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_init_fragment, container, false);
        mThsInitPresenter = new THSInitPresenter(this);
        initializeSDK(view);

        ActionBarListener actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_welcome),true);
        }
        return view;
    }

    private void initializeSDK(ViewGroup view) {
        createCustomProgressBar(view, BIG);
        mThsInitPresenter.initializeAwsdk();
    }

    public void popSelfBeforeTransition() {
        try {
            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }catch (IllegalStateException exception){

        }
    }
}
