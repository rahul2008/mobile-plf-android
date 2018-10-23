/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.testing.verticals.table;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.InsightMetadata;

import java.io.Serializable;

public class TestInsightMetaData implements InsightMetadata, Serializable {

    public static final long serialVersionUID = 11L;

    private int id;

    private String meta_key;

    private String meta_value;

    private TestInsight testInsight;

    TestInsightMetaData() {
    }

    public TestInsightMetaData(@NonNull final String meta_key, @NonNull final String meta_value, TestInsight testInsight) {
        this.meta_key = meta_key;
        this.meta_value = meta_value;
        this.testInsight = testInsight;
    }

    @Override
    public String getKey() {
        return meta_key;
    }

    @Override
    public void setKey(String key) {
        this.meta_key = key;
    }

    @Override
    public String getValue() {
        return meta_value;
    }

    @Override
    public void setValue(String value) {
        this.meta_value = value;
    }

    @Override
    public Insight getInsight() {
        return testInsight;
    }
}
