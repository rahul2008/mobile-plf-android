package philips.appframeworklibrary.flowmanager.listeners;

import philips.appframeworklibrary.flowmanager.enums.AppFlowEnum;

public interface AppFlowJsonListener {
    void onParseSuccess();
    void onError(AppFlowEnum appFlowEnum);
}
