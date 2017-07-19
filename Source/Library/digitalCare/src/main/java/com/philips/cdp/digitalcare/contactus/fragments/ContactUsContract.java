package com.philips.cdp.digitalcare.contactus.fragments;

/**
 * Created by philips on 7/19/17.
 */

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
