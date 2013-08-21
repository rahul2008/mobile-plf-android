package com.philips.cl.di.dev.pa.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto ;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;


public class OutdoorAQIParser extends DefaultHandler  { 
	private static final String TAG = OutdoorAQIParser.class.getSimpleName() ;

	/*** List object to store list of AQI values ***/
	public List<OutdoorAQIEventDto> list ;
	/**  StringBuilder to store the attributes **/
	private StringBuilder builder;
    /*** OutdoorAQI object to store the each item in the XML ***/
	private OutdoorAQIEventDto outdoorAQIObj ;

	/**
	 * This method will be called at the start of XML Parsing
	 * Initialize the list of OutdoorAQI
	 */
	@Override
	public void startDocument() throws SAXException {

		/******* Create ArrayList To Store XmlValuesModel object ******/
		list = new ArrayList<OutdoorAQIEventDto> ();
		builder = new StringBuilder() ;
	}


	/***
	 * This method will be called at the start of each XML element
	 * Initialize all the list of values and the objects which will be used to 
	 * store the XML contents
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 */

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		/****  When New XML Node initiating to parse this function called *****/
		if(localName.equalsIgnoreCase("item")) {
			outdoorAQIObj = new OutdoorAQIEventDto();
		}
		else if(localName.equalsIgnoreCase("AQI")) {
			builder.setLength(0) ;
		}
		else if( localName.equalsIgnoreCase("ReadingDateTime")) {
			builder.setLength(0) ;
		}
	}


	/***
	 * This method will be called during the end of each XML element.
	 * The values in the element will get stored in the attributes amd will be used here when the 
	 * element ends
	 * @param uri
	 * @param localName
	 * @param qName
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if(localName.equalsIgnoreCase("item")){

			/** finished reading a job xml node, add it to the arraylist **/
			list.add( outdoorAQIObj );

		}
		else if( localName.equalsIgnoreCase("AQI")) {
			outdoorAQIObj.setOutdoorAQI(Integer.parseInt(builder.toString())) ;
		}

		else if ( localName.equalsIgnoreCase("ReadingDateTime")) {
			outdoorAQIObj.setSyncDateTime(builder.toString()) ;
		}
	}


	/***
	 * Read the value of each xml NODE
	 * @param ch
	 * @param start
	 * @param length
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		/******  Read the characters and append them to the buffer  ******/
		String tempString = new String(ch, start, length);
		builder.append(tempString);
	}
	
	/**
	 * This method will be called at the end of XML Parsing or the XML document
	 * Process the parsed Data
	 */
	@Override
	public void endDocument() throws SAXException {
		Log.i(TAG, ""+ list.size()) ;
		
	}
}