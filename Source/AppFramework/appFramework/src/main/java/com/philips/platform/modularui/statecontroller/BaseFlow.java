package com.philips.platform.modularui.statecontroller;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public interface BaseFlow {

    Integer getNextState(String eventID);
}
