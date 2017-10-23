/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra;

/**
 * The interface for version Information.
 */

public interface ComponentVersionInfo {

    /**
     * Gets component ID.
     *
     * @return component three letter acronym as defined at developer portal
     * @since 2.1.0
     */
   String getComponentId();

    
    /**
     * Gets component version.
     *
     * @return component version, which may include snapshot indication
     * @since 2.1.0
     */
   String getVersion();


}
