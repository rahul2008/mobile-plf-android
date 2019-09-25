package com.philips.cdp.di.ecs.model.disclaimer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Disclaimers contains list of disclaimers.
 */
public class Disclaimers implements Serializable {
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
