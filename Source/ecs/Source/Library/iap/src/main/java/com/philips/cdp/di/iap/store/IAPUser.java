/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshSessionListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDataListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class IAPUser implements UserDataListener {
    private final static String TAG = IAPUser.class.getSimpleName();

    Semaphore mSemaphore = new Semaphore(0);
    private HybrisStore mStore;
    private boolean mTokenRefreshSuccessful;

    private volatile boolean mLockReleaseRequested;
    private UserDataInterface mUserDataInterface;

    public IAPUser() {
    }

    public IAPUser(final Context context, final HybrisStore store, IAPDependencies iapDependencies) {
        mUserDataInterface = iapDependencies.getUserDataInterface();
        mUserDataInterface.addUserDataInterfaceListener(this);
        mStore = store;
    }

    public String getJanRainID() {
        ArrayList<String> detailsKey = new ArrayList<>();
        detailsKey.add(UserDetailConstants.ACCESS_TOKEN);
        try {
           HashMap<String,Object> userDetailsMap = mUserDataInterface.getUserDetails(detailsKey);
           return userDetailsMap.get(UserDetailConstants.ACCESS_TOKEN).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getJanRainEmail() {
        ArrayList<String> detailsKey = new ArrayList<>();
        detailsKey.add(UserDetailConstants.EMAIL);
        String janrainEmail = null;
        try {
               janrainEmail = mUserDataInterface.getUserDetails(detailsKey).get(UserDetailConstants.EMAIL).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return janrainEmail;
    }

    public String getGivenName() {
        ArrayList<String> detailsKey = new ArrayList<>();
        detailsKey.add(UserDetailConstants.GIVEN_NAME);
        String givenName = null;
        try {
            givenName = mUserDataInterface.getUserDetails(detailsKey).get(UserDetailConstants.GIVEN_NAME).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return givenName;
    }

    public String getFamilyName() {
        ArrayList<String> detailsKey = new ArrayList<>();
        detailsKey.add(UserDetailConstants.FAMILY_NAME);
        String familyName = null;
        try {
            familyName = mUserDataInterface.getUserDetails(detailsKey).get(UserDetailConstants.FAMILY_NAME).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return familyName;
    }

    public void refreshLoginSession() {
        mTokenRefreshSuccessful = false;
        mLockReleaseRequested = false;
        IAPLog.d(TAG, " requesting refresh login session for user");

        mUserDataInterface.refreshSession(new RefreshSessionListener() {
            @Override
            public void refreshSessionSuccess() {
                IAPLog.d(TAG, " refreshLoginSuccessful");
                mStore.updateJanRainIDBasedUrls();
                mTokenRefreshSuccessful = true;
                mLockReleaseRequested = true;
                unlockOAuthThread();
            }

            @Override
            public void refreshSessionFailed(Error error) {
                IAPLog.d(TAG, " refreshLoginSuccessful failed with error=" + error);
                mLockReleaseRequested = true;
                unlockOAuthThread();
            }

            @Override
            public void forcedLogout() {
                IAPLog.d(TAG, "forcedLogout");
                mStore.setNewUser(true);

            }
        });
        lockOAuthThread();
    }

    public boolean isTokenRefreshSuccessful() {
        return mTokenRefreshSuccessful;
    }

    protected void unlockOAuthThread() {
        mSemaphore.release();
    }

    private void lockOAuthThread() {
        //We got the refresh call back earlier than request locking.
        if (mLockReleaseRequested)
            return;

        try {
            mSemaphore.acquire();
        } catch (InterruptedException e) {
            IAPLog.e(IAPLog.LOG, e.getMessage());
        }
    }

    @Override
    public void logoutSessionSuccess() {
        IAPLog.d(TAG, "logoutSessionSuccess");
        mStore.setNewUser(true);
        mUserDataInterface.removeUserDataInterfaceListener( this);
    }

    @Override
    public void logoutSessionFailed(Error error) {
        //NOP
        IAPLog.d(TAG, "logoutSessionFailed");
    }

    @Override
    public void onRefetchSuccess() {
    // NOP
    }

    @Override
    public void onRefetchFailure(Error error) {
        // NOP
    }

    @Override
    public void refreshSessionSuccess() {
        // NOP since handled by inline listener
    }

    @Override
    public void refreshSessionFailed(Error error) {
    // NOP since handled by inline listener
    }

    @Override
    public void forcedLogout() {
        mStore.setNewUser(true);
        mUserDataInterface.removeUserDataInterfaceListener( this);

    }
}