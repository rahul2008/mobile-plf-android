/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.mya.injection;

import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {MyaUiModule.class})
@Singleton
public interface MyaUiComponent {

    FragmentLauncher getFragmentLauncher();

    MyaListener getMyaListener();
}
