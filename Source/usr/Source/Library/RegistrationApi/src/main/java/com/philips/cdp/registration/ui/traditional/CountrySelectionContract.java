package com.philips.cdp.registration.ui.traditional;

import android.content.Context;

import com.philips.cdp.registration.dao.Country;

import java.util.ArrayList;

public interface CountrySelectionContract {

     void initRecyclerView();

     void updateRecyclerView(ArrayList<Country> countries);

     void setSelectedCountry(Country country);

     CountrySelectionFragment getCountrySelectionFragment();

     Context getUSRContext();

     RegistrationFragment getRegistrationFragment();

     void popCountrySelectionFragment();

     void notifyCountryChange(Country country);
}