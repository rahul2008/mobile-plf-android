package com.philips.cdp.di.ecs.prx.Disclaimer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Disclaimers {
    @SerializedName("disclaimer")
    @Expose
    private List<Disclaimer> disclaimer = new ArrayList<Disclaimer>();

    /**
     * No args constructor for use in serialization
     */
    public Disclaimers() {
    }

    /**
     * @param disclaimer
     */
    public Disclaimers(List<Disclaimer> disclaimer) {
        this.disclaimer = disclaimer;
    }

    public List<Disclaimer> getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(List<Disclaimer> disclaimer) {
        this.disclaimer = disclaimer;
    }
}
