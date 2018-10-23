/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.insurance.Relationship;

import java.util.List;

public class THSRelationship {
    public List<Relationship> getRelationShipList() {
        return relationShipList;
    }

    public void setRelationShipList(List<Relationship> relationShipList) {
        this.relationShipList = relationShipList;
    }

    private List<Relationship> relationShipList;
}
