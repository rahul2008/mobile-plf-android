package com.philips.platform.ths.appointment;

import com.americanwell.sdk.entity.consumer.RemindOptions;

interface THSDialogFragmentCallback<T> {
    void onPostData(T t);
    RemindOptions getReminderOptions();
}
