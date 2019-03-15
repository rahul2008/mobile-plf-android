package com.philips.platform.pim.manager;

public class PIMLoginManager {
    PIMAuthManager pimAuthManager;
    PIMUserManager pimUserManager;

    public PIMLoginManager(PIMAuthManager authManager, PIMUserManager userManager) {
        pimAuthManager = authManager;
        pimUserManager = userManager;
    }

//    void login(OIDCConfig oidcConfig) {
//        pimAuthManager.performLoginWithAccessToken(new PIMListener(){
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
