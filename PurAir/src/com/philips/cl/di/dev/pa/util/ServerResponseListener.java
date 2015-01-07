package com.philips.cl.di.dev.pa.util;

/**
 * The listener interface for receiving serverResponse events.
 * The class that is interested in processing a serverResponse
 * event implements this interface. When
 * the serverResponse event occurs, that object's appropriate
 * method is invoked.
 *
 */
public interface ServerResponseListener {
	
	void receiveServerResponse(int responseCode, String responseData, String type);
	void receiveServerResponse(int responseCode, String responseData, String type, String areaId) ;
}
