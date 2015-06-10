package com.philips.cl.di.dev.pa.datamodel;

import java.util.HashMap;
import java.util.List;

import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;

/**
 * This class will have the session objects required for the Air Purifier application
 * @author 310124914
 *
 */
public class SessionDto {	
	
	private static SessionDto sessionDto ;
	private FilterStatusDto filterStatusDto ;
	private List<Weatherdto> weatherDetails ;
	
	private IndoorTrendDto indoorTrendDto ;
	private HashMap<String, IndoorTrendDto> indoorTrendsHashMap;
	
	private DevicePortProperties deviceDto ;
	private WifiPortProperties deviceWifiDto ;
	/**
	 * Get the indoor trend from Hashmap using Purifier eui64
	 * @param purifierEui64
	 * @return
	 */
	public IndoorTrendDto getIndoorTrendDto(String purifierEui64) {
		IndoorTrendDto inDto = null;
		if (indoorTrendsHashMap.containsKey(purifierEui64)) {
			inDto = indoorTrendsHashMap.get(purifierEui64);
		}
		return inDto;
	}
	
	public void setIndoorTrendDto(String eui64, IndoorTrendDto indoorTrendDto) {
		indoorTrendsHashMap.put(eui64, indoorTrendDto) ;
	}

	public DevicePortProperties getDeviceDto() {
		return deviceDto;
	}

	public void setDeviceDto(DevicePortProperties deviceDto) {
		this.deviceDto = deviceDto;
	}
	
	public WifiPortProperties getDeviceWifiDto() {
		return deviceWifiDto;
	}

	public void setDeviceWifiDto(WifiPortProperties deviceWifiDto) {
		this.deviceWifiDto = deviceWifiDto;
	}

	public IndoorTrendDto getIndoorTrendDto() {
		return indoorTrendDto;
	}

	public List<Weatherdto> getWeatherDetails() {
		return weatherDetails;
	}

	public void setWeatherDetails(List<Weatherdto> weatherDetails) {
		this.weatherDetails = weatherDetails;
	}	
	
	/**
	 * Private Constructor for Session object
	 * This will be a singleton class, since there will be only one session for application.
	 */
	private SessionDto() {
		indoorTrendsHashMap = new HashMap<String, IndoorTrendDto>();
	}
	
	public  FilterStatusDto getFilterStatusDto() {
		return filterStatusDto;
	}

	public  void setFilterStatusDto(FilterStatusDto filterStatusDto) {
		this.filterStatusDto = filterStatusDto;
	}
	
	/**
	 * Get the instance of the Session Object
	 * @return
	 */
	public static SessionDto getInstance() {
		synchronized(SessionDto.class) {
			if ( null == sessionDto ) {
				sessionDto = new SessionDto() ;
			}
		}
		return sessionDto ;
	}
}
