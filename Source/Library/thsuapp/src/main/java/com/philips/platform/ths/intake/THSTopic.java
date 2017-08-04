/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.visit.Topic;

public class THSTopic {

    Topic topic;

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    String getTitle(){
        return topic.getTitle();
    }

    String getDescription(){
        return topic.getDescription();
    }

    void setSelected(@NonNull boolean isSelected){
        topic.setSelected(isSelected);
    }

    boolean isSelected(){
        return topic.isSelected();
    }

}
