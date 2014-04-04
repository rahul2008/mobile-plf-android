package com.philips.cl.di.dev.pa.datamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CityDetails {

	ProvObject provs;

	Map<String, City> cities;

	List<String> hotCities;

	/**
	 * @return the cities
	 */
	public Map<String, City> getCities() {
		return cities;
	}

	/**
	 * @return the hot_cities
	 */
	public List<String> getHotCities() {
		return hotCities;
	}

	/**
	 * @return the provs
	 */
	public ProvObject getProvs() {
		return provs;
	}

	/**
	 * @param pCities
	 *            the cities to set
	 */
	public void setCities(HashMap<String, City> pCities) {
		this.cities = pCities;
	}

	/**
	 * @param hotCities
	 *            the hot_cities to set
	 */
	public void setHotCities(List<String> hotCities) {
		this.hotCities = hotCities;
	}

	/**
	 * @param provs
	 *            the provs to set
	 */
	public void setProvs(ProvObject provs) {
		this.provs = provs;
	}

	public City getDetails(String pcity) {
		City param = new City();
		param.setKey(pcity);
		boolean found = false;
		for (Entry entry : cities.entrySet()) {
	        if(pcity.equals(((City)entry.getValue()).getKey())){
	        	param = (City) entry.getValue();
	        	found = true;
	        }
        }
		if(!found) {
			param = null;
		}
		return param;
	}

	
	@Override
	public String toString() {
		return "provs " + provs + " cities " + cities + " hot_cities " + hotCities;
	}

}
