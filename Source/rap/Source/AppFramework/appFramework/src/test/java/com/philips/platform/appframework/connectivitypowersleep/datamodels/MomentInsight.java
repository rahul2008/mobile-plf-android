package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import android.support.annotation.Nullable;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;
import com.philips.platform.core.datatypes.SynchronisationData;

import java.util.Collection;

/**
 * Created by philips on 11/14/17.
 */

public class MomentInsight implements Insight {
    private String ruleId;
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
        this.ruleId = s;
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
        return ruleId;
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
}
