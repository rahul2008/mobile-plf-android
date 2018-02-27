package com.philips.platform.mya.csw.justintime;

import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.permission.helper.ErrorMessageCreator;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

public class JustInTimeConsentPresenter implements JustInTimeConsentContract.Presenter {
    private final JustInTimeConsentContract.View view;

    public JustInTimeConsentPresenter(JustInTimeConsentContract.View view) {
        this.view = view;
    }

    @Override
    public void onConsentGivenButtonClicked() {
        postConsent(true, new JustInTimePostConsentCallback(new PostConsentSuccessHandler() {
            @Override
            public void onSuccess() {
                if (JustInTimeConsentDependencies.completionListener != null) {
                    JustInTimeConsentDependencies.completionListener.onConsentGiven();
                }
            }
        }));
    }

    @Override
    public void onConsentRejectedButtonClicked() {
        postConsent(false, new JustInTimePostConsentCallback(new PostConsentSuccessHandler() {
            @Override
            public void onSuccess() {
                if (JustInTimeConsentDependencies.completionListener != null) {
                    JustInTimeConsentDependencies.completionListener.onConsentRejected();
                }
            }
        }));
    }

    @Override
    public void postConsent(boolean status, PostConsentCallback callback) {
        boolean isOnline = JustInTimeConsentDependencies.appInfra.getRestClient().isInternetReachable();
        if (isOnline) {
            view.showProgressDialog();
            JustInTimeConsentDependencies.consentHandlerInterface.storeConsentState(JustInTimeConsentDependencies.consentDefinition, status, callback);
        } else {
            view.showErrorDialog(R.string.csw_offline_title, R.string.csw_offline_message);
        }
    }

    class JustInTimePostConsentCallback implements PostConsentCallback {

        private final JustInTimeConsentPresenter.PostConsentSuccessHandler handler;

        public JustInTimePostConsentCallback(JustInTimeConsentPresenter.PostConsentSuccessHandler handler) {
            this.handler = handler;
        }

        @Override
        public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
            view.hideProgressDialog();
            view.showErrorDialogForCode(R.string.csw_problem_occurred_error_title, error.getErrorCode());
        }

        @Override
        public void onPostConsentSuccess(Consent consent) {
            view.hideProgressDialog();
            handler.onSuccess();
        }
    }

    interface PostConsentSuccessHandler {
        void onSuccess();
    }
}