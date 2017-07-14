package com.philips.platform.ths.practice;

import com.americanwell.sdk.entity.practice.Practice;

import java.util.List;

//TODO: Review Comment - Spoorti - add other getter and setter that SDk is providing
//TODO: Review Comment - Spoorti - There is no wrapper provided for Practise
public class THSPractice {
    List<Practice> practices;

    public List<Practice> getPractices() {
        return practices;
    }

    public void setPractices(List<Practice> practice) {
        this.practices = practice;
    }


}
