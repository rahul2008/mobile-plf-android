/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;

public interface THSNoticeOfPrivacyPracticesCallBack {

    void onNoticeOfPrivacyPracticesReceivedSuccess(String string, SDKError sDKError );
    void onNoticeOfPrivacyPracticesReceivedFailure(Throwable throwable );


}
