package com.philips.platform.dscdemo.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.platform.dscdemo.DemoAppManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRegistrationHandler implements UserRegistrationInterface {
    @NonNull
    private final Context context;

    @NonNull
    private final User user;

    private boolean accessTokenRefreshInProgress;

    @NonNull
    private String accessToken = "";

    @Inject
    public UserRegistrationHandler(@NonNull final Context context, @NonNull final User user) {
        this.context = context;
        this.user = user;
    }

    public UserRegistrationHandler getInstance() {
        return this;
    }

    @NonNull
    private User getUser(final Context context) {
        return new User(context);
    }

    @Override
    public boolean isUserLoggedIn() {
        return new User(context).isUserSignIn();
    }

    @NonNull
    @Override
    public String getHSDPAccessToken() {
        if (accessToken.isEmpty() && !accessTokenRefreshInProgress) {
            accessToken = getUser(context).getHsdpAccessToken();
        }
        return accessToken;
    }

    @NonNull
    @Override
    public UserProfile getUserProfile() {
        final UserProfile userProfile;
        User user = new User(context);
        userProfile = new UserProfile(user.getGivenName(), user.getFamilyName(), user.getEmail(), user.getHsdpUUID());
        return userProfile;
    }

    @Override
    public synchronized void refreshAccessTokenUsingWorkAround() {
        if (accessTokenRefreshInProgress) {
            return;
        }
        accessTokenRefreshInProgress = true;
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(refreshLoginSessionRunnable);
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException ignored) {

            }
        }
        accessTokenRefreshInProgress = false;
    }

    private Runnable refreshLoginSessionRunnable = new Runnable() {
        @Override
        public void run() {
            user.refreshLoginSession(new RefreshLoginSessionHandler() {
                @Override
                public void onRefreshLoginSessionSuccess() {
                    accessToken = getUser(context).getHsdpAccessToken();
                    notifyLoginSessionResponse();
                }

                @Override
                public void onRefreshLoginSessionFailedWithError(int statusCode) {
                    Toast.makeText(context, "refresh token failed and status code is = " + statusCode, Toast.LENGTH_LONG).show();
                    notifyLoginSessionResponse();
                }

                @Override
                public void onRefreshLoginSessionInProgress(String s) {
                    accessTokenRefreshInProgress = true;
                }
            });
        }
    };

    private void notifyLoginSessionResponse() {
        synchronized (this) {
            notify();
        }
    }

    private void clearPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    @Override
    public String getHSDPUrl() {
        Object propertyForKey = DemoAppManager.getInstance().getAppInfra().getConfigInterface().getPropertyForKey(URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL,
                URConfigurationConstants.UR, new AppConfigurationInterface.AppConfigurationError());
        return propertyForKey.toString();
    }

    public void clearUserData(DBRequestListener dbRequestListener) {
        DataServicesManager manager = DataServicesManager.getInstance();
        manager.deleteAll(dbRequestListener);
        clearPreferences();
        accessToken = "";
    }

}