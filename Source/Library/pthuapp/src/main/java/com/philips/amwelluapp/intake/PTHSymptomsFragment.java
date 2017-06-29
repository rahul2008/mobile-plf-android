package com.philips.amwelluapp.intake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class PTHSymptomsFragment extends PTHBaseFragment implements BackEventListener {
    public static final String TAG = PTHSymptomsFragment.class.getSimpleName();
    PTHSymptomsPresenter mPTHSymptomsPresenter;
    PTHConsumer consumer;
    PTHProviderInfo providerInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pth_symptoms, container, false);
        Bundle bundle = getArguments();
        consumer = (PTHConsumer) bundle.getSerializable("Consumer");

        providerInfo = (PTHProviderInfo) bundle.getSerializable("providerInfo");
        return view;
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPTHSymptomsPresenter = new PTHSymptomsPresenter(this,consumer,providerInfo);
        if(null != getActionBarListener()){
            getActionBarListener().updateActionBar(getString(R.string.pth_prepare_your_visit),true);
        }
        getVisistContext();
    }

    private void getVisistContext() {
        try {
            mPTHSymptomsPresenter.getVisitContext();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        } catch (AWSDKInitializationException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
