/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.MomentChangeEvent;
import com.philips.platform.core.monitors.EventMonitor;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.Collections;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentsMonitor extends EventMonitor {

    @NonNull
    private final MomentsDataFetcher momentsDataFetcher;

    @NonNull
    private final MomentsDataSender momentsDataSender;


    @Inject
    public MomentsMonitor(@NonNull final MomentsDataSender momentsDataSender, @NonNull final MomentsDataFetcher momentsDataFetcher) {
        this.momentsDataSender = momentsDataSender;
        this.momentsDataFetcher = momentsDataFetcher;
    }

    @SuppressWarnings("CheckResult")
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(MomentChangeEvent momentChangeEvent) {

        if (momentsDataSender.sendDataToBackend(Collections.singletonList(momentChangeEvent.getMoment()))) {
             momentsDataFetcher.fetchDataSince(DateTime.now());
        }
    }
}
