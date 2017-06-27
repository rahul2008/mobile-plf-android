package com.philips.amwelluapp.registration;

import com.americanwell.sdk.entity.State;

public class PTHState {
    State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getCode(){
        return state.getCode();
    }

    public boolean isLegalResidence(){
        return state.isLegalResidence();
    }
}
