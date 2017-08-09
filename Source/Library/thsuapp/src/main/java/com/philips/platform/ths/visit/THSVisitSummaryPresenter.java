package com.philips.platform.ths.visit;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKCallback;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

/**
 * Created by philips on 8/4/17.
 */

public class THSVisitSummaryPresenter implements THSBasePresenter, THSVisitSummaryCallbacks.THSVisitSummaryCallback<THSVisitSummary, THSSDKError>,THSSDKCallback<Void, SDKError> {

    THSVisitSummaryFragment mTHSVisitSummaryFragment;

    public THSVisitSummaryPresenter(THSVisitSummaryFragment mTHSVisitSummaryFragment) {
        this.mTHSVisitSummaryFragment = mTHSVisitSummaryFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

     void fetchVisitSummary(){
        try {
            THSManager.getInstance().getVisitSummary(mTHSVisitSummaryFragment.getFragmentActivity(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

     void sendRatings(Integer providerRating, Integer visitRating){
        try {
            THSManager.getInstance().sendRatings(mTHSVisitSummaryFragment.getFragmentActivity(),providerRating,visitRating,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    //start of visit summary callbacks
    @Override
    public void onResponse(THSVisitSummary tHSVisitSummary, THSSDKError tHSSDKError) {

    }

    @Override
    public void onResponse(Void var1, SDKError var2) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }



}
