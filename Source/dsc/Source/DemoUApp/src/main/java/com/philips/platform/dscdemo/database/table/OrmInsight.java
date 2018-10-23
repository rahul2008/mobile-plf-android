package com.philips.platform.dscdemo.database.table;

import android.support.annotation.Nullable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.dscdemo.database.EmptyForeignCollection;
import com.philips.platform.dscdemo.database.annotations.DatabaseConstructor;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;
import java.util.Collection;


@DatabaseTable
public class OrmInsight implements Insight, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String rule_id;

    @DatabaseField
    private String subjectID;

    @DatabaseField
    private String moment_id;

    @DatabaseField
    private String type;

    @DatabaseField
    private String time_stamp;

    @DatabaseField
    private String title;

    @DatabaseField
    private int program_min_version;

    @DatabaseField
    private int program_max_version;

    @DatabaseField
    private boolean synced;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private OrmSynchronisationData synchronisationData;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<OrmInsightMetaData> ormInsightMetaDatas = new EmptyForeignCollection<>();

    @DatabaseField
    private DateTime expirationDate;

    @DatabaseConstructor
    public OrmInsight() {
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
    public OrmSynchronisationData getSynchronisationData() {
        return synchronisationData;
    }

    @Override
    public void setSynchronisationData(SynchronisationData synchronisationData) {
        this.synchronisationData = (OrmSynchronisationData) synchronisationData;
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
    public Collection<? extends OrmInsightMetaData> getInsightMetaData() {
        return ormInsightMetaDatas;
    }

    @Override
    public void addInsightMetaData(InsightMetadata insightMetadata) {
        ormInsightMetaDatas.add((OrmInsightMetaData) insightMetadata);
    }

    @Override
    public void setExpirationDate(DateTime timestamp) {
        expirationDate = timestamp;
    }

    @Override
    public DateTime getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String toString() {
        return "[OrmInsight, InsightID = " + this.synchronisationData.getGuid() + ", MomentID = " + moment_id + ", Title = " + title;
    }
}
