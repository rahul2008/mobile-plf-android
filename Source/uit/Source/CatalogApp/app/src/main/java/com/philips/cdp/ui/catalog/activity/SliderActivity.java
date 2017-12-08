package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.BaseSlider;
import com.philips.cdp.uikit.customviews.DiscreteSlider;
import com.philips.cdp.uikit.customviews.IndicatorSlider;
import com.philips.cdp.uikit.customviews.RangeSeekBar;
import com.philips.cdp.uikit.customviews.RangeSlider;

import java.text.NumberFormat;
import java.util.Locale;

public class SliderActivity extends CatalogActivity {


    BaseSlider baseSlider;
    IndicatorSlider indicatorSlider;
    DiscreteSlider discreteSlider;
    RangeSlider rangeSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        baseSlider = (BaseSlider) findViewById(R.id.baseslider);
        indicatorSlider = (IndicatorSlider) findViewById(R.id.indicatorslider);
        discreteSlider = (DiscreteSlider) findViewById(R.id.discreteslider);
        rangeSlider = (RangeSlider) findViewById(R.id.rangeslider);

        /**
         * A callback that notifies clients when the progress level has been
         * changed. This includes changes that were initiated by the user through a
         * touch gesture or arrow key/trackball as well as changes that were initiated
         * programmatically.
         */
        baseSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Notification that the progress level has changed. Clients can use the fromUser parameter
             * to distinguish user-initiated changes from those that occurred programmatically.
             *
             * @param seekBar The SeekBar whose progress has changed
             * @param progress The current progress level. This will be in the range 0..max where max
             *        was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
             * @param fromUser True if the progress change was initiated by the user.
             */
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {

            }

            /**
             * Notification that the user has started a touch gesture. Clients may want to use this
             * to disable advancing the seekbar.
             * @param seekBar The SeekBar in which the touch gesture began
             */

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {

            }
            /**
             * Notification that the user has finished a touch gesture. Clients may want to use this
             * to re-enable advancing the seekbar.
             * @param seekBar The SeekBar in which the touch gesture began
             */

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
        });

        /**
         * Notification that the range value has changed.
         */


        rangeSlider.setOnRangeSliderChangeListener(new RangeSlider.OnRangeSliderChangeListener() {
            @Override
            public void onRangeSliderValuesChanged(final RangeSeekBar bar, final Object minValue, final Object maxValue) {

                Locale netherland = new Locale("nl", "NL");
                NumberFormat netherlandFormat = NumberFormat.getCurrencyInstance(netherland);

                String minStr = netherlandFormat.format(minValue);
                String maxStr = netherlandFormat.format(maxValue);

                rangeSlider.setText(minStr, maxStr);

            }
        });

        /**
         * A callback that notifies clients when the progress level has been
         * changed. This includes changes that were initiated by the user through a
         * touch gesture or arrow key/trackball as well as changes that were initiated
         * programmatically.
         */

        indicatorSlider.setOnIndicatorSliderChangeListener(new IndicatorSlider.OnIndicatorSliderChangeListener() {
            /**
             * Notification that the progress level has changed. Clients can use the fromUser parameter
             * to distinguish user-initiated changes from those that occurred programmatically.
             *
             * @param seekBar The SeekBar whose progress has changed
             * @param progress The current progress level. This will be in the range 0..max where max
             *        was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
             * @param fromUser True if the progress change was initiated by the user.
             */
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
            }

            /**
             * Notification that the user has started a touch gesture. Clients may want to use this
             * to disable advancing the seekbar.
             * @param seekBar The SeekBar in which the touch gesture began
             */
            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {

            }

            /**
             * Notification that the user has finished a touch gesture. Clients may want to use this
             * to re-enable advancing the seekbar.
             * @param seekBar The SeekBar in which the touch gesture began
             */
            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
        });
        /**
         * A callback that notifies clients when the progress level has been
         * changed. This includes changes that were initiated by the user through a
         * touch gesture or arrow key/trackball as well as changes that were initiated
         * programmatically.
         */
        discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            /**
             * Notification that the progress level has changed. Clients can use the fromUser parameter
             * to distinguish user-initiated changes from those that occurred programmatically.
             *
             * @param seekBar The SeekBar whose progress has changed
             * @param progress The current progress level. This will be in the range 0..max where max
             *        was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
             * @param fromUser True if the progress change was initiated by the user.
             */
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {

            }

            /**
             * Notification that the user has started a touch gesture. Clients may want to use this
             * to re-enable advancing the seekbar.
             * @param seekBar The SeekBar in which the touch gesture began
             */
            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {

            }

            /**
             * Notification that the user has finished a touch gesture. Clients may want to use this
             * to re-enable advancing the seekbar.
             * @param seekBar The SeekBar in which the touch gesture began
             */
            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
