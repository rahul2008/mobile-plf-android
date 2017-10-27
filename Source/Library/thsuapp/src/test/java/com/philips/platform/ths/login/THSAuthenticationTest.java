package com.philips.platform.ths.login;

import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.consumer.ConsumerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class THSAuthenticationTest {

    com.philips.platform.ths.login.THSAuthentication THSAuthentication;

    @Mock
    Authentication authenticationMock;

    @Mock
    ConsumerInfo consumerInfoMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSAuthentication = new THSAuthentication();
        THSAuthentication.setAuthentication(authenticationMock);
    }

    @Test
    public void needsToCompleteEnrollment() throws Exception {
        when(authenticationMock.needsToCompleteEnrollment()).thenReturn(true);
        boolean needsCompleteEnrollment =  THSAuthentication.needsToCompleteEnrollment();
        verify(authenticationMock).needsToCompleteEnrollment();
        assertTrue(needsCompleteEnrollment);
    }

    @Test(expected = NullPointerException.class)
    public void needsToCompleteEnrollmentThrowsNullPointerExceptionWhenAuthenticationObjectisnull() throws Exception {
        THSAuthentication = new THSAuthentication();
        THSAuthentication.needsToCompleteEnrollment();
    }

    @Test
    public void getConsumerInfo() throws Exception {
        when(authenticationMock.getConsumerInfo()).thenReturn(consumerInfoMock);
        ConsumerInfo consumerInfo =  THSAuthentication.getConsumerInfo();
        verify(authenticationMock).getConsumerInfo();

        assertThat(consumerInfo).isNotNull();
        assertThat(consumerInfo).isInstanceOf(ConsumerInfo.class);
    }

    @Test
    public void isCredentialsSystemGenerated() throws Exception {
        when(authenticationMock.isCredentialsSystemGenerated()).thenReturn(true);
        boolean isCredentialsSystemGenerated =  THSAuthentication.isCredentialsSystemGenerated();
        verify(authenticationMock).isCredentialsSystemGenerated();
        assertTrue(isCredentialsSystemGenerated);
    }

    @Test
    public void getAuthentication() throws Exception {
        when(authenticationMock.getConsumerInfo()).thenReturn(consumerInfoMock);
        ConsumerInfo consumerInfo =  THSAuthentication.getConsumerInfo();
        verify(authenticationMock).getConsumerInfo();

        assertThat(consumerInfo).isNotNull();
        assertThat(consumerInfo).isInstanceOf(ConsumerInfo.class);
    }

    @Test
    public void setAuthentication() throws Exception {
        THSAuthentication.setAuthentication(authenticationMock);

        assertThat(THSAuthentication.getAuthentication()).isNotNull();
        assertThat(THSAuthentication.getAuthentication()).isInstanceOf(Authentication.class);
    }

}