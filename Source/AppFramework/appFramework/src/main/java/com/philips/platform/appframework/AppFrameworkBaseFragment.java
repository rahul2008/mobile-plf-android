/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.modularui.statecontroller.UIBasePresenter;

/**
 * AppFrameworkBaseFragment is the <b>Base class</b> for all fragments.
 */
public abstract class AppFrameworkBaseFragment extends Fragment{

    private static String TAG = AppFrameworkBaseFragment.class.getSimpleName();
    private static boolean isConnectionAvailable;
    private static int mContainerId = 0;
    private static String mPreviousPageName = null;
    private static int mEnterAnimation = 0;
    private static int mExitAnimation = 0;
    private static FragmentActivity mFragmentActivityContext = null;
    private static FragmentActivity mActivityContext = null;
    private static String FRAGMENT_TAG_NAME = "productselection";
    private static Boolean mListViewRequired = true;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    protected SharedPreferences prefs = null;
    protected int mLeftRightMarginPort = 0;
    protected int mLeftRightMarginLand = 0;
    private FragmentManager fragmentManager = null;
    private Thread mUiThread = Looper.getMainLooper().getThread();
    private TextView mActionBarTitle = null;
    protected UIBasePresenter fragmentPresenter;

    public synchronized static void setStatus(boolean connection) {
        isConnectionAvailable = connection;
    }


    public abstract String getActionbarTitle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mFragmentActivityContext = getActivity();
        fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionbarTitle();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }



    protected final void updateUI(Runnable runnable) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    protected void backstackFragment() {
        fragmentManager.popBackStack();
        removeCurrentFragment();
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    /**
     * Updating action bar title. The text has to be updated at each fragment
     * selection/creation.
     */
    private void setActionbarTitle() {
        if (mActionBarTitle == null) {
            mActionBarTitle = (TextView) getActivity().findViewById(R.id.hamburger_title);
        }
        String titleText = null;
        if (getActionbarTitle() == null) {
            titleText = getResources().getString(R.string.app_name);
        } else {
            titleText = getActionbarTitle();
        }
        mActionBarTitle.setText(titleText);
    }
}
