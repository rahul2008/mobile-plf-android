package com.philips.cdp.registration.update;

import io.reactivex.Completable;

public interface UpdateUserProfile {

    Completable updateUserEmail(String emailId);
}
