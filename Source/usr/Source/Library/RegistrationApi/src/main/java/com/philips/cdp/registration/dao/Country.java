package com.philips.cdp.registration.dao;

import java.util.Comparator;

public class Country implements Comparator<Country> {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }


    public String getName() {
        return name;
    }

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int compare(Country o1, Country o2) {

        return o1.getName().compareTo(o2.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Country other = (Country) obj;

        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return code.hashCode() + name.hashCode();
    }
}