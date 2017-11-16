/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

/**
 * Created by philips on 25/07/17.
 */

public interface WebViewContract {
    interface View {
        void updateUiOnUrlLoaded(String url);

        void onUrlLoadError(String errorMessage);

        void showToast(String message);

        void onArticleLoaded(String articleWebPageUrl);
    }

    interface Action {
        void loadUrl(WebViewEnum state);

        void loadArticle(String articleServiceId,String articleTitle);
    }

}
