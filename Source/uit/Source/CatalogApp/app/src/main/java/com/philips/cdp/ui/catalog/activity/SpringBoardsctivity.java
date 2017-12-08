/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;


/**
 * The layout for fullblocks is uikit_springboard_fullblocks , set this as content in the activity
 * Imageviews and Textviews have Ids numbered from 1 to 8 eg. imageView1 , textView1.
 * setImageDrawable(Drawable) to imageview  and  setText to the view.
 * in imageview attribute app:"theme / inverted /none " can be applied for selector that inverts the color of image on pressed state
 *  theme will have basecolor , inverted would have white color and none would do no change to image
 *  @link ref com.philips.cdp.ui.catalog.activity.SpringBoardFullBlocksActivity
 *
 * The layout for SixGrid  is uikit_springboard_sixblocks , set this as content in the activity
 * Imageviews and Textviews have Ids numbered from 1 to 6 eg. imageView1 , textView1.
 * setImageDrawable(Drawable) to imageview  and  setText to the view.
 * in imageview attribute app:"theme / inverted /none " can be applied for selector that inverts the color of image on pressed state
 *  theme will have basecolor , inverted would have white color and none would do no change to image
 *   @link ref com.philips.cdp.ui.catalog.activity.SpringBoardSixActivity
 *
 *The layout for Lists  is uikit_springboard_list , set this as content in the activity
 * Imageviews and Textviews have Ids numbered from 1 to 6 eg. imageView1 , textView1.
 * setImageDrawable(Drawable) to imageview  and  setText to the view.
 * in imageview attribute app:"theme / inverted /none " can be applied for selector that inverts the color of image on pressed state
 *  theme will have basecolor , inverted would have white color and none would do no change to image
 *  @link ref com.philips.cdp.ui.catalog.activity.SpringBoardListActivity

 */
public class SpringBoardsctivity extends CatalogActivity {
    Intent i1, i2, i3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_boardsctivity);
        Button block = (Button) findViewById(R.id.full_block);
        i1 = new Intent(this, SpringboardFullBlocksActivity.class);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i1);

            }
        });

        Button block2 = (Button) findViewById(R.id.grid6_block);
        i2 = new Intent(this, SpringBoardSixGridActivity.class);
        block2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i2);

            }
        });

        Button block3 = (Button) findViewById(R.id.list);
        i3 = new Intent(this, SpringBoardListActivity.class);
        block3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i3);

            }
        });
    }

}
