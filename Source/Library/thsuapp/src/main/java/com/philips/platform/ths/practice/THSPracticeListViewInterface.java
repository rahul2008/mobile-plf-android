package com.philips.platform.ths.practice;

import com.philips.platform.ths.base.THSBaseView;


public interface THSPracticeListViewInterface extends THSBaseView {
    void showError(String errorMessage);
    void showError(String errorMessage, boolean shouldGoBack);
}
