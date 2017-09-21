/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.platform.ths.R;
import com.philips.platform.ths.init.THSInitFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.ProgressBar;

import static com.philips.platform.ths.utility.THSConstants.THS_USER_NOT_LOGGED_IN;


public class THSBaseFragment extends Fragment implements THSBaseView,BackEventListener {


    public FragmentLauncher mFragmentLauncher;
    public com.philips.platform.uid.view.widget.ProgressBar mPTHBaseFragmentProgressBar;
    private ActionBarListener actionBarListener;
    protected final int SMALL = 0;
    protected final int MEDIUM = 1;
    protected final int BIG = 2;

    @Override
    public void finishActivityAffinity() {

    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public int getContainerID() {
        return mFragmentLauncher.getParentContainerResourceID();
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
     this.mFragmentLauncher = fragmentLauncher;
    }

    public FragmentLauncher getFragmentLauncher(){
        return mFragmentLauncher;
    }

    public void setActionBarListener(ActionBarListener actionBarListener){
        this.actionBarListener = actionBarListener;
    }

    public ActionBarListener getActionBarListener(){
        return actionBarListener;
    }

    public void hideProgressBar() {
        if (mPTHBaseFragmentProgressBar != null) {
            mPTHBaseFragmentProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void addFragment(THSBaseFragment fragment, String fragmentTag, Bundle bundle) {
        //TODO: The try catch block will be removed when the loading will not be done on Back press
        try {
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(getContainerID(), fragment, fragmentTag);
            fragmentTransaction.addToBackStack(fragmentTag);
            fragment.setFragmentLauncher(getFragmentLauncher());
            fragment.setActionBarListener(getActionBarListener());
            fragmentTransaction.commit();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void popFragmentByTag(String fragmentTag,int flag) {
        getFragmentActivity().getSupportFragmentManager().popBackStack(fragmentTag,flag);
    }

    public void createCustomProgressBar(ViewGroup group, int size) {
        ViewGroup parentView = (ViewGroup) getView();
        ViewGroup layoutViewGroup = group;
        if(parentView != null){
            group = parentView;
        }

        switch (size){
            case BIG:
                getContext().getTheme().applyStyle(R.style.PTHCircularPBBig, true);
                break;
            case SMALL:
                getContext().getTheme().applyStyle(R.style.PTHCircularPBSmall, true);
                break;
            case MEDIUM:
                getContext().getTheme().applyStyle(R.style.PTHCircularPBMedium, true);
                break;
            default:
                getContext().getTheme().applyStyle(R.style.PTHCircularPBMedium, true);
                break;
        }

        mPTHBaseFragmentProgressBar = new ProgressBar(getContext(), null, R.attr.pth_cirucular_pb);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPTHBaseFragmentProgressBar.setLayoutParams(params);

        try {
            group.addView(mPTHBaseFragmentProgressBar);
        }catch (Exception e){
            layoutViewGroup.addView(mPTHBaseFragmentProgressBar);
        }

        if (mPTHBaseFragmentProgressBar != null) {
            mPTHBaseFragmentProgressBar.setVisibility(View.VISIBLE);
        }
    }

    //TODO: Toast to be removed
    public void showToast(String message){
        if (getContext() != null) {
            //TODO: TO be removed
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void showError(String message) {
        AlertDialogFragment alertDialogFragmentUserNotLoggedIn = (AlertDialogFragment) getFragmentManager().findFragmentByTag(THS_USER_NOT_LOGGED_IN);
        if (null != alertDialogFragmentUserNotLoggedIn) {
            alertDialogFragmentUserNotLoggedIn.dismiss();
        }

        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getFragmentActivity());
        builder.setMessage(message);
        builder.setTitle(getResources().getString(R.string.ths_matchmaking_error));

        alertDialogFragmentUserNotLoggedIn = builder.setCancelable(false).create();
        View.OnClickListener onClickListener = getOnClickListener(alertDialogFragmentUserNotLoggedIn);
        builder.setPositiveButton(getResources().getString(R.string.ths_matchmaking_ok_button), onClickListener);
        alertDialogFragmentUserNotLoggedIn.setPositiveButtonListener(onClickListener);
        alertDialogFragmentUserNotLoggedIn.show(getFragmentManager(), THS_USER_NOT_LOGGED_IN);
    }

    @NonNull
    private View.OnClickListener getOnClickListener(final AlertDialogFragment finalAlertDialogFragmentStartVisit) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragmentByTag(THSInitFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                finalAlertDialogFragmentStartVisit.dismiss();
            }
        };
    }
}
