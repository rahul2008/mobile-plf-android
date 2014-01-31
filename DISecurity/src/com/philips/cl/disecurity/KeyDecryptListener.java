package com.philips.cl.disecurity;

public interface KeyDecryptListener {
	
	/**
	 * Receive encrypted key.
	 *
	 * @param key 
	 */
	public void keyDecrypt(String key) ;

}
