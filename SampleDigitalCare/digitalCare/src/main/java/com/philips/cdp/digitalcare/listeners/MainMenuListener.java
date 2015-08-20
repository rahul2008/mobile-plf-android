package com.philips.cdp.digitalcare.listeners;


/**
 * MainMenuListener is interface for handling custom button handling for home screen(digitalcare). 
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 1 June 2015
 */

public interface MainMenuListener {

	/**
	 * This method returns MainMenu button callbacks.
	 * @param mainMenuItem
	 * @return
	 */
	public boolean onMainMenuItemClicked(String mainMenuItem);
}
