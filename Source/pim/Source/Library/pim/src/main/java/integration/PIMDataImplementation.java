package integration;

import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pim.manager.PIMUserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PIMDataImplementation implements PIMUserDataInterface {
    private PIMUserManager pimUserManager;
    private Map<String, String> userProfil;

    public PIMDataImplementation(PIMUserManager pimUserManager) {
        this.pimUserManager = pimUserManager;
    }

    @Override
    public void logoutSession(LogoutSessionListener logoutSessionListener) {

    }

    @Override
    public void refreshSession(RefreshSessionListener refreshSessionListener) {

    }

    @Override
    public void refetchUserDetails(RefetchUserDetailsListener userDetailsListener) {

    }

    @Override
    public HashMap<String, Object> getUserDetails(ArrayList<String> detailKeys) throws Exception {
        return null;
    }

    @Override
    public boolean authorizeLoginToHSDP(HSDPAuthenticationListener hsdpListener) {
        return false;
    }

    @Override
    public UserLoggedInState getUserLoggedInState() {
        return null;
    }

    @Override
    public void addUserDataInterfaceListener(UserDataListener listener) {

    }

    @Override
    public void removeUserDataInterfaceListener(UserDataListener listener) {

    }
}
