/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSSharedPreferenceUtilityTest {

    @Mock
    Context contextMock;

    @Mock
    SharedPreferences sharedPreferencesMock;

    @Mock
    SharedPreferences.Editor editorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSSharedPreferenceUtility d = new THSSharedPreferenceUtility();
        when(contextMock.getSharedPreferences(THSSharedPreferenceUtility.SHARED_PREF_HELPER_FILE, 0)).thenReturn(sharedPreferencesMock);
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
    }

    @Test
    public void setString() throws Exception {
        THSSharedPreferenceUtility.setString(contextMock,"say","hello");
        verify(editorMock).commit();
    }

    @Test
    public void setInt() throws Exception {
        THSSharedPreferenceUtility.setInt(contextMock,"key",12);
        verify(editorMock).commit();
    }

    @Test
    public void setBoolean() throws Exception {
        THSSharedPreferenceUtility.setBoolean(contextMock,"key",true);
        verify(editorMock).commit();
    }

    @Test
    public void getString() throws Exception {
        String st = THSSharedPreferenceUtility.getString(contextMock,"key","value");
    }

    @Test
    public void getInt() throws Exception {
        final int key = THSSharedPreferenceUtility.getInt(contextMock, "key", 12);
    }

    @Test
    public void getBoolean() throws Exception {
        Boolean bool = THSSharedPreferenceUtility.getBoolean(contextMock,"key",true);
    }

}