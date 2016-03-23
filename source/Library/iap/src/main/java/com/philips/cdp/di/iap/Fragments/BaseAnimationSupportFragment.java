/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : InAppPurchase
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPBackButtonListener;
import com.philips.cdp.di.iap.activity.IAPFragmentListener;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;

public abstract class BaseAnimationSupportFragment extends Fragment implements IAPBackButtonListener, IAPNetworkError.IAPNoNetworkError, EventListener{
    private IAPFragmentListener mActivityListener;
    IAPNetworkError mIapNetworkError;
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mActivityListener = (IAPFragmentListener) getActivity();
        IAPNetworkError.getInstance().setListner(this);
    }

    public enum AnimationType {
        /**
         * No animation for Fragment
         */
        NONE,
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackButtonVisibility(View.VISIBLE);
    }

    public void addFragment(BaseAnimationSupportFragment newFragment,
                            String newFragmentTag) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                + newFragmentTag + ")");
    }

    public void replaceFragment(Fragment newFragment, String newFragmentTag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.replace(R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
        transaction.commitAllowingStateLoss();
    }

    protected void setTitle(int resourceId) {
        mActivityListener.setHeaderTitle(resourceId);
    }

    protected void setBackButtonVisibility(final int isVisible) {
        mActivityListener.setBackButtonVisibility(isVisible);
    }

    protected void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void onBackPressed() {
        //NOP
    }

    @Override
    public void noConnectionError(Message msg) {
        showErrorMessage(msg);
    }

    private void showErrorMessage(final Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            IAPNetworkError error = (IAPNetworkError) msg.obj;
            if(error.getMessage()!=null && !error.getMessage().equalsIgnoreCase("")) {
                NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getContext().getString(R.string.iap_ok),
                        getContext().getString(R.string.iap_server_error), error.getMessage());
            }
        } else {
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getContext().getString(R.string.iap_ok),
                    getContext().getString(R.string.iap_server_error), getContext().getString(R.string.iap_something_went_wrong));
        }
    }

    @Override
    public void raiseEvent(String event){

    }

    @Override
    public void onEventReceived(String event){

    }
}
