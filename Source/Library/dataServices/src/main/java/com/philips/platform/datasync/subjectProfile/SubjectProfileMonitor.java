package com.philips.platform.datasync.subjectProfile;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.CreateSubjectProfileRequestEvent;
import com.philips.platform.core.events.DeleteSubjectProfileRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileListRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileListResponseEvent;
import com.philips.platform.core.events.GetSubjectProfileRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileResponseEvent;
import com.philips.platform.core.events.SubjectProfileErrorResponseEvent;
import com.philips.platform.core.events.SubjectProfileResponseEvent;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SubjectProfileMonitor extends EventMonitor {

    private SubjectProfileController mSubjectProfileController;
    private SubjectProfileListener mSubjectProfileListener;

    @Inject
    public SubjectProfileMonitor(@NonNull SubjectProfileController subjectProfileController) {
        mSubjectProfileController = subjectProfileController;
        DataServicesManager.getInstance().getAppComponent().injectSubjectProfileMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(CreateSubjectProfileRequestEvent createSubjectProfileRequestEvent) {
        mSubjectProfileListener = createSubjectProfileRequestEvent.getSubjectProfileListener();
        mSubjectProfileController.createSubjectProfile(createSubjectProfileRequestEvent.getUCoreCreateSubjectProfileRequest());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(GetSubjectProfileListRequestEvent getSubjectProfileListRequestEvent) {
        mSubjectProfileListener = getSubjectProfileListRequestEvent.getSubjectProfileListener();
        mSubjectProfileController.getSubjectProfileList();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(GetSubjectProfileRequestEvent getSubjectProfileRequestEvent) {
        mSubjectProfileListener = getSubjectProfileRequestEvent.getSubjectProfileListener();
        mSubjectProfileController.getSubjectProfile(getSubjectProfileRequestEvent.getSubjectID());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(DeleteSubjectProfileRequestEvent deleteSubjectProfileRequestEvent) {
        mSubjectProfileListener = deleteSubjectProfileRequestEvent.getSubjectProfileListener();
        mSubjectProfileController.getSubjectProfile(deleteSubjectProfileRequestEvent.getSubjectID());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final SubjectProfileResponseEvent subjectProfileResponseEvent) {
        mSubjectProfileListener.onResponse(subjectProfileResponseEvent.isSuccess());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final SubjectProfileErrorResponseEvent subjectProfileErrorResponseEvent) {
        mSubjectProfileListener.onError(subjectProfileErrorResponseEvent.getDataServicesError());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final GetSubjectProfileListResponseEvent getSubjectProfileListResponseEvent) {
        mSubjectProfileListener.onGetSubjectProfiles(getSubjectProfileListResponseEvent.getUCoreSubjectProfileList().getSubjectProfiles());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final GetSubjectProfileResponseEvent getSubjectProfileResponseEvent) {
        List<UCoreSubjectProfile> uCoreSubjectProfileList = new ArrayList<>();
        uCoreSubjectProfileList.add(getSubjectProfileResponseEvent.getUCoreSubjectProfile());
        mSubjectProfileListener.onGetSubjectProfiles(uCoreSubjectProfileList);
    }
}
