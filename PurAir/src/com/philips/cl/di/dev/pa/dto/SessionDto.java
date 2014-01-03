package com.philips.cl.di.dev.pa.dto;

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
}
