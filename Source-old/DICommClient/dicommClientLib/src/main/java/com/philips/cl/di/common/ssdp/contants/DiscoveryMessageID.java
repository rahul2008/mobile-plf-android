package com.philips.cl.di.common.ssdp.contants;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */

public enum DiscoveryMessageID {

	/**
	 * Field device discovered and lost
	 */
	DEVICE_DISCOVERED, DEVICE_LOST;

	/**
	 * 
	 * @param index
	 * @return
	 */
	public static DiscoveryMessageID getID(int index) {
		return (DiscoveryMessageID.values())[index];
	}
}
