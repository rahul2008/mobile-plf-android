/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.americanwell.sdk.entity.FileAttachment;
import com.philips.platform.ths.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class THSFileUtilsTest {
    THSFileUtils mThsFileUtils;

    @Mock
    Context contextMock;

    @Mock
    FileAttachment fileAttachmentMock;

    @Mock
    ApplicationInfo applicationInfoMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(fileAttachmentMock.getType()).thenReturn("pdf");
        when(contextMock.getApplicationInfo()).thenReturn(applicationInfoMock);
//        when(applicationInfoMock.labelRes).thenReturn(123);
     //   when(contextMock.getString(112)).thenReturn("222");
        mThsFileUtils = new THSFileUtils();
    }

    @Test(expected = NullPointerException.class)
    public void downloadAttachment() throws Exception {
        mThsFileUtils.downloadAttachment(contextMock,fileAttachmentMock);
    }

    @Test
    public void getUploadAttachment() throws Exception {

    }

    @Test
    public void getFileName() throws Exception {

    }

    @Test
    public void openAttachment() throws Exception {

    }

}