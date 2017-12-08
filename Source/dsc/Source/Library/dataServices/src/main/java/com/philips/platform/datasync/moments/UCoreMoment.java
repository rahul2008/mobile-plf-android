/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class UCoreMoment {

    @Expose
    private String timestamp;

    @Expose
    @Nullable
    private String expirationDate;

    @Expose
    private String type;

    private String guid;
    private boolean inactive;
    private String lastModified;
    private String subjectId;
    private String creatorId;

    @Expose
    private int version;

    @Expose
    @Nullable
    private List<UCoreMeasurementGroups> measurementGroups;

    @Expose
    @Nullable
    private List<UCoreDetail> details;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(@NonNull final String guid) {
        this.guid = guid;
    }

    public boolean getInactive() {
        return inactive;
    }

    public void setInactive(final boolean inactive) {
        this.inactive = inactive;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(@NonNull final String lastModified) {
        this.lastModified = lastModified;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(@NonNull final String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(@NonNull final String creatorId) {
        this.creatorId = creatorId;
    }

    public String getType() {
        return type;
    }

    public void setType(@NonNull final String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Nullable
    public List<UCoreMeasurementGroups> getMeasurementGroups() {
        return measurementGroups;
    }

    public void setMeasurementGroups(@Nullable final List<UCoreMeasurementGroups> measurementGroups) {
        this.measurementGroups = measurementGroups;
    }

    public void addMeasurementGroup(final UCoreMeasurementGroups measurementGroup) {
        if (measurementGroups == null) {
            measurementGroups = new ArrayList<>();
        }
        measurementGroups.add(measurementGroup);
    }

    @Nullable
    public List<UCoreDetail> getDetails() {
        return details;
    }

    public void setDetails(@Nullable final List<UCoreDetail> details) {
        this.details = details;
    }
}