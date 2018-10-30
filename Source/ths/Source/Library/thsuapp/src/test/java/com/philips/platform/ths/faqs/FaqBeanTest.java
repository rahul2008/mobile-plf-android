/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class FaqBeanTest {

    FaqBeanPojo mFaqBeanPojo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mFaqBeanPojo = new FaqBeanPojo();
    }

    @Test
    public void getQuestion() throws Exception {
        mFaqBeanPojo.setQuestion("How are you");
        final String question = mFaqBeanPojo.getQuestion();
        assertNotNull(question);
        assertThat(question).isInstanceOf(String.class);
        assert question.equalsIgnoreCase("How are you");
    }

    @Test
    public void getAnswer() throws Exception {
        mFaqBeanPojo.setAnswer("Spoorti");
        final String answer = mFaqBeanPojo.getAnswer();
        assertNotNull(answer);
        assertThat(answer).isInstanceOf(String.class);
        assert answer.equalsIgnoreCase("Spoorti");
    }

}