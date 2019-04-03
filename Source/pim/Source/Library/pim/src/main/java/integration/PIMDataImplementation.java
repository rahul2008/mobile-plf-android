package integration;

import android.content.Context;

import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDetailsListener;
import com.philips.platform.pim.manager.PIMUserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PIMDataImplementation implements PIMUserDataInterface {
    private PIMUserManager pimUserManager;
    private Map<String, String> userProfil;
    private Context mContext;

    public PIMDataImplementation(Context context, PIMUserManager pimUserManager) {
        mContext = context;
        pimUserManager = pimUserManager;
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
    public String getJanrainUUID() {
        return null;
    }

    @Override
    public String getJanrainAccessToken() {
        return null;
    }

    @Override
    public String getHSDPAccessToken() {
        return null;
    }

    @Override
    public String getHSDPUUID() {
        return null;
    }

    @Override
    public boolean isUserLoggedIn(Context context) {
        return false;
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
    public void authorizeHsdp(com.philips.platform.pif.DataInterface.USR.listeners.HSDPAuthenticationListener hsdpAuthenticationListener) {

    }

    @Override
    public void refreshLoginSession(RefreshListener refreshListener) {

    }

    @Override
    public void logOut(LogoutListener logoutListener) {

    }

    @Override
    public void refetch(UserDetailsListener userDetailsListener) {

    }

    @Override
    public void updateMarketingOptInConsent(UserDetailsListener userDetailsListener) {

    }

    @Override
    public void registerLogOutListener(LogoutListener logoutListener) {

    }

    @Override
    public void unregisterLogOutListener(LogoutListener logoutListener) {

    }

    @Override
    public void addUserDataInterfaceListener(UserDataListener listener) {

    }

    @Override
    public void removeUserDataInterfaceListener(UserDataListener listener) {

    }
}
