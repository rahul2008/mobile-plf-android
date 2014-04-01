package com.philips.cl.di.dev.pa.interfaces;


import java.util.List;

import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.datamodel.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;

/**
 * This interface will be implemented by DataParser to parse all types of response data from Server
 * @author 310124914
 *
 */
public interface DataParserInterface {
	/** Parse Air Purifier Event Data **/
	public AirPurifierEventDto parseAirPurifierEventData() ;
	/** Parse History Data **/
	//TODO - Parse History should return a history object
	//We will have to define a History object
	public List<IndoorHistoryDto> parseHistoryData() ;
	
	public void parseOutdoorAQIData() ;
	
	public List<Weatherdto> parseWeatherData() ;
	
	public AirPurifierEventDto parseAirPurifierEventDataFromCPP() ;
	
}
