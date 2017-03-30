/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.screens.base;

import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

public interface FragmentViewUapp extends UappUIView {

    ActionBarListener getActionBarListener();

    int getContainerId();

    BaseFlowManager getTargetFlowManager();
}
