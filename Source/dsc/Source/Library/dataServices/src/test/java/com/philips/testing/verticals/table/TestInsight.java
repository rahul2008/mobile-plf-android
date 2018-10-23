/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.testing.verticals.table;

import android.support.annotation.Nullable;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;
import com.philips.platform.core.datatypes.SynchronisationData;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestInsight implements Insight, Serializable {

    public static final long serialVersionUID = 11L;

    private int id;

    private String rule_id;

    private String subjectID;

    private String moment_id;

    private String type;

    private String time_stamp;

    private String title;

    private int program_min_version;

    private int program_max_version;

    private boolean synced;

    private DateTime expiration_date;

    private TestSynchronisationData synchronisationData;

    List<TestInsightMetaData> testInsightMetaData = new ArrayList<>();

    public TestInsight() {
        synchronisationData = new TestSynchronisationData(null, false, null, -1);
    }

    @Override
    public String toString() {
        return "[TestInsight, InsightID = " + synchronisationData.getGuid() + ", MomentID = " + moment_id + ", Title = " + title;
    }

    @Override
    public void setRuleId(String ruleId) {
        this.rule_id = ruleId;
    }

    @Override
    public void setSubjectId(String subjectId) {
        this.subjectID = subjectId;
    }

    @Override
    public void setMomentId(String momentId) {
        this.moment_id = momentId;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setTimeStamp(String timeStamp) {
        this.time_stamp = timeStamp;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setProgram_minVersion(int program_minVersion) {
        this.program_min_version = program_minVersion;
    }

    @Override
    public void setProgram_maxVersion(int program_maxversion) {
        this.program_max_version = program_maxversion;
    }

    @Override
    public String getRuleId() {
        return rule_id;
    }

    @Override
    public String getSubjectId() {
        return subjectID;
    }

    @Override
    public String getMomentId() {
        return moment_id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTimeStamp() {
        return time_stamp;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getProgram_minVersion() {
        return program_min_version;
    }

    @Override
    public int getProgram_maxVersion() {
        return program_max_version;
    }


    @Nullable
    @Override
    public TestSynchronisationData getSynchronisationData() {
        return synchronisationData;
    }

    @Override
    public void setSynchronisationData(SynchronisationData synchronisationData) {
        this.synchronisationData = (TestSynchronisationData) synchronisationData;
    }

    @Override
    public void setSynced(boolean isSynced) {
        this.synced = isSynced;
    }

    @Override
    public boolean getSynced() {
        return synced;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Collection<? extends TestInsightMetaData> getInsightMetaData() {
        return testInsightMetaData;
    }

    @Override
    public void addInsightMetaData(InsightMetadata insightMetadata) {
        testInsightMetaData.add((TestInsightMetaData) insightMetadata);
    }

    @Override
    public void setExpirationDate(DateTime timestamp) {
        expiration_date = timestamp;
    }

    @Override
    public DateTime getExpirationDate() {
        return expiration_date;
    }
}
