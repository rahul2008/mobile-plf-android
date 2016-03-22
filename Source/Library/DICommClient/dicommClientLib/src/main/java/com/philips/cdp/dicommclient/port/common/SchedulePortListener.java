/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import java.util.List;

public interface SchedulePortListener {

    void onSchedulesReceived(List<ScheduleListPortInfo> scheduleList);

    void onScheduleReceived(ScheduleListPortInfo schedule);

    void onError(int errorType);
}
