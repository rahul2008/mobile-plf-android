package com.philips.platform.core.datatypes;

import com.philips.platform.datasync.insights.UCoreMetaData;

import java.io.Serializable;

/**
 * Created by sangamesh on 16/01/17.
 */
public interface Insight extends BaseAppData, Serializable {

    String GU_ID="guid";
    String LAST_MODIFIED="last_modified";
    String INACTIVE="inactive";
    String VERSION="VERSION";
    String RULE_ID="rule_id";
    String SUBJECT_ID="subjectID";
    String MOMENT_ID="moment_id";
    String TYPE="type";
    String TIME_STAMP="time_stamp";
    String TITLE="title";
    String PROGRAM_MIN_VERSION="program_min_version";
    String PROGRAM_MAX_VERSION="program_max_version";
    String METADATA_AVG="metadata_avg";
    String METADATA_MAX="metadata_max";
    String METADATA_MIN="metadata_min";


    public void setGU_ID(String GU_ID) ;

    public void setLastModified(String lastModified) ;

    public void setInactive(boolean inactive) ;

    public void setVersion(int version) ;

    public void setRuleId(String ruleId) ;

    public void setSubjectId(String subjectId) ;

    public void setMomentId(String momentId);

    public void setType(String type);

    public void setTimeStamp(String timeStamp);

    public void setTitle(String title);

    public void setProgram_minVersion(int program_minversion) ;

    public void setProgram_maxVersion(int program_maxversion) ;

    public void setMetadataAvg(String metadataAvg);
    public void setMetadataMin(String metadataMin);
    public void setMetadataMax(String metadataMax);

    public String getGUId();

    public String getLastModified() ;

    public boolean isInactive();

    public int getVersion();

    public String getRuleId();

    public String getSubjectId();

    public String getMomentId();

    public String getType();

    public String getTimeStamp();
    public String getTitle();

    public int getProgram_minVersion();

    public int getProgram_maxVersion();

    public String getMetadataAvg();
    public String getMetadataMin();
    public String getMetadataMax();

}
