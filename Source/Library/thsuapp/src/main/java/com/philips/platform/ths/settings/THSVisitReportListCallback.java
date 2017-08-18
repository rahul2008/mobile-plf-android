/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import com.americanwell.sdk.entity.visit.VisitReport;

public interface THSVisitReportListCallback<List, SDKError> {
    void onResponse(java.util.List<VisitReport> visitReports, com.americanwell.sdk.entity.SDKError sdkError);
    void onFailure(Throwable throwable);
}
