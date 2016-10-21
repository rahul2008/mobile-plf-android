/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.flowmanagerjson.pojo;

/**
 * Project           : Lumea
 * File Name         : AppFlowStates
 * Description       : Model to AppFlowStates object.
 * Revision History: version 1:
 * Description: Initial version
 */
@SuppressWarnings("serial")
public class AppFlowStates implements java.io.Serializable {

    private AppFlowNextState[] nextState;

    private String state;

    public AppFlowNextState[] getNextState() {
        return this.nextState;
    }

    public void setNextState(AppFlowNextState[] nextState) {
        this.nextState = nextState;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
