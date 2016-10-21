/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.flowmanagerjson.pojo;

/**
 * Project           : Lumea
 * File Name         : AppFlow
 * Description       : Model to AppFlow object.
 * Revision History: version 1:
 * Description: Initial version
 */
@SuppressWarnings("serial")
public class AppFlow implements java.io.Serializable {

    private String firstState;

    private AppFlowStates[] states;

    public String getFirstState() {
        return this.firstState;
    }

    public void setFirstState(String firstState) {
        this.firstState = firstState;
    }

    public AppFlowStates[] getStates() {
        return this.states;
    }

    public void setStates(AppFlowStates[] states) {
        this.states = states;
    }
}
