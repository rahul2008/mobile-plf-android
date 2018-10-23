/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.faqs.THSFaqPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class THSRestClientTest {

    THSRestClient mThsRestClient;

    @Mock
    THSBasePresenter thsBasePresenter;

    @Mock
    THSFaqPresenter faqPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsRestClient = new THSRestClient(thsBasePresenter);
    }

    @Test
    public void doInBackground() throws Exception {
        final String s = mThsRestClient.doInBackground("http://");
        assert s == null;
    }

    @Test
    public void onPostExecute() throws Exception {
        mThsRestClient.onPostExecute("http://");
        verifyNoMoreInteractions(thsBasePresenter);
    }


    @Test
    public void onPostExecuteFaq() throws Exception {
        mThsRestClient = new THSRestClient(faqPresenter);
        mThsRestClient.onPostExecute("http://");
        verify(faqPresenter).parseJson(anyString());
    }

}