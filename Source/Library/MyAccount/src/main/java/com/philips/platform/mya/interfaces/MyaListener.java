/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.interfaces;


import com.philips.platform.uappframework.launcher.FragmentLauncher;

public interface MyaListener {

    boolean onClickMyaItem(String itemName, FragmentLauncher fragmentLauncher);

    boolean onLogOut();
}
