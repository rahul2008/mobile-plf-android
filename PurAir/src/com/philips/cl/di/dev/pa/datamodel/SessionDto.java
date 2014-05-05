package com.philips.cl.di.dev.pa.datamodel;

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
	
	private DeviceDto deviceDto ;
	private DeviceWifiDto deviceWifiDto ;
	private String Eui64;
	private String macAddress;
	
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

	public void setIndoorTrendDto(IndoorTrendDto indoorTrendDto) {
		this.indoorTrendDto = indoorTrendDto;
	}

	public CityDetails getCityDetails() {
		return cityDetails;
	}

	public void setCityDetails(CityDetails cityDetails) {
		this.cityDetails = cityDetails;
	}

	private OutdoorAQIEventDto outdoorEventDto ;
	private CityDetails cityDetails ;
	
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
	
	public String getEui64() {
		return Eui64;		
	}

	/**
	 * Private Constructor for Session object
	 * This will be a singleton class, since there will be only one session for application.
	 */
	private SessionDto() {
	}
	
	public static SessionDto getSessionDto() {
		return sessionDto;
	}

	public static void setSessionDto(SessionDto sessionDto) {
		SessionDto.sessionDto = sessionDto;
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
	
	public static void reset() {
		sessionDto = new SessionDto() ;
	}
}
