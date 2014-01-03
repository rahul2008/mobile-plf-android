package com.philips.cl.di.dev.pa.utils;
import java.util.List;

import org.json.JSONException;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.cl.di.dev.pa.constants.ParserConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.dto.Weatherdto;
import com.philips.cl.di.dev.pa.interfaces.DataParserInterface;

/***
 * This class it used to parse data for AirPurifier event
 * @author 310124914
 *
 */
public class DataParser implements DataParserInterface {
	//private static final String TAG = DataParser.class.getSimpleName() ;
	private String dataToParse ;

	public DataParser(String dataToParse) {
		this.dataToParse = dataToParse ;
	}

	@Override
	public AirPurifierEventDto parseAirPurifierEventData() {
		AirPurifierEventDto airPurifierEvent = null ;
		try {			
			JSONObject jsonObj = new JSONObject(dataToParse) ;
			airPurifierEvent = new AirPurifierEventDto() ;			

			airPurifierEvent.setMachineMode(jsonObj.getString(ParserConstants.MACHINE_MODE)) ;
			airPurifierEvent.setFanSpeed(jsonObj.getString(ParserConstants.FAN_SPEED)) ;
			airPurifierEvent.setPowerMode(jsonObj.getString(ParserConstants.POWER_MODE)) ;
			airPurifierEvent.setIndoorAQI(Integer.parseInt(jsonObj.getString(ParserConstants.AQI))) ;
			airPurifierEvent.setAqiL(Integer.parseInt(jsonObj.getString(ParserConstants.AQI_LIGHT))) ;
			airPurifierEvent.setAqiThreshold(Integer.parseInt(jsonObj.getString(ParserConstants.AQI_THRESHOLD))) ;
			airPurifierEvent.setDtrs(Integer.parseInt(jsonObj.getString(ParserConstants.DTRS))) ;
			airPurifierEvent.setFilterStatus1(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_1))) ;
			airPurifierEvent.setFilterStatus2(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_2))) ;
			airPurifierEvent.setFilterStatus3(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_3))) ;
			airPurifierEvent.setFilterStatus4(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_4))) ;
			airPurifierEvent.setReplaceFilter1(jsonObj.getString(ParserConstants.CLEAN_FILTER_1)) ;
			airPurifierEvent.setReplaceFilter2(jsonObj.getString(ParserConstants.REP_FILTER_2)) ;
			airPurifierEvent.setReplaceFilter3(jsonObj.getString(ParserConstants.REP_FILTER_3)) ;
			airPurifierEvent.setReplaceFilter4(jsonObj.getString(ParserConstants.REP_FILTER_4)) ;
			airPurifierEvent.setChildLock(Integer.parseInt(jsonObj.getString(ParserConstants.CL))) ;
			airPurifierEvent.setpSensor(Integer.parseInt(jsonObj.getString(ParserConstants.PSENS))) ;
			airPurifierEvent.settFav(Integer.parseInt(jsonObj.getString(ParserConstants.TFAV))) ;			

		} catch (JSONException e) {
			return null ;
		}

		return airPurifierEvent ;
	}

	@Override
	public void parseHistoryData() {

	}


	@Override
	public void parseOutdoorAQIData() {
		Gson gson = new GsonBuilder().create() ;
		OutdoorAQIEventDto outdoorAQI = gson.fromJson(dataToParse, OutdoorAQIEventDto.class) ;
		SessionDto.getInstance().setOutdoorEventDto(outdoorAQI) ;
	}

	@Override
	public List<Weatherdto> parseWeatherData() {
		return new WeatherDataParser().parseWeatherData(dataToParse) ;
	}
}
