package com.philips.cdp.digitalcare;

/**
 *  <p> It is the abstract Product information configurable class.</p>
 *   <p> This is the mandotory class, must used by the integrating application to pass the object of {@link ConsumerProductInfo}
 *   object during initialization of the DigitalCare component.</p>
 *   <p>Hint:  For reference please glance at the demo sample</p>
 */
public abstract class ConsumerProductInfo {
	public abstract String getGroup();

	public abstract String getSector();

	public abstract String getCategory();

	public abstract String getCatalog();

	public abstract String getSubCategory();

	public abstract String getCtn();

	public abstract String getProductTitle();

	public abstract String getProductReviewUrl();
}
