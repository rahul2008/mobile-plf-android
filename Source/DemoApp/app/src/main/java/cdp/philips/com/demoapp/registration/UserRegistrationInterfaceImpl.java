/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package cdp.philips.com.demoapp.registration;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.janrain.android.engage.JREngage.getApplicationContext;

@Singleton
public class UserRegistrationInterfaceImpl implements UserRegistrationInterface {
    @NonNull
    private final Context mContext;
    @NonNull
    private User mUser;
    private boolean mIsRefershTokenInProgress;
    private String mAccessToken = "";

    @Inject
    public UserRegistrationInterfaceImpl(@NonNull final Context context, @NonNull final User user) {
        mContext = context;
        mUser = user;
    }

    private Runnable refreshLoginSessionRunnable = new Runnable() {
        @Override
        public void run() {
            mUser = new User(mContext);
            mUser.refreshLoginSession(new RefreshLoginSessionHandler() {
                @Override
                public void onRefreshLoginSessionSuccess() {
                    mAccessToken = gethsdpaccesstoken();
                    notifyLoginSessionResponse();
                }

                @Override
                public void onRefreshLoginSessionFailedWithError(int statusCode) {
                    notifyLoginSessionResponse();
                }

                @Override
                public void onRefreshLoginSessionInProgress(String s) {
                    mIsRefershTokenInProgress = true;
                }
            });
        }
    };

    private String gethsdpaccesstoken() {
        return getUser(mContext).getHsdpAccessToken();
    }

    @NonNull
    private User getUser(final Context context) {
        mUser = new User(context);
        return mUser;
    }

    @Override
    public boolean isUserLoggedIn() {
        mUser = new User(mContext);
        return mUser.isUserSignIn();
    }

    @NonNull
    @Override
    public String getHSDPAccessToken() {
        if ((mAccessToken == null || mAccessToken.isEmpty()) && !mIsRefershTokenInProgress) {
            mAccessToken = gethsdpaccesstoken();
        }
        return mAccessToken;
    }

    @NonNull
    @Override
    public UserProfile getUserProfile() {
        return getUserProfileUserRegistrationPart();
    }

    @NonNull
    private UserProfile getUserProfileUserRegistrationPart() {
        final UserProfile userProfile;
        userProfile = new UserProfile(mUser.getGivenName(), mUser.getFamilyName(), mUser.getEmail(), mUser.getHsdpUUID());
        return userProfile;
    }

    @Override
    public synchronized void refreshAccessTokenUsingWorkAround() {
        if (mIsRefershTokenInProgress) {
            return;
        }
        mIsRefershTokenInProgress = true;
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(refreshLoginSessionRunnable);
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException ignored) {

            }
        }
        mIsRefershTokenInProgress = false;
    }

    private void notifyLoginSessionResponse() {
        synchronized (this) {
            notify();
        }
    }

    @Override
    public String getHSDPUrl() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        AppInfra gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        Object propertyForKey = gAppInfra.getConfigInterface().getPropertyForKey
                (URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL, URConfigurationConstants.UR, configError);
        return propertyForKey.toString();
    }

    /*private String email;

    public void clearUserData(DBRequestListener dbRequestListener) {
        DataServicesManager manager = DataServicesManager.getInstance();
        manager.deleteAll(dbRequestListener);
        clearPreferences();
        email = null;
        mAccessToken = "";
    }

    private void clearPreferences() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }*/
}