package com.philips.platform.ths.appointment;

interface THSDialogFragmentCallback<T> {
    void onPostData(T t);
    String getReminderOptions();
}
