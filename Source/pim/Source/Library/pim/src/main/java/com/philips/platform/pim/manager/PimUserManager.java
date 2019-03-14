package com.philips.platform.pim.manager;

import com.philips.platform.pim.models.OIDCConfig;
import com.philips.platform.pim.models.PimUserProfile;
import com.philips.platform.pim.rest.PimRestClient;
import com.philips.platform.pim.rest.PimRestClientInterface;
import com.philips.platform.pim.rest.UserProfileRequest;

public class PimUserManager {

    public PimUserProfile fetchuserprofile(){
        //Fetch from server and make PimUserProfile
        //return new PimUserProfile();
    }

    private void makeUserProfileRequest(OIDCConfig config) {
        PimRestClientInterface userInfoRequest = new UserProfileRequest(config);
        PimRestClient pimRestClient = new PimRestClient();
        pimRestClient.invokeRequest(userInfoRequest, response -> handleSuccess(), error -> handleError());
    }

    private void handleError() {

    }

    private void handleSuccess() {

    }
}
