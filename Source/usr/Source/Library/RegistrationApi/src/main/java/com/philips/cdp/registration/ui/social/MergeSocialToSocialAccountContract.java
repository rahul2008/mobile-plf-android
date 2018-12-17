package com.philips.cdp.registration.ui.social;


import android.app.Activity;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.ui.traditional.mobile.FaceBookContractor;
import com.philips.cdp.registration.ui.utils.URFaceBookUtility;

interface MergeSocialToSocialAccountContract extends FaceBookContractor{

    void connectionStatus(boolean isOnline);

    void mergeStatus(boolean isOnline);

    void mergeSuccess();

    void mergeFailure(UserRegistrationFailureInfo error);

    void mergeFailureIgnored();

    Activity getActivityContext();

    URFaceBookUtility getURFaceBookUtility();
}
