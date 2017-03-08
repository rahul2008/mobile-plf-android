package com.philips.platform.core.datatypes;

import android.support.annotation.Nullable;


import java.io.Serializable;

public interface Insight extends BaseAppData, Serializable {

    String MOMENT_NEVER_SYNCED_AND_DELETED_GUID = "-1";

    String GU_ID = "guid";
    String LAST_MODIFIED = "last_modified";
    String INACTIVE = "inactive";
    String VERSION = "VERSION";
    String RULE_ID = "rule_id";
    String SUBJECT_ID = "subjectID";
    String MOMENT_ID = "moment_id";
    String TYPE = "type";
    String TIME_STAMP = "time_stamp";
    String TITLE = "title";
    String PROGRAM_MIN_VERSION = "program_min_version";
    String PROGRAM_MAX_VERSION = "program_max_version";
    String METADATA_AVG = "metadata_avg";
    String METADATA_MAX = "metadata_max";
    String METADATA_MIN = "metadata_min";

    void setGU_ID(String GU_ID);

    void setLastModified(String lastModified);

    void setInactive(boolean inactive);

    void setVersion(int version);

    void setRuleId(String ruleId);

    void setSubjectId(String subjectId);

    void setMomentId(String momentId);

    void setType(String type);

    void setTimeStamp(String timeStamp);

    void setTitle(String title);

    void setProgram_minVersion(int program_minversion);

    void setProgram_maxVersion(int program_maxversion);

    void setMetadataAvg(int metadataAvg);

    void setMetadataMin(int metadataMin);

    void setMetadataMax(int metadataMax);

    String getGUId();

    String getLastModified();

    boolean isInactive();

    int getVersion();

    String getRuleId();

    String getSubjectId();

    String getMomentId();

    String getType();

    String getTimeStamp();

    String getTitle();

    int getProgram_minVersion();

    int getProgram_maxVersion();

    int getMetadataAvg();

    int getMetadataMin();

    int getMetadataMax();

    @Nullable
    com.philips.platform.core.datatypes.SynchronisationData getSynchronisationData();

    void setSynchronisationData(com.philips.platform.core.datatypes.SynchronisationData synchronisationData);

    void setSynced(boolean isSynced);

    boolean getSynced();

    void setId(int id);

    int getId();

}
