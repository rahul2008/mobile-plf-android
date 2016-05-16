
package com.philips.cdp.registration.coppa.base;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;
import com.philips.cdp.registration.ui.utils.RLog;

public class CoppaExtension {


    private final String NULL = "null";

    private Context mContext;

    public CoppaExtension(Context context) {
        mContext = context;
    }

    public CoppaStatus getCoppaEmailConsentStatus() {
        return getCoppaStatusForConsent(CoppaConfiguration.getConsent());
    }


    private CoppaStatus getCoppaStatusForConsent(com.philips.cdp.registration.coppa.base.Consent consent) {
        if (consent == null) {
            return null;
        }
        CoppaStatus coppaStatus = null;
        if (null != consent.getGiven()) {
            if (!consent.getGiven().equalsIgnoreCase(null)
                    && Boolean.parseBoolean(consent.getGiven())) {
                coppaStatus = CoppaStatus.kDICOPPAConsentGiven;
                if (null != consent.getConfirmationGiven() && !consent.getConfirmationGiven().equalsIgnoreCase(NULL)) {
                    if ((Boolean.parseBoolean(consent.getConfirmationGiven())) ) {
                        coppaStatus = CoppaStatus.kDICOPPAConfirmationGiven;
                    } else {
                        coppaStatus = CoppaStatus.kDICOPPAConfirmationNotGiven;
                    }
                } else if (consent.getGiven() != null && consent.getConfirmationGiven() == null) {
                    RLog.d("Consent","Consent ***" + consent.getConfirmationCommunicationSentAt() + " " + consent.getConfirmationCommunicationSentAt());
                    coppaStatus = CoppaStatus.kDICOPPAConfirmationPending;
                    if(!consent.getLocale().equals("en_US")){
                        coppaStatus = CoppaStatus.kDICOPPAConfirmationGiven;
                    }
                    RLog.d("Consent","Consent coppaconfirmationPending");
                }
            } else {
                coppaStatus = CoppaStatus.kDICOPPAConsentNotGiven;
            }
        } else {
            coppaStatus = CoppaStatus.kDICOPPAConsentPending;
        }
        return coppaStatus;
    }


    public void updateCoppaConsentStatus(final boolean coppaConsentStatus, final CoppaConsentUpdateCallback coppaConsentUpdateCallback) {
        new CoppaConsentUpdater(mContext).updateCoppaConsentStatus(coppaConsentStatus, coppaConsentUpdateCallback);
    }

    public void updateCoppaConsentConfirmationStatus(final boolean coppaConsentStatus, final CoppaConsentUpdateCallback coppaConsentUpdateCallback) {
        new CoppaConsentUpdater(mContext).updateCoppaConsentConfirmationStatus(coppaConsentStatus, coppaConsentUpdateCallback);
    }

    public Consent getConsent() {
        rebuildUserData();
        CoppaConfiguration.getCoopaConfigurationFlields(Jump.getSignedInUser());
        return CoppaConfiguration.getConsent();
    }

    public void buildConfiguration() {
        rebuildUserData();
        CoppaConfiguration.getCoopaConfigurationFlields(Jump.getSignedInUser());
    }

    public void resetConfiguration(){
        CoppaConfiguration.clearConfiguration();

    }

    private void rebuildUserData() {
        CaptureRecord signInUser = Jump.getSignedInUser();
        if (signInUser == null)
            Jump.loadUserFromDiskInternal(mContext);

    }

}
