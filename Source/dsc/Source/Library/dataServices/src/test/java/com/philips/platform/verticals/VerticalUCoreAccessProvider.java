package com.philips.platform.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

/**
 * Created by 310218660 on 1/3/2017.
 */

public class VerticalUCoreAccessProvider extends UCoreAccessProvider {
    public VerticalUCoreAccessProvider(@NonNull UserRegistrationInterface userRegistrationInterface) {
        super(userRegistrationInterface);
    }
}
