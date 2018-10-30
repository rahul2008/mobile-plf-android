/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import android.content.Context;

import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.ths.R;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.practice.THSPracticesListCallback;
import com.philips.platform.ths.sdkerrors.THSSDKError;

import javax.net.ssl.HttpsURLConnection;

public class THSRefreshToken implements THSLoginCallBack<THSAuthentication, THSSDKError>{

    boolean isRefreshTokenRequestedBefore;
    Context context;
    THSRefreshTokenCallBack mThsRefreshCallBack;

    THSRefreshToken(){
        isRefreshTokenRequestedBefore = false;
    }

    @Override
    public void onLoginResponse(THSAuthentication thsAuthentication, THSSDKError thssdkError) {

        AmwellLog.i(AmwellLog.LOG, "in THSRefreshToken.onLoginResponse");

        if (!checkIfAuthSuccess(thssdkError)){
            AmwellLog.i(AmwellLog.LOG, "in THSRefreshToken.onLoginResponse, Auth not success hence call refreshToken");
            refreshToken(this);
        }else if(mThsRefreshCallBack!=null){
            mThsRefreshCallBack.onSuccess();
        }

        AmwellLog.i(AmwellLog.LOG, "in THSRefreshToken.onLoginResponse, notifyAll the waiting threads");
    }

    public boolean checkIfAuthSuccess(THSSDKError thssdkError) {
        AmwellLog.i(AmwellLog.LOG, "in THSRefreshToken.checkIfAuthSuccess");

        if (thssdkError.getSdkError() != null && thssdkError.getHttpResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            AmwellLog.i(AmwellLog.LOG, "in THSRefreshToken.checkIfAuthSuccess UNAUTHERIZED");
            AmwellLog.d(AmwellLog.LOG,"OnLogin Response of Authenticate call - " + "UNAUTHORIZED");
            if (checkIfRefreshTokenWasTriedBefore()) {
                AmwellLog.e("in THSRefreshToken.checkIfAuthSuccess ", thssdkError.getSdkError().toString());
                return false;
            }
            AmwellLog.d(AmwellLog.LOG,"in THSRefreshToken.checkIfAuthSuccess - Before calling refresh signon, isRefreshToken made true");
            isRefreshTokenRequestedBefore = true;
            return false;
        }
        return true;
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        AmwellLog.i(AmwellLog.LOG, "in THSRefreshToken.onLoginFailure, notify all threads for failure, return auth as null and make context null");
        if(mThsRefreshCallBack!=null){
            mThsRefreshCallBack.onFailure(throwable);
        }
        context =null;
    }

    public void refreshToken(final THSLoginCallBack thsLoginCallBack) {

        AmwellLog.i(AmwellLog.LOG,"Inside THSRefreshToken.refreshToken of user registration");

        THSManager.getInstance().getUser(context).refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                AmwellLog.i(AmwellLog.LOG,"Inside THSRefreshToken.refreshToken onRefreshLoginSessionSuccess");

                if(context!= null) {
                    AmwellLog.i(AmwellLog.LOG,"Inside THSRefreshToken.refreshToken onRefreshLoginSessionSuccess - context not null");
                    THSManager.getInstance().getThsConsumer(context).setHsdpToken(THSManager.getInstance().getUser(context).getHsdpAccessToken());
                    try {
                        THSManager.getInstance().authenticateMutualAuthToken(context,thsLoginCallBack);
                    } catch (AWSDKInstantiationException e) {
                        e.printStackTrace();
                    }
                }else {
                    AmwellLog.i(AmwellLog.LOG,"Inside THSRefreshToken.refreshToken onRefreshLoginSessionSuccess - context null");
                    thsLoginCallBack.onLoginFailure(new Exception());
                }

                AmwellLog.i(AmwellLog.LOG,"Notify All waiting threads from user refresh token success");

            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int i) {
                AmwellLog.d(AmwellLog.LOG,"In onRefreshLoginSessionFailedWithError" + i);

                AmwellLog.i(AmwellLog.LOG,"Notify All waiting threads from user onRefreshLoginSessionFailedWithError");

                thsLoginCallBack.onLoginFailure(new Exception(context.getString(R.string.ths_refresh_signon_failed)));
            }

            @Override
            public void onRefreshLoginSessionInProgress(String s) {

            }
        });
    }

    private boolean checkIfRefreshTokenWasTriedBefore() {
        if (isRefreshTokenRequestedBefore) {
            isRefreshTokenRequestedBefore = false;
            return true;
        }
        return false;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setRefreshCallBack(THSRefreshTokenCallBack thsRefreshTokenCallBack){
        this.mThsRefreshCallBack = thsRefreshTokenCallBack;
    }

}
