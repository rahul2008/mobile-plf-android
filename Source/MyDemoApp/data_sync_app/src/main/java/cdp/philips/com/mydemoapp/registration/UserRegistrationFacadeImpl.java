package cdp.philips.com.mydemoapp.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.UserCredentials;
import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;

import org.joda.time.DateTime;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
public class UserRegistrationFacadeImpl implements UserRegistrationFacade {
    public static final boolean USE_COPPA_FLOW = false;
    public static final boolean TAGGING_ENABLED = true;
    public final static int ACCESS_TOKEN_KEEP_ALIVE_TIME_IN_HOURS = 1;
    static final String KEY_PROFILE_PHOTO_SET = "PROFILE_PHOTO_SET";

    // TODO: This I do not want
    @NonNull
    private final Context context;

    @NonNull
    private final User user;

    @NonNull
    private final RegistrationHelper registrationHelper;

    @NonNull
    private final Eventing eventing;


    @NonNull
    private SharedPreferences sharedPreferences;
    private DateTime accessTokenRefreshTime;
    private boolean accessTokenRefreshInProgress;
    private String accessToken = "";
    private Runnable refreshLoginSessionRunnable = new Runnable() {
        @Override
        public void run() {
            user.refreshLoginSession(new RefreshLoginSessionHandler() {
                @Override
                public void onRefreshLoginSessionSuccess() {
                    accessTokenRefreshTime = DateTime.now();
                    accessToken = getHsdpAccessToken();
                    notifyLoginSessionResponse();
                }

                @Override
                public void onRefreshLoginSessionFailedWithError(int statusCode) {
                    notifyLoginSessionResponse();
                }

                @Override
                public void onRefreshLoginSessionInProgress(String s) {

                }
            });
        }
    };
    private RegistrationConfiguration registrationConfiguration;
    private HSDPInfo hsdpInfo;

    @Nullable
    private String email;

    @Inject
    public UserRegistrationFacadeImpl(
            @NonNull final Context context,
            @NonNull final User user,
            @NonNull final RegistrationHelper registrationHelper,

            @NonNull final Eventing eventing,
            @NonNull final RegistrationConfiguration registrationConfiguration,
            @NonNull final HSDPInfo hsdpInfo) {
        this.context = context;
        this.user =  user;
        this.registrationHelper = registrationHelper;

        this.eventing = eventing;
        this.hsdpInfo = hsdpInfo;
        this.registrationConfiguration = registrationConfiguration;

    }

    private String getHsdpAccessToken() {
        if (getUser(context) == null) {
            return accessToken;
        }
        return getUser(context).getHsdpAccessToken();
    }

    @NonNull
    private User getUser(final Context context) {
        return new User(context);
    }

    public void init() {
        //This is as per Common component requirement think before removing
        PILLocaleManager pilLocaleManager = new PILLocaleManager(context);
        pilLocaleManager.setInputLocale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        registrationHelper.initializeUserRegistration(context.getApplicationContext());

    }

    @Override
    public boolean isUserLoggedIn() {
        return user.isUserSignIn();
    }

    @NonNull
    @Override
    public String getAccessToken() {
        refreshAccessTokenUsingWorkAround();
        return accessToken;
    }

    @NonNull
    @Override
    public UserProfile getUserProfile() {
        UserProfile userProfile = getUserProfileUserRegistrationPart();



        return userProfile;
    }

    @Override
    public void setUserSkippedOrAddedPhoto() {
        sharedPreferences.edit().putBoolean(KEY_PROFILE_PHOTO_SET, true).apply();
    }


    @NonNull
    private UserProfile getUserProfileUserRegistrationPart() {
        final UserProfile userProfile;
        if (user != null) {
            userProfile = new UserProfile(user.getGivenName(), user.getFamilyName(), user.getEmail(), user.getHsdpUUID());
        } else {
            userProfile = new UserProfile("", "", "", "");
        }
        return userProfile;
    }

    // TODO: We may have to ask the common component to take care of this
    private synchronized void refreshAccessTokenUsingWorkAround() {
        /*if (isAccessTokenStillValid() || accessTokenRefreshInProgress) {
            return;
        }*/
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(refreshLoginSessionRunnable);
        accessTokenRefreshInProgress = true;
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException ignored) {

            }
        }
        accessTokenRefreshInProgress = false;
    }

    private void notifyLoginSessionResponse() {
        synchronized (this) {
            notify();
        }
    }

    private boolean isAccessTokenStillValid() {
     //   return accessTokenRefreshTime != null && accessTokenRefreshTime.plusHours(ACCESS_TOKEN_KEEP_ALIVE_TIME_IN_HOURS).isAfter(DateTime.now());
        return false;
    }

    public void clearUserData() {
        accessTokenRefreshTime = null;
        eventing.post(new DataClearRequest());
        clearPreferences();


        email = null;
    }



    @Override
    public boolean isSameUser() {
        return email == null || email.length() == 0 || email.equals(getUserProfile().getEmail());
    }

    @Override
    public UserCredentials getUserCredentials() {
        return new UserCredentials(user.getHsdpUUID(), user.getHsdpAccessToken(), user.getJanrainUUID(), user.getAccessToken());
    }

    @Override
    public void setHsdpUrl() {

       /* hsdpInfo.setBaseURL("https://sandbox-ds-syncclient.cloud.pcftest.com");
        hsdpInfo.setApplicationName("DataCore");
        hsdpInfo.setSharedId("");
        hsdpInfo.setSecreteId("");

        RegistrationConfiguration.getInstance().setHSDPInfo(Configuration.DEVELOPMENT, hsdpInfo);*/
    }

    @Nullable
    private Map<String, String> getQueryParams(final String url, final int baseUrlIndex) {
        Map<String, String> paramsMap = parseQueryParameters(url.substring(baseUrlIndex + 1));
        if (paramsMap.isEmpty()) {
            return null;
        }
        return paramsMap;
    }

    private Configuration getEnvironment(String janrainEnv) {
        Configuration configuration = Configuration.DEVELOPMENT;
        switch (janrainEnv) {
            case "Development":
                configuration = Configuration.DEVELOPMENT;
                break;
            case "Production":
                configuration = Configuration.PRODUCTION;
                break;
            case "Evaluation":
                configuration = Configuration.EVALUATION;
                break;
            case "Staging":
                configuration = Configuration.STAGING;
                break;
            case "Testing":
                configuration = Configuration.TESTING;
                break;
            default:
        }
        return configuration;
    }

    private Map<String, String> parseQueryParameters(String query) {
        Map<String, String> queryPairsMap = new LinkedHashMap<>();
        String[] queryParamsPairs = query.split("&");
        for (String pair : queryParamsPairs) {
            int idx = pair.indexOf(":");
            queryPairsMap.put(pair.substring(0, idx), pair.substring(idx + 1));
        }
        return queryPairsMap;
    }

    private void clearPreferences() {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}