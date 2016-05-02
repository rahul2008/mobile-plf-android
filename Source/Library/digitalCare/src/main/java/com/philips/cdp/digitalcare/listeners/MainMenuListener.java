/**
 * MainMenuListener is interface for handling custom button handling for home screen(digitalcare).
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 1 June 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.listeners;

public interface MainMenuListener {

	/**
	 * This method returns MainMenu button callbacks.
	 * @param mainMenuItem
	 * @return
	 */
	public boolean onMainMenuItemClicked(String mainMenuItem);
}
