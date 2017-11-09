/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.platform.ths.R;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.init.THSInitFragment;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSNetworkStateListener;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.welcome.THSWelcomeBackFragment;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.ProgressBar;

import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SERVER_ERROR;
import static com.philips.platform.ths.utility.THSConstants.THS_USER_ERROR;

public class THSBaseFragment extends Fragment implements THSBaseView, BackEventListener, THSNetworkStateListener.ConnectionReceiverListener {


    public FragmentLauncher mFragmentLauncher;
    public com.philips.platform.uid.view.widget.ProgressBar mPTHBaseFragmentProgressBar;
    private ActionBarListener actionBarListener;
    protected final int SMALL = 0;
    protected final int MEDIUM = 1;
    protected final int BIG = 2;
    private THSNetworkStateListener networkStateListener;
    public AlertDialogFragment alertDialogFragment;
    public static final String ALERT_DIALOG_TAG = THSBaseFragment.class.getSimpleName() + "Dialog";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkStateListener = new THSNetworkStateListener();
        getActivity().registerReceiver(
                networkStateListener,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));

       /* Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                // put your save logic here
                // save to file, send to email, etc.
                // Also you can get information about throwed exception
                // for example : ex.getMessage();
                AmwellLog.i(AmwellLog.LOG,"crashed");
                showToast(getString(R.string.ths_se_server_error_toast_message));
                doTagging("Others",ex.getMessage(),false);
                exitFromAmWell(false);
            }
        });*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(networkStateListener);
        setConnectionListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        setConnectionListener(this);

    }

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

    public FragmentLauncher getFragmentLauncher() {
        return mFragmentLauncher;
    }

    public void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }

    public ActionBarListener getActionBarListener() {
        return actionBarListener;
    }

    public void hideProgressBar() {
        if (mPTHBaseFragmentProgressBar != null) {
            mPTHBaseFragmentProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void addFragment(THSBaseFragment fragment, String fragmentTag, Bundle bundle, boolean isReplace) {
        //TODO: The try catch block will be removed when the loading will not be done on Back press
        isReplace = true;
        try {
            fragment.setArguments(bundle);
            if (null == getFragmentLauncher()) {
                popFragmentByTag(THSWelcomeFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fragment.setFragmentLauncher(getFragmentLauncher());
            fragment.setActionBarListener(getActionBarListener());
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentActivity().getSupportFragmentManager().beginTransaction();
            if (isReplace) {
                fragmentTransaction.replace(getContainerID(), fragment, fragmentTag);
            } else {
                fragmentTransaction.add(getContainerID(), fragment, fragmentTag);
            }
            fragmentTransaction.addToBackStack(fragmentTag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void popFragmentByTag(String fragmentTag, int flag) {
        getFragmentActivity().getSupportFragmentManager().popBackStack(fragmentTag, flag);
    }

    public void createCustomProgressBar(ViewGroup group, int size) {
        ViewGroup parentView = (ViewGroup) getView();
        ViewGroup layoutViewGroup = group;
        if (parentView != null) {
            group = parentView;
        }

        switch (size) {
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
        } catch (Exception e) {
            layoutViewGroup.addView(mPTHBaseFragmentProgressBar);
        }

        if (mPTHBaseFragmentProgressBar != null) {
            mPTHBaseFragmentProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void setConnectionListener(THSNetworkStateListener.ConnectionReceiverListener listener) {
        THSNetworkStateListener.connectionReceiverListener = listener;
    }

    //TODO: Toast to be removed
    public void showToast(String message) {
        if (getContext() != null) {
            //TODO: TO be removed
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(int stringResource) {
        if (getContext() != null) {
            //TODO: TO be removed
            Toast.makeText(getContext(), stringResource, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void showError(String message) {
        showError( message, false);
    }

    public void showError( String message, final boolean shouldGoBack) {
        if (isFragmentAttached()) {
            if(null==message){  // if message is not identified make it THS_GENERIC_SERVER_ERROR
                message = getString(R.string.ths_se_server_error_toast_message);
                //doTagging(module,message,true);
            }

            alertDialogFragment = new AlertDialogFragment.Builder(UIDHelper.getPopupThemedContext(getContext())).setDialogType(DialogConstants.TYPE_ALERT).setTitle(R.string.ths_matchmaking_error)
                    .setMessage(message).
                            setPositiveButton(R.string.ths_matchmaking_ok_button, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialogFragment.dismiss();
                                    if (shouldGoBack) {
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }
                                }
                            }).setCancelable(false).create();
            alertDialogFragment.show(getActivity().getSupportFragmentManager(), ALERT_DIALOG_TAG);
        }

    }

    public void doTagging(String module, String message, boolean isServerError){
            final String errorTag= THSTagUtils.createErrorTag(module,message);
            if(isServerError) {
                THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, THS_SERVER_ERROR, errorTag);
            }else{
                THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, THS_USER_ERROR, errorTag);
            }

    }

    public boolean isFragmentAttached() {
        boolean result = false;
        try {
            if (null != getActivity() && null != getContext() && isAdded()) {
                result = true;
            }
        } catch (Exception e) {
            AmwellLog.e(THSBaseFragment.class.getSimpleName(), e.getMessage());
        }
        return result;
    }

    public void exitFromAmWell(boolean isSuccess) {

        if (this.getActivity() instanceof THSLaunchActivity) {
            THSLaunchActivity thsLaunchActivity = (THSLaunchActivity) this.getActivity();
            thsLaunchActivity.finish();
        } else {

            FragmentManager fragmentManager = getFragmentManager();
            Fragment welComeFragment = fragmentManager.findFragmentByTag(THSWelcomeFragment.TAG);
            Fragment welComeBackFragment = fragmentManager.findFragmentByTag(THSWelcomeBackFragment.TAG);
            Fragment tHSInitFragment = fragmentManager.findFragmentByTag(THSInitFragment.TAG);

            if (welComeFragment != null) {
                fragmentManager.popBackStack(THSWelcomeFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (welComeBackFragment != null) {
                fragmentManager.popBackStack(THSWelcomeBackFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else if (tHSInitFragment != null) {
                fragmentManager.popBackStack(THSInitFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA,"exitToUgrow","toUgrowPage");
        if (THSManager.getInstance().getThsVisitCompletionListener() != null) {
            THSManager.getInstance().getThsVisitCompletionListener().onTHSVisitComplete(isSuccess);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            showToast(getString(R.string.ths_internet_disconnected_message));
        }
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
