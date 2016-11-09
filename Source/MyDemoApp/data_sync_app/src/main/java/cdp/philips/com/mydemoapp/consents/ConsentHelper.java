package cdp.philips.com.mydemoapp.consents;

import android.support.annotation.NonNull;


import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.ConsentDetailType;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.Collection;

import javax.inject.Inject;

import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetailType;
import cdp.philips.com.mydemoapp.registration.UserRegistrationFacadeImpl;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentHelper {

    @NonNull
    private final BaseAppDataCreator mDataCreator;
    @NonNull
    private UserRegistrationFacadeImpl userRegistrationFacade;

    private final DataServicesManager dataServicesManager;

    public ConsentHelper(DataServicesManager dataServicesManager) {
        this.mDataCreator = dataServicesManager.getDataCreater();
        this.userRegistrationFacade = (UserRegistrationFacadeImpl) dataServicesManager.getUserRegistrationImpl();
        this.dataServicesManager = dataServicesManager;
    }

    public void addConsent(@NonNull Consent consent, @NonNull final ConsentDetailType consentDetailType, final ConsentDetailStatusType consentDetailStatusType, final String deviceIdentificationNumber) {
        if (consent == null) {
            consent = mDataCreator.createConsent(userRegistrationFacade.getUserProfile().getGUid());
        }
        addConsentDetails(consent, consentDetailType, consentDetailStatusType, deviceIdentificationNumber);
    }

    public void sendUpdateRequest(final Consent consent) {
        dataServicesManager.save(consent);
    }

    public void addConsentDetails(@NonNull final Consent consent, @NonNull final ConsentDetail consentDetail) {
        createAndAddConsentDetail(consent, consentDetail.getType(), consentDetail.getStatus(), consentDetail.getDeviceIdentificationNumber());
    }

    private void addConsentDetails(@NonNull final Consent consent, @NonNull final ConsentDetailType detailType, final ConsentDetailStatusType consentDetailStatusType, final String deviceIdentificationNumber) {
        createAndAddConsentDetail(consent, detailType, consentDetailStatusType, deviceIdentificationNumber);
    }

    private void createAndAddConsentDetail(@NonNull final Consent consent, @NonNull final ConsentDetailType detailType, final ConsentDetailStatusType consentDetailStatusType, final String deviceIdentificationNumber) {
        ConsentDetail consentDetail = mDataCreator.createConsentDetail(detailType, consentDetailStatusType.getDescription(), Consent.DEFAULT_DOCUMENT_VERSION, deviceIdentificationNumber, consent);
        consent.addConsentDetails(consentDetail);
    }

    private void createAndAddConsentDetail(@NonNull final Consent consent, @NonNull final ConsentDetailType detailType, final String consentDetailStatusType, final String deviceIdentificationNumber) {
        ConsentDetail consentDetail = mDataCreator.createConsentDetail(detailType, consentDetailStatusType, Consent.DEFAULT_DOCUMENT_VERSION, deviceIdentificationNumber, consent);
        consent.addConsentDetails(consentDetail);
    }

    public void updateConsent(@NonNull final Consent consent,@NonNull final boolean isSynchronized,@NonNull final ConsentDetailStatusType statusType, @NonNull final ConsentDetailType... consentDetailTypes) {
        if (consent != null) {
            boolean isConsentPresent = false;
            final Collection<? extends ConsentDetail> consentDetails = consent.getConsentDetails();
            for (ConsentDetail consentDetail : consentDetails) {
                for (int i = 0; i < consentDetailTypes.length; i++) {
                    if (consentDetail.getType() == consentDetailTypes[i]) {
                        isConsentPresent = true;
                        consentDetail.setStatus(statusType.getDescription());
                        break;
                    }
                }
            }

            if (!isConsentPresent) {
                for (int i = 0; i < consentDetailTypes.length; i++) {
                    String deviceIdentificationNumber = Consent.DEFAULT_DEVICE_IDENTIFICATION_NUMBER;
                    if (ConsentDetailType.ROOM_TEMPERATURE == consentDetailTypes[i] || ConsentDetailType.RELATIVE_HUMIDITY == consentDetailTypes[i]) {
                        deviceIdentificationNumber = Consent.SMART_BABY_MONITOR;
                    }
                    consent.addConsentDetails(new OrmConsentDetail(new OrmConsentDetailType(consentDetailTypes[i]), statusType.getDescription(), Consent.DEFAULT_DOCUMENT_VERSION, deviceIdentificationNumber, (OrmConsent) consent,isSynchronized));
                }
            }
        }
    }
}
