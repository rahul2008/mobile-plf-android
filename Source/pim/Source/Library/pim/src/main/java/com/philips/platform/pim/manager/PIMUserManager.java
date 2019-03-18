package com.philips.platform.pim.manager;

import com.philips.platform.pim.models.OIDCConfig;
import com.philips.platform.pim.models.PIMUserProfile;
import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.rest.PIMRestClientInterface;
import com.philips.platform.pim.rest.UserProfileRequest;

class PIMUserManager {

    public PIMUserProfile fetchuserprofile(){
        //Fetch from server and make PIMUserProfile
        return new PIMUserProfile();
    }

    private void makeUserProfileRequest(OIDCConfig config) {
        PIMRestClientInterface userInfoRequest = new UserProfileRequest(config);
        PIMRestClient pimRestClient = new PIMRestClient();
        pimRestClient.invokeRequest(userInfoRequest, response -> handleSuccess(), error -> handleError());
    }

    private void handleError() {

    }

    private void handleSuccess() {

    }
}
