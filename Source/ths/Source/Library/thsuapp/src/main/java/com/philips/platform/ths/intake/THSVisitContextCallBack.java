/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

public interface THSVisitContextCallBack<VisitContext, THSSDKError> {

    void onResponse(VisitContext pthVisitContext, THSSDKError pthsdkError);

    void onFailure(Throwable throwable);

}
