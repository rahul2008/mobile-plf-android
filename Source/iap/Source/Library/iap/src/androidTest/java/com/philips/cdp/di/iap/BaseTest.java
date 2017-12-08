package com.philips.cdp.di.iap;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.ArrayList;

/**
 * Created by philips on 5/2/17.
 */

public class BaseTest {
    public ArrayList<String> CTNs;

    public BaseTest() {
        CTNs = new ArrayList<String>();
        CTNs.add("HX8332/11");
    }


    public interface IAPFlows {
        int IAP_PRODUCT_CATALOG_VIEW = 0;
        int IAP_SHOPPING_CART_VIEW = 1;
        int IAP_PURCHASE_HISTORY_VIEW = 2;
        int IAP_PRODUCT_DETAIL_VIEW = 3;
        int IAP_BUY_DIRECT_VIEW = 4;
    }

    protected Intent getLaunchIntent(final int view, ArrayList<String> ctns, int theme) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(DemoTestActivity.IAP_VIEW, view);
        bundleExtra.putStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS, ctns);
        bundleExtra.putInt(DemoTestActivity.IAP_THEME, theme);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    protected void turnOnScreen(){
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Point[] coordinates = new Point[4];
        coordinates[0] = new Point(248, 1520);
        coordinates[1] = new Point(248, 929);
        coordinates[2] = new Point(796, 1520);
        coordinates[3] = new Point(796, 929);
        try {
            if (!uiDevice.isScreenOn()) {
                uiDevice.wakeUp();
                uiDevice.swipe(coordinates, 10);
            }
        } catch (RemoteException e) {
            IAPLog.e(IAPLog.LOG, e.getMessage());
        }
    }

}
