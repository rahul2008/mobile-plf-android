/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.login;

import com.philips.platform.ths.sdkerrors.THSSDKError;

public interface THSLoginCallBack<T, E extends THSSDKError> {
    void onLoginResponse(T var1, E var2);

    void onLoginFailure(Throwable var1);
}
