/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.insights;

import java.util.Map;

public class UCoreInsight {
    private String guid;
    private String lastModified;
    private boolean inactive;
    private int version;
    private String ruleId;
    private String subjectId;
    private String momentId;
    private String type;
    private String timeStamp;
    private String title;
    private int program_minversion;
    private int program_maxversion;
    private Map<String, String> metadata;

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProgram_minversion(int program_minversion) {
        this.program_minversion = program_minversion;
    }

    public void setProgram_maxversion(int program_maxversion) {
        this.program_maxversion = program_maxversion;
    }

    public String getGuid() {
        return guid;
    }

    public String getLastModified() {
        return lastModified;
    }

    public boolean isInactive() {
        return inactive;
    }

    public int getVersion() {
        return version;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getMomentId() {
        return momentId;
    }

    public String getType() {
        return type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public int getProgram_minversion() {
        return program_minversion;
    }

    public int getProgram_maxversion() {
        return program_maxversion;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
