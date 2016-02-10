package com.philips.cdp.digitalcare.listeners;


/**
 * <p> This class helps If you are using he DigitalCare Component features inside your Application Fragment Mananger.
 * This listener helps to update the title & back/close image in the ActionBar</p>
 */
public interface ActionbarUpdateListener {

    /**
     * For updating the ActionBar on every Fragments supported by DigitalCare Component.
     *
     * @param titleActionbar
     * @param hamburgerIconAvaialable
     */
    public void updateActionbar(String titleActionbar,
                                Boolean hamburgerIconAvaialable);
}
