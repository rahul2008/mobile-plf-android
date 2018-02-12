/* Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;


import android.text.TextUtils;

import static com.philips.platform.appinfra.tagging.AppTaggingConstants.SuccessMessage;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.TechnicalError;

public class AppInfraTaggingUtil implements AppTaggingAction {
    private AppTagging appTagging;

    public AppInfraTaggingUtil(AppTagging appTagging) {
        this.appTagging = appTagging;
    }

    @Override
    public void trackSuccessAction(String category, String message) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(message))
            appTagging.trackActionWithInfo(AppTaggingConstants.SendData, SuccessMessage, category.concat(":").concat(message));
    }

    @Override
    public void trackErrorAction(String category, String message) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(message))
            appTagging.trackActionWithInfo(AppTaggingConstants.SendData, TechnicalError, "AIL:".concat(category).concat(":").concat(message));
    }
}
