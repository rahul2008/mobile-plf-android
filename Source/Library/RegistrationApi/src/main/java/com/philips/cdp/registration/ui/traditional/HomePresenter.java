package com.philips.cdp.registration.ui.traditional;


import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.appinfra.servicediscovery.*;

import java.util.*;

import javax.inject.*;

public class HomePresenter implements NetworkStateListener {


    @Inject
    NetworkUtility networkUtility;

    @Inject
    AppConfiguration appConfiguration;

    @Inject
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    private HomeContract homeContract;

    public HomePresenter(HomeContract homeContract) {
        URInterface.getComponent().inject(this);
        this.homeContract = homeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void cleanUp() {
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if (isOnline) {
            homeContract.enableControlsOnNetworkConnectionArraival();
        } else {
            homeContract.disableControlsOnNetworkConnectionGone();
        }
    }


    public void updateHomeControls() {
        if (networkUtility.isNetworkAvailable()) {
            homeContract.enableControlsOnNetworkConnectionArraival();
        } else {
            homeContract.disableControlsOnNetworkConnectionGone();
        }
    }

    public void configureCountrySelection() {
        String mShowCountrySelection = appConfiguration.getShowCountrySelection();
        RLog.d(RLog.SERVICE_DISCOVERY, " Country Show Country Selection :" + mShowCountrySelection);
        if (mShowCountrySelection != null) {
            if (mShowCountrySelection.equalsIgnoreCase("false")) {
                homeContract.hideCountrySelctionLabel();
            } else {
                homeContract.showCountrySelctionLabel();
            }
        }
    }


    private ArrayList<Country> recentSelectedCountry = new ArrayList<>();

    public void addToRecent(String countryCode) {
        Country country = new Country(countryCode, new Locale("", countryCode).getDisplayCountry());
        recentSelectedCountry.add(0, country);
    }

    public ArrayList<Country> getRecentSelectedCountry(){
        return recentSelectedCountry;
    }


    public void initServiceDiscovery() {
        serviceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
            @Override
            public void onSuccess(String s, SOURCE source) {
                RLog.d(RLog.SERVICE_DISCOVERY, " Country Sucess :" + s);
                String selectedCountryCode;
                if (RegUtility.supportedCountryList().contains(s.toUpperCase())) {
                    selectedCountryCode = s.toUpperCase();
                } else {
                    selectedCountryCode = RegUtility.getFallbackCountryCode();
                }
                addToRecent(selectedCountryCode);
                serviceDiscoveryInterface.setHomeCountry(selectedCountryCode);
                homeContract.updateHomeCountry(selectedCountryCode);

            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                RLog.d(RLog.SERVICE_DISCOVERY, " Country Error :" + s);
                String selectedCountryCode = RegUtility.getFallbackCountryCode();
                addToRecent(selectedCountryCode);
                serviceDiscoveryInterface.setHomeCountry(selectedCountryCode);
                homeContract.updateHomeCountry(selectedCountryCode);
            }
        });
    }

    public List<String> getProviders(String countryCode) {
        return RegistrationConfiguration.getInstance().getProvidersForCountry(countryCode);
    }
}
