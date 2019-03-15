package com.philips.platform.pim.models;

public class PIMUser {
    private PIMHsdpUserProfile mHsdpUser;
    private PIMJanrainUserProfile mJanrainUser;

    public PIMHsdpUserProfile getmHsdpUser() {
        return mHsdpUser;
    }

    public void setmHsdpUser(PIMHsdpUserProfile mHsdpUser) {
        this.mHsdpUser = mHsdpUser;
    }

    public PIMJanrainUserProfile getmJanrainUser() {
        return mJanrainUser;
    }

    public void setmJanrainUser(PIMJanrainUserProfile mJanrainUser) {
        this.mJanrainUser = mJanrainUser;
    }
}
