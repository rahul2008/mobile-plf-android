/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.listeners;

public interface SynchronisationChangeListener {
    void dataPullSuccess();

    void dataPartialPullSuccess(String tillDate);

    void dataPullFail(Exception e);

    void dataPushSuccess();

    void dataPushFail(Exception e);

    void dataSyncComplete();
}
