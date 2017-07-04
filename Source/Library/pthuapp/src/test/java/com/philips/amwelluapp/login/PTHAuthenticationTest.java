package com.philips.amwelluapp.login;

import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.consumer.ConsumerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class PTHAuthenticationTest {

    PTHAuthentication pthAuthentication;

    @Mock
    Authentication authenticationMock;

    @Mock
    ConsumerInfo consumerInfoMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pthAuthentication = new PTHAuthentication();
        pthAuthentication.setAuthentication(authenticationMock);
    }

    @Test
    public void needsToCompleteEnrollment() throws Exception {
        when(authenticationMock.needsToCompleteEnrollment()).thenReturn(true);
        boolean needsCompleteEnrollment =  pthAuthentication.needsToCompleteEnrollment();
        verify(authenticationMock).needsToCompleteEnrollment();
        assertTrue(needsCompleteEnrollment);
    }

    @Test(expected = NullPointerException.class)
    public void needsToCompleteEnrollmentThrowsNullPointerExceptionWhenAuthenticationObjectisnull() throws Exception {
        pthAuthentication = new PTHAuthentication();
        pthAuthentication.needsToCompleteEnrollment();
    }

    @Test
    public void getConsumerInfo() throws Exception {
        when(authenticationMock.getConsumerInfo()).thenReturn(consumerInfoMock);
        ConsumerInfo consumerInfo =  pthAuthentication.getConsumerInfo();
        verify(authenticationMock).getConsumerInfo();

        assertThat(consumerInfo).isNotNull();
        assertThat(consumerInfo).isInstanceOf(ConsumerInfo.class);
    }

    @Test
    public void isCredentialsSystemGenerated() throws Exception {
        when(authenticationMock.isCredentialsSystemGenerated()).thenReturn(true);
        boolean isCredentialsSystemGenerated =  pthAuthentication.isCredentialsSystemGenerated();
        verify(authenticationMock).isCredentialsSystemGenerated();
        assertTrue(isCredentialsSystemGenerated);
    }

    @Test
    public void getAuthentication() throws Exception {
        when(authenticationMock.getConsumerInfo()).thenReturn(consumerInfoMock);
        ConsumerInfo consumerInfo =  pthAuthentication.getConsumerInfo();
        verify(authenticationMock).getConsumerInfo();

        assertThat(consumerInfo).isNotNull();
        assertThat(consumerInfo).isInstanceOf(ConsumerInfo.class);
    }

    @Test
    public void setAuthentication() throws Exception {
        pthAuthentication.setAuthentication(authenticationMock);

        assertThat(pthAuthentication.getAuthentication()).isNotNull();
        assertThat(pthAuthentication.getAuthentication()).isInstanceOf(Authentication.class);
    }

}