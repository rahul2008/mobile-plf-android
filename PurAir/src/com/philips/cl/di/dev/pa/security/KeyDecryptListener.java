package com.philips.cl.di.dev.pa.security;

public interface KeyDecryptListener {
	
	/**
	 * Receive encrypted key.
	 *
	 * @param key 
	 */
	public void keyDecrypt(String key) ;

}
