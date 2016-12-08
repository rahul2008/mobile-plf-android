package com.philips.platform.core.datatypes;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by indrajitkumar on 07/12/16.
 */
public class ConsentDetailStatusTypeTest {

    @Test
    public void shouldReturnID_WhenConsentTypeIscalled() {
        assertThat(ConsentDetailStatusType.UNKNOWN).isEqualTo(ConsentDetailStatusType.fromId(200));
        assertThat(ConsentDetailStatusType.ACCEPTED).isEqualTo(ConsentDetailStatusType.fromId(201));
        assertThat(ConsentDetailStatusType.REFUSED).isEqualTo(ConsentDetailStatusType.fromId(202));
    }

}