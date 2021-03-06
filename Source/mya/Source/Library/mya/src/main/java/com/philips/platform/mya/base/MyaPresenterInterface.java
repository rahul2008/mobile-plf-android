/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.base;

public interface MyaPresenterInterface<ViewT> {

    void onViewActive(ViewT view);

    void onViewInactive();
}
