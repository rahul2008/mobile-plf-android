package com.philips.cdp.wifirefuapp.states;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.cdp.wifirefuapp.ui.CreateSubjectProfileFragment;

/**
 * Created by philips on 6/8/17.
 */

public class CreateSubjectProfileState extends BaseState {

    private Context context;
    private PairDevicePojo pairDevicePojo;

    public CreateSubjectProfileState(PairDevicePojo pairDevicePojo,Context context) {
        super(context);
        this.context = context;
        this.pairDevicePojo = pairDevicePojo;
    }

    @Override
    void start(StateContext stateContext) {
        ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().add(new CreateSubjectProfileFragment(),"CreateSubjectProfileFragment").commit();
    }
}
