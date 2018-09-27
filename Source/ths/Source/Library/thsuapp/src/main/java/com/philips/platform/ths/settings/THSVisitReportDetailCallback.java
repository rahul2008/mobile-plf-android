/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

public interface THSVisitReportDetailCallback<VisitReportDetail,SDKError> {
    void onResponse(com.americanwell.sdk.entity.visit.VisitReportDetail visitReportDetail, com.americanwell.sdk.entity.SDKError sdkError);
    void onFailure(Throwable throwable);
}
