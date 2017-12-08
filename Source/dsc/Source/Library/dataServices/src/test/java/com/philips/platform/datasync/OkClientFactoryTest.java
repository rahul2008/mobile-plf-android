package com.philips.platform.datasync;

import com.squareup.okhttp.OkHttpClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import retrofit.client.OkClient;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sangamesh on 06/12/16.
 */
public class OkClientFactoryTest {

    @Mock
    OkHttpClient okHttpClient;
    private OkClientFactory okClientFactory;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        okClientFactory = new OkClientFactory();
    }

    @Test
    public void ShouldCreateCorrectly_WhenProvidedWithNonNullClient() throws Exception {
        OkClient okClient = okClientFactory.create(okHttpClient);
        assertThat(okClient).isNotNull();
    }

}