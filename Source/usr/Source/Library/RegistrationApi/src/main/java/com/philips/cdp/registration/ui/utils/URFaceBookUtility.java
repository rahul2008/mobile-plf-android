package com.philips.cdp.registration.ui.utils;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.ui.traditional.mobile.FaceBookContractor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by philips on 5/16/18.
 */

public class URFaceBookUtility implements FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

    private final static String TAG = URFaceBookUtility.class.getSimpleName();
    public static final String FIELDS = "fields";
    public static final String ID_EMAIL = "id,email";
    public static final String EMAIL = "email";

    private final FaceBookContractor faceBookContractor;

    private static List<String> FACEBOOK_PERMISSION_LIST;

    static {
        FACEBOOK_PERMISSION_LIST = Arrays.asList("public_profile", "email");
    }

    public URFaceBookUtility(FaceBookContractor faceBookContractor) {
        this.faceBookContractor = faceBookContractor;
    }

    public CallbackManager getCallBackManager() {
        return CallbackManager.Factory.create();
    }

    public void registerFaceBookCallBack() {
        RLog.i(TAG, "registerFaceBookCallBack");
        try {
            LoginManager.getInstance().registerCallback(faceBookContractor.getCallBackManager(), this);
        }catch (Exception e){
            RLog.i(TAG,e.getMessage());
            RLog.i(TAG,"Facebook Activities are not present in proposition Manifest file");
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        requestUserProfile(loginResult);
    }

    @Override
    public void onCancel() {
        RLog.i(TAG, "onCancel()");
        faceBookContractor.onFaceBookCancel();
        faceBookContractor.doHideProgressDialog();
    }

    @Override
    public void onError(FacebookException error) {
        if (error instanceof FacebookAuthorizationException) {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
        }
        RLog.i(TAG, error.getMessage());
        faceBookContractor.doHideProgressDialog();
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {

        if (response.getError() != null) {
            faceBookContractor.doHideProgressDialog();
            RLog.i(TAG, response.getError().getErrorMessage());
        } else {
            try {
                if (response.getJSONObject() != null) {
                    faceBookContractor.onFaceBookEmailReceived(response.getJSONObject().get(EMAIL).toString());
                }
            } catch (JSONException e) {
                RLog.i(TAG, e.getMessage());
                faceBookContractor.doHideProgressDialog();
            }
        }
    }

    void requestUserProfile(LoginResult loginResult) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, ID_EMAIL);
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    public void startAccessTokenAuthForFacebook(User user, Activity activity, SocialProviderLoginHandler socialProviderLoginHandler, String accessToken, String mergeToken) {
        user.startTokenAuthForNativeProvider(activity,
                RegConstants.SOCIAL_PROVIDER_FACEBOOK, socialProviderLoginHandler, mergeToken, accessToken);
    }

    public void startFaceBookLogIn() {
        RLog.i(TAG,"startFaceBookLogIn");
        //Making sure to logout Facebook Instance before logging in
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().logInWithReadPermissions(faceBookContractor.getHomeFragment(),FACEBOOK_PERMISSION_LIST);
    }
}
