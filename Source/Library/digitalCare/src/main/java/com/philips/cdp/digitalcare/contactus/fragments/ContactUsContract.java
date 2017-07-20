package com.philips.cdp.digitalcare.contactus.fragments;

public interface ContactUsContract {

    void showCallPhilipsBtn();

    void enableBottomText();

    void fadeoutButtons();

    void setTextCallPhilipsBtn(String phoneNumber);

    void updateFirstRowSharePreference(StringBuilder stringBuilder,String phoneNumber);

    void startProgressDialog();

    void closeProgressDialog();

    boolean isViewAdded();

}
