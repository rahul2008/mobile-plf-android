package com.philips.cl.di.dev.pa.datamodel;

import java.util.HashMap;
import java.util.List;

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
	
	private DeviceDto deviceDto ;
	private DeviceWifiDto deviceWifiDto ;
	private String Eui64;
	
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

	public DeviceDto getDeviceDto() {
		return deviceDto;
	}

	public void setDeviceDto(DeviceDto deviceDto) {
		this.deviceDto = deviceDto;
	}
	
	public DeviceWifiDto getDeviceWifiDto() {
		return deviceWifiDto;
	}

	public void setDeviceWifiDto(DeviceWifiDto deviceWifiDto) {
		this.deviceWifiDto = deviceWifiDto;
	}

	public IndoorTrendDto getIndoorTrendDto() {
		return indoorTrendDto;
	}

	private OutdoorAQIEventDto outdoorEventDto ;
	
	public OutdoorAQIEventDto getOutdoorEventDto() {
		return outdoorEventDto;
	}

	public void setOutdoorEventDto(OutdoorAQIEventDto outdoorEventDto) {
		this.outdoorEventDto = outdoorEventDto;
	}

	public List<Weatherdto> getWeatherDetails() {
		return weatherDetails;
	}

	public void setWeatherDetails(List<Weatherdto> weatherDetails) {
		this.weatherDetails = weatherDetails;
	}
	
	
	public void setAppEui64(String eui64) {
		this.Eui64 = eui64;
	}
	
	public String getAppEui64() {
		return Eui64;		
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
