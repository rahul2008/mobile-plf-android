package com.philips.platform.pim.manager;

public class PimLoginManager {
    PimAuthManager pimAuthManager;
    PimUserManager pimUserManager;

    public PimLoginManager(PimAuthManager authManager, PimUserManager userManager) {
        pimAuthManager = authManager;
        pimUserManager = userManager;
    }

//    void login(OIDCConfig oidcConfig) {
//        pimAuthManager.performLoginWithAccessToken(new PimListener(){
//
//            @Override
//            public void onSuccess() {
//                pimUserManager.fetchuserprofile();
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }
}
