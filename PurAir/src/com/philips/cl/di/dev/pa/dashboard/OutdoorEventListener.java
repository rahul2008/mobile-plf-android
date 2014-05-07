package com.philips.cl.di.dev.pa.dashboard;

import java.util.List;

import com.philips.cl.di.dev.pa.datamodel.City;


public interface OutdoorEventListener {

	void outdoorLocationDataReceived(List<City> cities);
}
