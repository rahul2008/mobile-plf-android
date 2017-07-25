package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.insurance.Relationship;

import java.util.List;

/**
 * Created by philips on 7/17/17.
 */

public class THSRelationship {
    public List<Relationship> getRelationShipList() {
        return relationShipList;
    }

    public void setRelationShipList(List<Relationship> relationShipList) {
        this.relationShipList = relationShipList;
    }

    List<Relationship> relationShipList;
}
