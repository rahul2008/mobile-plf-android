package com.philips.testing.verticals;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by indrajitkumar on 12/12/16.
 */
public class AssertHelper {
    public static void assertEquals(final Moment expected, final Moment actual) {
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getDateTime()).isNotNull();
        assertThat(actual.getCreatorId()).isNotNull();
        assertThat(actual.getSubjectId()).isNotNull();
        assertThat(actual.getType()).isNotNull();

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getDateTime()).isEqualTo(expected.getDateTime());
        assertThat(actual.getCreatorId()).isEqualTo(expected.getCreatorId());
        assertThat(actual.getSubjectId()).isEqualTo(expected.getSubjectId());
        assertThat(actual.getType()).isEqualTo(expected.getType());
    }

    public static void assertEquals(final MeasurementDetail expected, final MeasurementDetail actual) {
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getValue()).isNotNull();

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getValue()).isEqualTo(expected.getValue());
    }

    public static void assertEquals(final Measurement expected, final Measurement actual) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getDateTime()).isNotNull();
        assertThat(actual.getValue()).isEqualTo(expected.getValue());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getDateTime()).isEqualTo(expected.getDateTime());
        assertThat(actual.getValue()).isEqualTo(expected.getValue());
    }

    public static void assertEquals(final MomentDetail expected, final MomentDetail actual) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getValue()).isEqualTo(expected.getValue());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getValue()).isEqualTo(expected.getValue());
    }

    //public static void assertEquals(ConsentDetail expectedConsentDetail, ConsentDetail actualConsentDetail) {
        //assertThat(expectedConsentDetail.getId()).isEqualTo(actualConsentDetail.getId());
        //assertThat(expectedConsentDetail.getCreatorId()).isEqualTo(actualConsentDetail.getCreatorId());
       // assertThat(expectedConsentDetail.getDateTime()).isEqualTo(actualConsentDetail.getDateTime());
       // assertThat(expectedConsentDetail.isSynchronized()).isEqualTo(actualConsentDetail.isSynchronized());
    //}

    public static void assertEquals(ConsentDetail expectedConsentDetail, ConsentDetail actualConsentDetail) {
        assertThat(expectedConsentDetail.getId()).isEqualTo(actualConsentDetail.getId());
        assertThat(expectedConsentDetail.getType()).isEqualTo(actualConsentDetail.getType());
        assertThat(expectedConsentDetail.getStatus()).isEqualTo(actualConsentDetail.getStatus());
        assertThat(expectedConsentDetail.getVersion()).isEqualTo(actualConsentDetail.getVersion());
    }
}
