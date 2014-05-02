package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.philips.cl.di.dev.pa.datamodel.City;
import com.philips.cl.di.dev.pa.util.ALog;

public class OutdoorManager implements OutdoorEventListener {
	
	private List<OutdoorDto> outdoorLocations;
	
	private static OutdoorManager smInstance;
	
	private OutdoorDataUpdated outdoorDataUpdated;
	
	private OutdoorManager() {
		outdoorLocations = new ArrayList<OutdoorDto>();
		OutdoorController.getInstance().setOutdoorEventListener(this);
		OutdoorController.getInstance().startCitiesTask();
	}
	
	public void setOutdoorDataUpdatedListener(OutdoorDataUpdated outdoorDataUpdated) {
		this.outdoorDataUpdated = outdoorDataUpdated;
	}
	
	public static OutdoorManager getInstance() {
		if(smInstance == null) {
			smInstance = new OutdoorManager();
		}
		return smInstance;
	}
	
	public OutdoorDto getOutdoorDashboardData(int position) {
		if(outdoorLocations == null || outdoorLocations.size() <= 0) {
			return null;
		}
		if(outdoorLocations.get(position) == null) {
			return null;
		}
		return outdoorLocations.get(position);
	}

	@Override
	public void outdoorLocationDataReceived(List<City> cities) {
		addToOutdoorLocations(cities);
	}

	//Handle null case.
	private void addToOutdoorLocations(List<City> cities) {
		ALog.i(ALog.DASHBOARD, "OutdoorManager$addOutdoorLocations");
		Iterator<City> iter = cities.iterator();
		
		while(iter.hasNext()) {
			City city = iter.next();
			OutdoorDto outdoorDto = new OutdoorDto();
//			ALog.i(ALog.DASHBOARD, "outdoorDto " + (outdoorDto == null) + " city " + (city == null) + " gov " + (city.getGov() == null));
			if(city != null && city.getGov() != null && city.getWeather() != null) {
				outdoorDto.setAqi(city.getGov().getIdx());
				outdoorDto.setCityName(city.getKey());
				outdoorDto.setTemperature(city.getWeather().getTemp_c());
				outdoorDto.setUpdatedTime(city.getGov().getT());
				outdoorDto.setWeatherIcon(city.getWeather().getIcon());
				outdoorLocations.add(outdoorDto);
			}
		}
		
		outdoorDataUpdated.updateOutdoorData();
	}
}
