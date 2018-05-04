package com.philips.platform.util;

import com.philips.platform.core.datatypes.Moment;

import org.mockito.ArgumentMatcher;

import java.util.List;

public class MomentsListSizeMatcher implements ArgumentMatcher<List<Moment>> {

    private final int expectedSize;

    public MomentsListSizeMatcher(int expectedSize) {
        this.expectedSize = expectedSize;
    }

    @Override
    public boolean matches(List<Moment> argument) {
        return argument.size() == expectedSize;
    }
}
