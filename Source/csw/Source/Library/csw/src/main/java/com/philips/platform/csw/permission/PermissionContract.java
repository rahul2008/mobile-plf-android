package com.philips.platform.csw.permission;

import com.philips.platform.csw.dialogs.ConfirmDialogTextResources;
import com.philips.platform.csw.dialogs.ConfirmDialogView;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

public interface PermissionContract {
    interface View {
        void setPresenter(Presenter presenter);

        void showProgressDialog();

        void hideProgressDialog();

        void showErrorDialog(boolean goBack, int titleRes, ConsentError error);

        void showErrorDialog(boolean goBack, int titleRes, int messageRes);

        void showConfirmRevokeConsentDialog(ConfirmDialogTextResources dialogTexts, ConfirmDialogView.ConfirmDialogResultHandler handler);

        boolean isActive();
    }

    interface Presenter {
        void fetchConsentStates(List<ConsentDefinition> consentDefinitionList);

        void onToggledConsent(int position, ConsentDefinition definition,
                              boolean consentGiven, ConsentToggleResponse responseHandler);

        interface ConsentToggleResponse {
            void handleResponse(boolean result);
        }

        void trackPageName();
    }
}
