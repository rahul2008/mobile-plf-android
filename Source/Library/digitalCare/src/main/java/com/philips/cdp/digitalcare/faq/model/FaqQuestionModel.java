package com.philips.cdp.digitalcare.faq.model;

/**
 * Created by naveen@philips.com on 12-Apr-16.
 */
public class FaqQuestionModel {

    private String mQuestion = null;
    private String mAnsmer = null;

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getAnswer() {
        return mAnsmer;
    }

    public void setAnsmer(String mAnsmer) {
        this.mAnsmer = mAnsmer;
    }
}
