/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.flowmanagerjson.pojo;

/**
 * Project           : Lumea
 * File Name         : AppFlowModel
 * Description       : Model to AppFlowModel object.
 * Revision History: version 1:
 * Description: Initial version
 */
@SuppressWarnings("serial")
public class AppFlowModel implements java.io.Serializable {

    private AppFlow appflow;

    public AppFlow getAppFlow() {
        return this.appflow;
    }

    public void setAppFlow(AppFlow appflow) {
        this.appflow = appflow;
    }
}
