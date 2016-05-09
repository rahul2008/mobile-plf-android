/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without 
the written consent of the copyright holder.

Project           : SaecoAvanti

File Name         : EventListner.java

Description       : TODOEnter description
Revision History: version 1: 
    Date: Jul 5, 2014
    Original author: Maruti Kutre
    Description: Initial version    
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.eventhelper;

/**
 * @author Maruti Kutre
 */
public interface EventListener {

    /**
     * take action on this event
     *
     * @param event
     */
    public void onEventReceived(String event);

}
