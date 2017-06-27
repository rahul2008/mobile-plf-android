package com.philips.amwelluapp.practice;

import com.americanwell.sdk.entity.practice.Practice;

import java.util.List;

//TODO: Review Comment - Spoorti - add other getter and setter that SDk is providing
public class PTHPractice {
    List<Practice> practices;

    public List<Practice> getPractices() {
        return practices;
    }

    public void setPractices(List<Practice> practice) {
        this.practices = practice;
    }


}
