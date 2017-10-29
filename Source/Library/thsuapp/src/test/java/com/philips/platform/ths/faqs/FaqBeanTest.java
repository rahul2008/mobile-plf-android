/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class FaqBeanTest {

    FaqBean mFaqBean;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mFaqBean = new FaqBean();
    }

    @Test
    public void getQuestion() throws Exception {
        mFaqBean.setQuestion("How are you");
        final String question = mFaqBean.getQuestion();
        assertNotNull(question);
        assertThat(question).isInstanceOf(String.class);
        assert question.equalsIgnoreCase("How are you");
    }

    @Test
    public void getAnswer() throws Exception {
        mFaqBean.setAnswer("Spoorti");
        final String answer = mFaqBean.getAnswer();
        assertNotNull(answer);
        assertThat(answer).isInstanceOf(String.class);
        assert answer.equalsIgnoreCase("Spoorti");
    }

}