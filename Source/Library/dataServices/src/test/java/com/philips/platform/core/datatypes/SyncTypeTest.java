package com.philips.platform.core.datatypes;

import org.junit.Before;
import org.junit.Test;

public class SyncTypeTest {
    @Before
    public void setUp() {

    }

    @Test
    public void Should_call_fromId(){
        SyncType id = SyncType.fromId(SyncType.MOMENT.getId());
        assert (id).equals(SyncType.MOMENT);
    }

    @Test
    public void Should_call_fromId_for_Unknown(){
        SyncType id = SyncType.fromId(3);
        assert (id).equals(SyncType.UNKNOWN);
    }
}