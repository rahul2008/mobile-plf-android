package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;

public interface PTHSDKValidatedCallback<T, E extends SDKError> extends SDKCallback<T, E> {

    void onValidationFailure(Map<String, ValidationReason> var1);
}
