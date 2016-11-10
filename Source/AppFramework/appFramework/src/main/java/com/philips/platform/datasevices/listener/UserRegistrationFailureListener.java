package com.philips.platform.datasevices.listener;

import retrofit.RetrofitError;

/**
 * Created by 310218660 on 10/20/2016.
 */

public interface UserRegistrationFailureListener {
    public void onFailure(RetrofitError error);
}
