package com.philips.platform.csw.justintime;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.csw.BuildConfig;
import com.philips.platform.csw.CswConstants;
import com.philips.platform.csw.R;
import com.philips.platform.csw.utils.TaggingUtils;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SEND_DATA;

public class JustInTimeConsentPresenter implements JustInTimeConsentContract.Presenter {
    private static final String JIT_CONSENT = "jitConsent";

    private final JustInTimeConsentContract.View view;
    private final ConsentDefinition consentDefinition;
    private final JustInTimeWidgetHandler completionListener;
    private AppTaggingInterface taggingInterface;
    private AppInfraInterface appInfra;

    public JustInTimeConsentPresenter(JustInTimeConsentContract.View view, AppInfraInterface appInfra, ConsentDefinition consentDefinition, JustInTimeWidgetHandler completionListener) {
        this.view = view;
        this.appInfra = appInfra;
        this.view.setPresenter(this);
        this.consentDefinition = consentDefinition;
        this.completionListener = completionListener;
        this.taggingInterface = appInfra.getTagging().createInstanceForComponent(CswConstants.Tagging.COMPONENT_ID, BuildConfig.VERSION_NAME);
    }

    @Override
    public void trackPageName() {
        final Map<String, String> info = new HashMap<>();
        info.put(CswConstants.Tagging.CONSENT_TYPE, TaggingUtils.join(consentDefinition.getTypes()));
        taggingInterface.trackPageWithInfo(JIT_CONSENT, info);
    }

    @Override
    public void onConsentGivenButtonClicked() {
        postConsent(true, new JustInTimePostConsentCallback(new PostConsentSuccessHandler() {
            @Override
            public void onSuccess() {
                trackAction(CswConstants.Tagging.CONSENT_ACCEPTED);
                if (completionListener != null) {
                    completionListener.onConsentGiven();
                }
            }
        }));
    }

    @Override
    public void onConsentRejectedButtonClicked() {
        postConsent(false, new JustInTimePostConsentCallback(new PostConsentSuccessHandler() {
            @Override
            public void onSuccess() {
                trackAction(CswConstants.Tagging.CONSENT_REJECTED);
                if (completionListener != null) {
                    completionListener.onConsentRejected();
                }
            }
        }));
    }

    private void trackAction(final String action) {
        taggingInterface.trackActionWithInfo(SEND_DATA, prepareTrackActionInfo(action, TaggingUtils.join(consentDefinition.getTypes())));
    }

    private void postConsent(boolean status, PostConsentCallback callback) {
        view.showProgressDialog();
        appInfra.getConsentManager().storeConsentState(consentDefinition, status, callback);
    }

    private Map<String, String> prepareTrackActionInfo(String event, String types) {
        Map<String, String> info = new HashMap<>();
        info.put(CswConstants.Tagging.SPECIAL_EVENTS, event);
        info.put(CswConstants.Tagging.CONSENT_TYPE, types);
        return info;
    }

    class JustInTimePostConsentCallback implements PostConsentCallback {

        private final JustInTimeConsentPresenter.PostConsentSuccessHandler handler;

        private JustInTimePostConsentCallback(JustInTimeConsentPresenter.PostConsentSuccessHandler handler) {
            this.handler = handler;
        }

        @Override
        public void onPostConsentFailed(ConsentError error) {
            view.hideProgressDialog();
            switch (error.getErrorCode()) {
                case ConsentError.CONSENT_ERROR_NO_CONNECTION:
                    view.showErrorDialog(R.string.csw_offline_title, R.string.csw_offline_message);
                    break;
                default:
                    view.showErrorDialogForCode(R.string.csw_problem_occurred_error_title, error.getErrorCode());
            }
        }

        @Override
        public void onPostConsentSuccess() {
            view.hideProgressDialog();
            handler.onSuccess();
        }
    }

    interface PostConsentSuccessHandler {
        void onSuccess();
    }
}