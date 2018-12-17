package com.philips.cdp.registration.ui.traditional.mobile;

import com.facebook.CallbackManager;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;

/**
 * Created by philips on 5/16/18.
 */

public interface FaceBookContractor {

    void initFacebookLogIn();

    void onFaceBookCancel();

    void onFaceBookEmailReceived(String email);

    void startFaceBookLogin();

    void doHideProgressDialog();

    void startAccessTokenAuthForFacebook();

    CallbackManager getCallBackManager();

    RegistrationBaseFragment getHomeFragment();
}
