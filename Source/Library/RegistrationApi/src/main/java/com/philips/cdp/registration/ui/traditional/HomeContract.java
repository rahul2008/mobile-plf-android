package com.philips.cdp.registration.ui.traditional;

interface HomeContract {
  

    void enableControlsOnNetworkConnectionArraival();

    void disableControlsOnNetworkConnectionGone();

    void hideCountrySelctionLabel();

    void showCountrySelctionLabel();

    void updateHomeCountry(String selectedCountryCode);
}
