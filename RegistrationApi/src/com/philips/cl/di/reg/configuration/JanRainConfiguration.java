/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without 
the written consent of the copyright holder.

Project           : RegistrationSampleApp

File Name         : JanRainConfiguration.java

Description       : TODOEnter description
Revision History: version 1: 
    Date: May 21, 2015
    Original author: Maruti Kutre
    Description: Initial version    
----------------------------------------------------------------------------*/

package com.philips.cl.di.reg.configuration;


/**
 * @author Maruti Kutre
 *
 */
public class JanRainConfiguration {
	
	private RegistrationClientId clientIds;

	/**
     * @return the clientIds
     */
    public RegistrationClientId getClientIds() {
	    return clientIds;
    }

	/**
     * @param clientIds the clientIds to set
     */
    public void setClientIds(RegistrationClientId clientIds) {
	    this.clientIds = clientIds;
    }

}
