/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.states;

public class StateContext {
    private AbstractBaseState baseState;

    public void start() {
        baseState.start(this);
    }

    public void setState(AbstractBaseState baseState) {
        this.baseState = baseState;
    }

    public AbstractBaseState getState() {
        return baseState;
    }
}
