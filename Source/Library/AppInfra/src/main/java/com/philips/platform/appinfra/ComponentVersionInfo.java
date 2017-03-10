/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra;

/**
 * Created by philips on 3/8/17.
 */

public interface ComponentVersionInfo {

    //returns component three letter acronym as defined at developer portal
   String getComponentId();

    //returns component version, which may include snapshot indication
   String getVersion();


}
