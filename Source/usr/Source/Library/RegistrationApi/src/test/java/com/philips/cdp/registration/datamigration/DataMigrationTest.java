package com.philips.cdp.registration.datamigration;

import android.content.Context;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;

/**
 * Created by philips on 11/30/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataMigrationTest extends TestCase{

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