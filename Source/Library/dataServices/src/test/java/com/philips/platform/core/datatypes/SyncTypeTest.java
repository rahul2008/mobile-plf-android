package com.philips.platform.core.datatypes;

import org.junit.Before;
import org.junit.Test;

import static com.philips.platform.core.datatypes.SyncType.MOMENT;

public class SyncTypeTest {
    @Before
    public void setUp() {

    }

    @Test
    public void Should_call_fromId(){
        SyncType id = SyncType.fromId(MOMENT.getId());
        assert (id).equals(MOMENT);
    }

    @Test
    public void Should_call_fromId_for_Unknown(){
        SyncType id = SyncType.fromId(3);
        assert (id).equals(SyncType.UNKNOWN);
    }

    @Test
    public void Should_call_getDescription(){
        String momentdescription = SyncType.MOMENT.getDescription();
        assert (momentdescription).equals("moment");
    }
}