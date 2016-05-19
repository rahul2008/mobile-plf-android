package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.cardviewpager.CardActivity;
import com.philips.cdp.uikit.utils.TabUtils;

/**
 * <p>
 * UIKit uses design library TabLayout {@link TabLayout}.
 * Please refer {@link com.philips.cdp.uikit.utils.TabUtils} for managing tabs.
 * <br>
 * Due to different requirement of tabs on phone and tablet,
 * {@link TabUtils#adjustTabs(TabLayout, Context)} must be called in onResume.
 * <p/>
 * Tab can be created with two variants.
 * <h5>With Icons</h5>
 * <pre> style="@style/PTablayout.Image"</pre></pre></p>
 * <p/>
 * <h5>Text</h5>
 * <pre> style="@style/PTablayout"</pre></pre></p>
 * <p/>
 * <h5>Creating Tabs</h5>
 * <p>
 * Use {@link TabUtils#newTab(int, int, int)}  for creating new tabs
 * </p>
 * <p/>
 * <p>
 * Examples:
 * <pre>
 *             &lt;android.support.design.widget.TabLayout
 *                      android:id="@+id/tab_bar"
 *                      <font color="red">style="@style/PTablayout.Image"</font>/&gt;
 *
 *             &lt;android.support.design.widget.TabLayout
 *                      android:id="@+id/tab_bar_text"
 *                      <font color="red">style="@style/PTablayout"</font>/&gt;
 *     </pre>
 * </p>
 */
public class TabBarDemo extends CatalogActivity {

    /*TabLayout topLayout;
    TabLayout bottomLayout;*/
    Button tabTop, tabBottom;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_demo);

    }


    @Override
    protected void onResume() {
        super.onResume();
        tabTop=(Button)findViewById(R.id.tabbar_top);
        tabBottom=(Button)findViewById(R.id.tabbar_bottom);

        tabBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), TabBarDemoBottom.class));

            }
        });

        tabTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), TabBarDemoTop.class));

            }
        });
    }
}


