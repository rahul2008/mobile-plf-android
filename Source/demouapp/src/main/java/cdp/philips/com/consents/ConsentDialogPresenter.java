package cdp.philips.com.consents;

import android.content.Context;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sangamesh on 16/11/16.
 */

public class ConsentDialogPresenter {

    private final Context mContext;
    private final DBRequestListener<ConsentDetail> dbRequestListener;

    ConsentDialogPresenter(Context mContext, DBRequestListener<ConsentDetail> dbRequestListener) {
        this.mContext = mContext;
        this.dbRequestListener = dbRequestListener;
    }

    protected boolean getConsentDetailStatus(ConsentDetail consentDetail) {

        if (consentDetail.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
            return true;
        }
        return false;
    }

    public void updateConsent(List<ConsentDetail> consentDetails) {
        DataServicesManager.getInstance().updateConsentDetails(consentDetails,dbRequestListener);
    }

    public void saveDefaultConsentDetails(){

        DataServicesManager dataServicesManager=DataServicesManager.getInstance();
        List<ConsentDetail> consentDetails=new ArrayList<>();
        consentDetails.add(dataServicesManager.createConsentDetail(ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED,ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        consentDetails.add(dataServicesManager.createConsentDetail(ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED,ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        consentDetails.add(dataServicesManager.createConsentDetail(ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED,ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        dataServicesManager.saveConsentDetails(consentDetails,dbRequestListener);
    }
}
