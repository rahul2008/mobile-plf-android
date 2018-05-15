package com.philips.platform.util;

import android.util.Log;

import com.philips.platform.core.datatypes.Moment;

import org.mockito.ArgumentMatcher;

import java.util.List;

public class MomentListVersionMatcher implements ArgumentMatcher<List<Moment>> {
    private static final String TAG = "MomentListVersionMatche";
    private final int expectedVersion;

    public MomentListVersionMatcher(int expectedVersion) {
        this.expectedVersion = expectedVersion;
    }

    @Override
    public boolean matches(List<Moment> argument) {
        boolean isCorrect = true;
        for (Moment m : argument) {
            if(m.getSynchronisationData() == null) {
                isCorrect = false;
                continue;
            }
            Log.d(TAG, "matches: " + m.getSynchronisationData().getVersion());
            isCorrect &= m.getSynchronisationData().getVersion() == expectedVersion;
        }
        return isCorrect;
    }
}
