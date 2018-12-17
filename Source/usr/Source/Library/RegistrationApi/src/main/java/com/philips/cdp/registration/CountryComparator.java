package com.philips.cdp.registration;

import com.philips.cdp.registration.dao.Country;

import java.util.Comparator;

/**
 * Created by philips on 2/21/18.
 */

public class CountryComparator implements Comparator<Country> {
    @Override
    public int compare(Country country1, Country country2) {
       return country1.getName().compareTo(country2.getName());

    }
}
