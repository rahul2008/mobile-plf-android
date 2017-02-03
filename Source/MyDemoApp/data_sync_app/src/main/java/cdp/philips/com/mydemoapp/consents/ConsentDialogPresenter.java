package cdp.philips.com.mydemoapp.consents;

import android.content.Context;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.List;

/**
 * Created by sangamesh on 16/11/16.
 */

public class ConsentDialogPresenter {

    private final Context mContext;
    private final DBRequestListener dbRequestListener;

    ConsentDialogPresenter(Context mContext, DBRequestListener dbRequestListener) {
        this.mContext = mContext;
        this.dbRequestListener = dbRequestListener;
    }

    protected boolean getConsentDetailStatus(Consent consent) {

        if (consent.getStatus().equalsIgnoreCase(ConsentDetailStatusType.ACCEPTED.name())) {
            return true;
        }
        return false;
    }

    public void updateConsent(List<Consent> consents) {
        DataServicesManager.getInstance().updateConsent(consents,dbRequestListener);
    }
}
