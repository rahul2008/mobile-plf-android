/**
 * Question & Answer holding Model class used in the FAQScreen first Screen.
 *
 * @author naveen@philips.com
 * @Created 12-Apr-16.
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.faq.model;


public class FaqQuestionModel {

    private String mQuestion = null;
    private String mAnsmer = null;
    private String mLanguageCode = null;

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        mLanguageCode = languageCode;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getAnswer() {
        return mAnsmer;
    }

    public void setAnswer(String mAnsmer) {
        this.mAnsmer = mAnsmer;
    }
}
