package com.philips.platform.mya.csw.justintime;public class JustInTimeConsentPresenter implements com.philips.platform.mya.csw.justintime.JustInTimeConsentContract.Presenter{private final com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment justInTimeConsentFragment;	public JustInTimeConsentPresenter(com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment justInTimeConsentFragment)	{		this.justInTimeConsentFragment = justInTimeConsentFragment;	}@java.lang.Override
    public void onConsentGivenButtonClicked() {
        justInTimeConsentFragment.postConsent(true, new com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment.JustInTimePostConsentCallback(new com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment.PostConsentSuccessHandler() {
            @java.lang.Override
            public void onSuccess() {
                if (com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.completionListener != null) {
                    com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.completionListener.onConsentGiven();
                }
            }
        }));
    }@java.lang.Override
    public void onConsentRejectedButtonClicked() {
        justInTimeConsentFragment.postConsent(false, new com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment.JustInTimePostConsentCallback(new com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment.PostConsentSuccessHandler() {
            @java.lang.Override
            public void onSuccess() {
                if (com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.completionListener != null) {
                    com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.completionListener.onConsentRejected();
                }
            }
        }));
    }@java.lang.Override
    public void postConsent(boolean status, com.philips.platform.pif.chi.PostConsentCallback callback) {
        boolean isOnline = com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.appInfra.getRestClient().isInternetReachable();
        if (isOnline) {
            justInTimeConsentFragment.showProgressDialog();
            com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.consentHandlerInterface.storeConsentState(com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.consentDefinition, status, callback);
        } else {
            justInTimeConsentFragment.showErrorDialog(justInTimeConsentFragment.getString(com.philips.platform.mya.csw.R.string.csw_offline_title), justInTimeConsentFragment.getString(com.philips.platform.mya.csw.R.string.csw_offline_message));
        }
    }interface PostConsentSuccessHandler {
        void onSuccess();
    }}