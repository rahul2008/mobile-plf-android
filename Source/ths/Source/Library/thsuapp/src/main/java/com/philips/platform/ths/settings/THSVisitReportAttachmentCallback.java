/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import com.americanwell.sdk.entity.FileAttachment;
import com.americanwell.sdk.entity.SDKError;

public interface THSVisitReportAttachmentCallback<FileAttachment,sdkError> {
    void onResponse(FileAttachment fileAttachment, SDKError sdkError);
    void onFailure(Throwable throwable);
}
