package com.philips.amwelluapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.amwelluapp.common.GlobalValues;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.uappclasses.PTHMicroAppInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PTHLaunchFragment extends Fragment implements BackEventListener {


    public static String TAG = PTHLaunchFragment.class.getSimpleName();
    private FragmentLauncher fragmentLauncher;
    private TextView welcomeTextView;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        GlobalValues.fragmentContainer=container;
        View view = inflater.inflate(R.layout.fragment_sample, container, false);
        welcomeTextView = (TextView) view.findViewById(R.id.welcome_text);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //fragmentLauncher.getActionbarListener().updateActionBar("Sample", true);
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            initializeTeleHealth();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(PTHMicroAppInterface.WELCOME_MESSAGE);
            welcomeTextView.setText(message);


        }
    }

    private void initializeTeleHealth() throws MalformedURLException, URISyntaxException {
        try {
            GlobalValues.mAWSDK = AWSDKFactory.getAWSDK(getActivity().getApplicationContext());
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
        GlobalValues.mAWSDK.getDefaultLogger().setPriority(Log.DEBUG); // set log level to debug
        SDKCallback callback = new SDKCallback() {
            @Override
            public void onResponse(Object o, SDKError sdkError) {
                Log.v("AWSDK ","Initialization Success");

                LoginFragment loginFragment= new LoginFragment();
                getFragmentManager().beginTransaction()
                        .replace(GlobalValues.fragmentContainer.getId(), loginFragment,"LoginFragment")
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v("AWSDK ","Initialization  error");
            }
        };

        final Map<AWSDK.InitParam, Object> initParams = new HashMap<>();
        initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://sdk.myonlinecare.com");
        initParams.put(AWSDK.InitParam.ApiKey, "62f5548a"); //client key

       // initParams.put(AWSDK.InitParam.LaunchIntentData,null );//launchUri

        try {
            GlobalValues.mAWSDK.initialize(
                    initParams,
                    callback);
            int y=10;
        }
        catch (AWSDKInitializationException e) {
            Log.v("AWSDK ","Initialization Auth error");
        }


    }
}