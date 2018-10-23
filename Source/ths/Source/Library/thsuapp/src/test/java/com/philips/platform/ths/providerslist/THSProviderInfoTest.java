/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderVisibility;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class THSProviderInfoTest {

    THSProviderInfo thsProviderInfo;

    @Mock
    ProviderInfo providerInfoMock;

    @Mock
    PracticeInfo practiceInfo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsProviderInfo = new THSProviderInfo();

    }

    @Test
    public void testSetGet(){
        thsProviderInfo.setTHSProviderInfo(providerInfoMock);
        final ProviderInfo providerInfo = thsProviderInfo.getProviderInfo();
        assertNotNull(providerInfo);
        assertThat(providerInfo).isInstanceOf(ProviderInfo.class);
        when(thsProviderInfo.getVisibility()).thenReturn(ProviderVisibility.OFFLINE);
        assertEquals(thsProviderInfo.getVisibility(),ProviderVisibility.OFFLINE);
        when(thsProviderInfo.getRating()).thenReturn(10);
        assertEquals(thsProviderInfo.getRating(),10);
        when(thsProviderInfo.getGender()).thenReturn(Gender.FEMALE);
        assertEquals(thsProviderInfo.getGender(),Gender.FEMALE);
        when(thsProviderInfo.getPracticeInfo()).thenReturn(practiceInfo);
        assertEquals(thsProviderInfo.getPracticeInfo(),practiceInfo);
        when(thsProviderInfo.getSourceId()).thenReturn("Testing");
        assertEquals(thsProviderInfo.getSourceId(),"Testing");
        when(thsProviderInfo.getWaitingRoomCount()).thenReturn(10);
        assertTrue((Integer)thsProviderInfo.getWaitingRoomCount() == 10);
    }



}
