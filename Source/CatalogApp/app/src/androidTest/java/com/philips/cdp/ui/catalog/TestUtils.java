package com.philips.cdp.ui.catalog;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by 310185184 on 9/3/2015.
 */
public class TestUtils {

    private static final String TAG = TestUtils.class.getSimpleName();
    private static final int RELAXED_BOUNDARY_PIXELS = 2;
    private static final int PASS_PERCENTAGE = 5;


    public static boolean sameAs(Bitmap actual, Bitmap expected) {
        int actualWidth = actual.getWidth();
        int expectedWidth = expected.getWidth();
        int actualHeight = actual.getHeight();
        int expectedHeight = expected.getHeight();
        if ((actualWidth != expectedWidth) || (actualHeight != expectedHeight)) {
            Log.d(TAG,"size mismatch");
            return false;
        }
        long diff = 0;
        //Relaxing the comparision by ignoring 2px from each boundary.
        int rowCount = actualHeight-RELAXED_BOUNDARY_PIXELS;
        int columnCount = actualWidth-RELAXED_BOUNDARY_PIXELS;
        //Start comparing by relaxing 2px from starting points
        for (int row = RELAXED_BOUNDARY_PIXELS; row < rowCount; row++) {
            for (int column = RELAXED_BOUNDARY_PIXELS; column < columnCount; column++) {
                if(actual.getPixel(row,column) != expected.getPixel(row,column)) {
                    diff++;
                }
            }
        }
        float mismatchPercentage = (diff/((float)actual.getHeight()*actual.getWidth()))*100;
        Log.d(TAG, "mismatchPercentage:" + mismatchPercentage + "% count=" + diff);

        return mismatchPercentage<=PASS_PERCENTAGE?true:false;
    }

}
