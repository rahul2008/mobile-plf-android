package com.philips.cl.di.dev.pa.security;

public interface KeyDecryptListener {
	
	/**
	 * Receive encrypted key.
	 *
	 * @param key 
	 */
	void keyDecrypt(String key, String deviceEui64) ;

}
