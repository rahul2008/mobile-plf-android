/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import com.philips.platform.ths.sdkerrors.THSSDKError;

public interface THSInitializeCallBack<T, E extends THSSDKError>{
    void onInitializationResponse(T var1, E var2);
    void onInitializationFailure(Throwable var1);
}
