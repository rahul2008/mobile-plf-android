package com.philips.cl.di.dev.pa.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;

public class WeatherHourlyDataParser extends DefaultHandler {

	private List<Weatherdto> weatherList ;
	private Weatherdto weatherDto ;
	private StringBuilder builder;
	
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		weatherList = new ArrayList<Weatherdto>() ;
		weatherDto = new Weatherdto() ;
		builder = new StringBuilder() ;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if( qName.equalsIgnoreCase(ParserConstants.HOURLY)) {
			
		}
		else if(qName.equalsIgnoreCase(ParserConstants.STEP)) {
			weatherDto = new Weatherdto();
			weatherDto.setTime(attributes.getValue(ParserConstants.TIME)) ;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		builder.append(new String(ch,start,length)) ;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equalsIgnoreCase(ParserConstants.STEP)) {
			weatherList.add(weatherDto) ;
		}
		else if( qName.equalsIgnoreCase(ParserConstants.TEMPARATURE)) {
			weatherDto.setTempInCentigrade(Float.parseFloat(builder.toString())) ;
		}
		else if(qName.equalsIgnoreCase(ParserConstants.WIND_SPEED_HOURLY)) {
			weatherDto.setWindSpeed(Float.parseFloat(builder.toString())) ;
		}
		else if(qName.equalsIgnoreCase(ParserConstants.ICON)) {
			weatherDto.setWeatherDesc(builder.toString()) ;
		}
		builder.setLength(0);
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	
	public List<Weatherdto> getWeatherForecastHourlyList() {
		return weatherList;
	}
}
