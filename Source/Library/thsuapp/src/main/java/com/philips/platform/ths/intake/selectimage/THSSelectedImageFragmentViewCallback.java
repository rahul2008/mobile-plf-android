package com.philips.platform.ths.intake.selectimage;

import com.philips.platform.ths.base.THSBaseView;

interface THSSelectedImageFragmentViewCallback extends THSBaseView{
    void updateProgreeDialog(boolean show);
    void showToast(String toastMessange);
}
