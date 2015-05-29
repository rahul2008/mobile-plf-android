package com.philips.cl.di.digitalcare;

/**
 * ConsumerProductInfo is the abstract class. Vertical must have to extend this
 * class and pass all the required information, required by DigitalCare to hit
 * several cloud servers like ATOS, CDLS, PRX etc.
 * 
 * @author: ritesh.jha@philips.com
 * @since: May 27, 2015
 */
public abstract class ConsumerProductInfo {
	public abstract String getGroup();

	public abstract String getSector();

	public abstract String getCategory();

	public abstract String getSubCategory();

	public abstract String getCtn();

	public abstract String getProductTitle();
}
