package com.philips.cdp.registration.ui.social;

interface MergeAccountContract {
    void handleNetworkError(boolean isOnline);

    void handleMergeStatus(boolean isOnline);

    void handleMergeSuccess();

    void handleMergeFailuer(String reason);

    void handleMergePasswordFailuer();
}
