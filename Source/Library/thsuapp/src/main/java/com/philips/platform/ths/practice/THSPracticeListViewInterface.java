package com.philips.platform.ths.practice;

import com.philips.platform.ths.base.THSBaseView;


public interface THSPracticeListViewInterface extends THSBaseView {
    void showError(String module, String errorMessage);
    void showError(String module,String errorMessage, boolean shouldGoBack);
}
