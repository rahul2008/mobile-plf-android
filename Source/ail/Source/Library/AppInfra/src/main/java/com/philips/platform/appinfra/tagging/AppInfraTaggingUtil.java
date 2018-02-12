/* Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;


import android.text.TextUtils;

import static com.philips.platform.appinfra.tagging.AppTaggingConstants.SUCCESS_MESSAGE;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.TECHNICAL_ERROR;

public class AppInfraTaggingUtil implements AppTaggingAction {
    private AppTaggingInterface appTagging;

    public AppInfraTaggingUtil(AppTaggingInterface appTagging) {
        this.appTagging = appTagging;
    }

    @Override
    public void trackSuccessAction(String category, String message) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(message))
            appTagging.trackActionWithInfo(AppTaggingConstants.SendData, SUCCESS_MESSAGE, category.concat(":").concat(message));
    }

    @Override
    public void trackErrorAction(String category, String message) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(message))
            appTagging.trackActionWithInfo(AppTaggingConstants.SendData, TECHNICAL_ERROR, "AIL:".concat(category).concat(":").concat(message));
    }
}
