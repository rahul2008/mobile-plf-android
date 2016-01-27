/**
 * * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.ui.catalog.R;

/**
 * <H1>Dev Guide:</H1>
 * Two versions for about screen are avialable. Please use below theme and layout.
 *
 * <H4>Philips shield</H4>
 * <pre>
 *     android:theme="@style/AboutScreenTheme"/
 *     layout: R.layout.uikit_about_screen
 *
 *     <b> Use below code to support About Screen specific action bar</b>
 *          <pre>
 *              ActionBar actionBar = getSupportActionBar();
 *              if (actionBar != null) {
 *                   actionBar.setDisplayShowCustomEnabled(true);
 *                   actionBar.setCustomView(R.layout.uikit_about_screen_actionbar);
 *                 }
 *          </pre>
 * </pre>
 *
 * <H4>Maker's mark</H4>
 * <pre>
 * android:theme="@style/AboutScreenTheme.MM"
 * layout: R.layout.uikit_about_screen_mm
 *
 *  <b> Use below code to support About Screen specific action bar</b>
 *          <pre>
 *              ActionBar actionBar = getSupportActionBar();
 *              if (actionBar != null) {
 *                   actionBar.setDisplayShowCustomEnabled(true);
 *                   actionBar.setCustomView(R.layout.uikit_about_screen_actionbar_mm);
 *                 }
 *          </pre>
 * </pre>
 *
 * The ids are mapped as:
 * <table border="2" width="85%" align="center" cellpadding="5">
 *     <thead>
 *         <tr><th>ResourceID</th> <th>String resource ID</th> <th>Description</th></tr>
 *     </thead>
 *
 *     <tbody>
 *     <tr>
 *         <td rowspan="1">UpButton</td>
 *         <td></td>
 *          <td>Button in custom navigation layout to which back behavior can be associated</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="1">aboutscreen_title</td>
 *         <td></td>
 *          <td>Display "About" or any custom text in custom navigation layout to which back
 *          behavior can be
 *          associated</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="1">CloseButton</td>
 *         <td></td>
 *          <td>Button in custom navigation layout to which close behavior can be associated</td>
 *     </tr>
 *      <tr>
 *         <td rowspan="1">about_title</td>
 *         <td>uikit_about_screen_apptitle</td>
 *          <td>Mobile App: -> App title</td>
 *
 *     </tr>
 *     <tr>
 *         <td rowspan="1">about_version</td>
 *         <td>uikit_about_screen_version</td>
 *         <td>Version 1.0.1 : App version</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="1">about_copyright</td>
 *         <td>uikit_about_screen_copyright</td>
 *         <td>Copyright information</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="1">about_terms</td>
 *         <td>uikit_about_screen_terms</td>
 *         <td>Terms & Conditions</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="1">about_policy</td>
 *         <td>uikit_about_screen_privacy</td>
 *         <td>Privacy and policy</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="1">about_content</td>
 *         <td>uikit_about_screen_content</td>
 *         <td>Description of the content</td>
 *     </tr>
 *     <tr>
 *         <td rowspan="1">about_screen_logo</td>
 *         <td></td>
 *         <td>Maker's mark logo icon drawable</td>
 *     </tr>
 *
 *     </tbody>
 *
 * </table>
 *
 */
public class AboutScreenLauncher extends CatalogActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen_choices);
    }

    public void launchAboutScreen(View v) {
        startActivity(getAboutScreenIntent(v.getId()));
    }

    public Intent getAboutScreenIntent(int id) {
        Class<?> targetClass = AboutScreenPhilips.class;

        switch (id) {
            case R.id.about_screen_philips:
                targetClass = AboutScreenPhilips.class;
                break;
            case R.id.about_screen_mm:
                targetClass = AboutScreenMM.class;
                break;
        }
        return new Intent(this, targetClass);
    }
}
