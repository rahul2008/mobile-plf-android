package com.philips.cdp.registration.ui.traditional;

import android.content.Context;

import com.philips.cdp.registration.CountryComparator;
import com.philips.cdp.registration.dao.Country;
import com.philips.cdp.registration.ui.utils.RegUtility;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class CountrySelectionPresenter {

    private final CountrySelectionContract countrySelectionContract;
    private  final Context context;

    public CountrySelectionPresenter(CountrySelectionContract countrySelectionContract) {
        this.countrySelectionContract = countrySelectionContract;
        this.context = countrySelectionContract.getUSRContext();
    }

    void fetchSupportedCountryList() {

        Set<Country> countryTreeSet=new TreeSet<>(new CountryComparator());
        String[] countryCodes = RegUtility.supportedCountryList().toArray(new String[RegUtility.supportedCountryList().size()]);
        for (String countryCode : countryCodes) {
            Country country = RegUtility.getCountry(countryCode,context);
            countryTreeSet.add(country);
        }
        countrySelectionContract.updateRecyclerView(new ArrayList<>(countryTreeSet));
    }
}