/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dprdemo.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;

import org.joda.time.DateTime;

import java.util.Collection;

import javax.inject.Singleton;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmCreator implements BaseAppDataCreator {

    private final UuidGenerator uuidGenerator;

    @Singleton
    public OrmCreator(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;

    }

    @Override
    @NonNull
    public Moment createMoment(@NonNull final String creatorId, @NonNull final String subjectId, @NonNull String type) {
        return null;
    }

    @Override
    @NonNull
    public MomentDetail createMomentDetail(@NonNull final String type,
                                           @NonNull final Moment moment) {
        return null;
    }


    @Override
    @NonNull
    public Measurement createMeasurement(@NonNull final String type,
                                         @NonNull final MeasurementGroup MeasurementGroup) {
        return null;
    }

    @Override
    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                     @NonNull final Measurement measurement) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull MeasurementGroup measurementGroup) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull Moment moment) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroupDetail createMeasurementGroupDetail(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return null;
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull final String guid, final boolean inactive,
                                                         @NonNull DateTime lastModifiedTime, final int version) {
        return null;
    }


    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber) {

        return new OrmConsentDetail(type, status, version, deviceIdentificationNumber);
    }

    @NonNull
    @Override
    public Characteristics createCharacteristics(@NonNull String type, @NonNull String value, @NonNull Characteristics characteristics) {
        return null;
    }

    @NonNull
    @Override
    public Characteristics createCharacteristics(@NonNull String type, @NonNull String value) {
        return null;
    }

    @NonNull
    @Override
    public Settings createSettings(String type, String value) {
        return null;
    }





    //Insight
    @NonNull
    @Override
    public Insight createInsight() {
        return new Insight() {
            @Override
            public void setGUId(String s) {

            }

            @Override
            public void setLastModified(String s) {

            }

            @Override
            public void setInactive(boolean b) {

            }

            @Override
            public void setVersion(int i) {

            }

            @Override
            public void setRuleId(String s) {

            }

            @Override
            public void setSubjectId(String s) {

            }

            @Override
            public void setMomentId(String s) {

            }

            @Override
            public void setType(String s) {

            }

            @Override
            public void setTimeStamp(String s) {

            }

            @Override
            public void setTitle(String s) {

            }

            @Override
            public void setProgram_minVersion(int i) {

            }

            @Override
            public void setProgram_maxVersion(int i) {

            }

            @Override
            public String getGUId() {
                return null;
            }

            @Override
            public String getLastModified() {
                return null;
            }

            @Override
            public boolean isInactive() {
                return false;
            }

            @Override
            public int getVersion() {
                return 0;
            }

            @Override
            public String getRuleId() {
                return null;
            }

            @Override
            public String getSubjectId() {
                return null;
            }

            @Override
            public String getMomentId() {
                return null;
            }

            @Override
            public String getType() {
                return null;
            }

            @Override
            public String getTimeStamp() {
                return null;
            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public int getProgram_minVersion() {
                return 0;
            }

            @Override
            public int getProgram_maxVersion() {
                return 0;
            }

            @Nullable
            @Override
            public SynchronisationData getSynchronisationData() {
                return null;
            }

            @Override
            public void setSynchronisationData(SynchronisationData synchronisationData) {

            }

            @Override
            public void setSynced(boolean b) {

            }

            @Override
            public boolean getSynced() {
                return false;
            }

            @Override
            public void setId(int i) {

            }

            @Override
            public int getId() {
                return 0;
            }

            @Override
            public Collection<? extends InsightMetadata> getInsightMetaData() {
                return null;
            }

            @Override
            public void addInsightMetaData(InsightMetadata insightMetadata) {

            }
        };
    }

    @NonNull
    @Override
    public InsightMetadata createInsightMetaData(String key, String value, Insight insight) {
        return null;
    }

}
