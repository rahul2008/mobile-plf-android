/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.flowmanagerjson.pojo;

/**
 * Project           : Lumea
 * File Name         : AppFlowNextState
 * Description       : Model to AppFlowNextState object.
 * Revision History: version 1:
 * Description: Initial version
 */
@SuppressWarnings("serial")
public class AppFlowNextState implements java.io.Serializable {

    private String[] condition;
    private String state;

    public String[] getCondition() {
        return this.condition;
    }

    public void setCondition(String[] condition) {
        this.condition = condition;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
