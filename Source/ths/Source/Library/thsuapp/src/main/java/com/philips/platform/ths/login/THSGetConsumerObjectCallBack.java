/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.login;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;

//TODO: Review Comment - Spoorti - Use generics for type safety. Eg: THSGetConsumerObjectCallBack<T, E extends THSSDKError>
//TODO: Review Comment - Spoorti - Wrap the SDk objects to PTH Objects
public interface THSGetConsumerObjectCallBack {
    void onReceiveConsumerObject(Consumer consumer, SDKError sdkError);

    void onError(Throwable throwable);
}
