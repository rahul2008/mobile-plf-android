package com.philips.cdp.digitalcare.listeners;

import com.philips.platform.uappframework.listener.UappListener;

/**
 * This is the "ConsumerCare" Common interface used across the application to serve the
 * Social Provider Click Listener , Supper Screen Button Click Listener & the ProductMenu Listener.
 * <p/>
 * Created by naveen@philips.com on 8/16/2016.
 */
public interface CcListener extends UappListener {


    /**
     * @param mainMenuItem
     * @return Click Event of the Button in  the Support Screen
     */
    boolean onMainMenuItemClicked(String mainMenuItem);


    /**
     * Returns the booleanValue onClick on the Products Menu
     *
     * @param productMenu
     * @return
     */
    boolean onProductMenuItemClicked(String productMenu);


    /**
     * Retuns the click event onPressing the respective SocialProvider Button.
     *
     * @param socialProviderItem
     * @return
     */
    boolean onSocialProviderItemClicked(String socialProviderItem);
}
