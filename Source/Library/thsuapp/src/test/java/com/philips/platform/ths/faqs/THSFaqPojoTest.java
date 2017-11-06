/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class THSFaqPojoTest {

    THSFaqPojo mThsFaqPojo;

    @Mock
    FaqBeanPojo faqBeanPojoMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsFaqPojo = new THSFaqPojo();
    }

    @Test
    public void getSection() throws Exception {
        mThsFaqPojo.setSection("category");
        final String section = mThsFaqPojo.getSection();
        assertNotNull(section);
        assertThat(section).isInstanceOf(String.class);
        assert section.equalsIgnoreCase("category");
    }

    @Test
    public void getFaq() throws Exception {
        List list = new ArrayList();
        list.add(faqBeanPojoMock);
        mThsFaqPojo.setFaq(list);
        final List<FaqBeanPojo> faq = mThsFaqPojo.getFaq();
        assertNotNull(faq);
        assertThat(faq).isInstanceOf(List.class);
    }

}