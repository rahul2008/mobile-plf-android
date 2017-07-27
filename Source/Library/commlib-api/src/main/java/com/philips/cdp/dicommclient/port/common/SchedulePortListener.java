/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import java.util.List;

public interface SchedulePortListener {

    public void onSchedulesReceived(List<ScheduleListPortInfo> scheduleList);

    public void onScheduleReceived(ScheduleListPortInfo schedule);

    public void onError(int errorType);
}
