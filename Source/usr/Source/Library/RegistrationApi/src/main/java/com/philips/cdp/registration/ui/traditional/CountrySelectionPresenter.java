package com.philips.cdp.registration.ui.traditional;

import android.content.Context;

import com.philips.cdp.registration.dao.Country;
import com.philips.cdp.registration.ui.utils.RegUtility;

import java.util.ArrayList;

public class CountrySelectionPresenter {

    private final CountrySelectionContract countrySelectionContract;
    private  final Context context;

    public CountrySelectionPresenter(CountrySelectionContract countrySelectionContract, Context context) {
        this.countrySelectionContract = countrySelectionContract;
        this.context = context;
    }

    void fetchSupportedCountryList() {
        ArrayList<Country> countries = new ArrayList<Country>();
        String[] countryCodes = RegUtility.supportedCountryList().toArray(new String[RegUtility.supportedCountryList().size()]);
        for (String countryCode : countryCodes) {
            Country country = RegUtility.getCountry(countryCode,context);
            countries.add(country);
        }
        countrySelectionContract.updateRecyclerView(countries);
    }
}