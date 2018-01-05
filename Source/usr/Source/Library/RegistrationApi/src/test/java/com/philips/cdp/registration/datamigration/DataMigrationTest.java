package com.philips.cdp.registration.datamigration;

import android.content.Context;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/30/17.
 */
@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class DataMigrationTest {

    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private Context contextMock;

    DataMigration dataMigration;

    String JR_CAPTURE_SIGNED_IN_USER = "jr_capture_signed_in_user";

    @Mock
    FileInputStream fileInputStreamMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);

      //  Mockito.when(contextMock.openFileInput(JR_CAPTURE_SIGNED_IN_USER)).thenReturn(fileInputStreamMock);
        dataMigration = new DataMigration(contextMock);
    }

    @Test(expected = NullPointerException.class)
    public void checkFileEncryptionStatus() throws Exception {
        dataMigration.checkFileEncryptionStatus();
    }

}