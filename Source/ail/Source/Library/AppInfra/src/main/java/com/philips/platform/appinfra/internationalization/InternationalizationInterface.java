/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.internationalization;

import java.io.Serializable;

/**
 * The interface Local interface.
 */
public interface InternationalizationInterface extends Serializable {

    /**
     * Gets String locale value .
     *
     * @return the String with Concatenated format
     * @since 1.1.0
     */
    String getUILocaleString();

    /**
     * Gets the full String locale value
     *
     * @return the String with the Full Concatenated format which is going to use ail_fullLocale
     * @since 2.2.0
     */
     String getBCP47UILocale();
}