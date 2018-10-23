/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class THSFileProviderTest {
    THSFileUtils thsFileUtils;
    THSFileProvider thsFileProvider;

    @Before
    public void setUp() throws Exception {
        thsFileUtils = new THSFileUtils();
        thsFileProvider = new THSFileProvider();
    }

    @Test
    public void testFileProviderForNull(){
        assert thsFileUtils != null;
    }

}