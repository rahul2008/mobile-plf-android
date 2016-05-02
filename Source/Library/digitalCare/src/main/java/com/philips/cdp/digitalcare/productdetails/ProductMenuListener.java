/**
 * ProductMenuButtonClickListener is interface for handling custom button
 * handling for "View Product Details" screen.
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 1 June 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.productdetails;


public interface ProductMenuListener {

	/**
	 * Returns the booleanValue onClick on the Products Menu
	 * @param productMenu
	 * @return
	 */
	public boolean onProductMenuItemClicked(String productMenu);
}
