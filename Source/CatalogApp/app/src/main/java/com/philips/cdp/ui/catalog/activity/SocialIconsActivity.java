package com.philips.cdp.ui.catalog.activity;



import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.costumviews.SocialMediaDrawables;

/**
 * Created by 310213373 on 11/3/2015.
 */
public class SocialIconsActivity extends CatalogActivity {
    ImageView mImageView1, mImageView2, mImageView3, mImageView4, mImageView5, mImageView6, mImageView7, mImageView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_icons_activity);
        mImageView1 = (ImageView) findViewById(R.id.image1);
        mImageView2 = (ImageView) findViewById(R.id.image2);
        mImageView3 = (ImageView) findViewById(R.id.image3);
        mImageView4 = (ImageView) findViewById(R.id.image4);
        mImageView5 = (ImageView) findViewById(R.id.image5);
        mImageView6 = (ImageView) findViewById(R.id.image6);
        mImageView7 = (ImageView) findViewById(R.id.image7);
        mImageView8 = (ImageView) findViewById(R.id.image8);


        SocialMediaDrawables smd = new SocialMediaDrawables();
        Drawable d1 = smd.getSocialDrawable(this, R.drawable.uikit_social_wechat, true);
        mImageView1.setBackgroundDrawable(d1);

        Drawable d2 = smd.getSocialDrawable(this, R.drawable.uikit_social_vkontakte, true);
        mImageView2.setBackgroundDrawable(d2);

        Drawable d3 = smd.getSocialDrawable(this, R.drawable.uikit_social_pininterest, true);
        mImageView3.setBackgroundDrawable(d3);

        Drawable d4 = smd.getSocialDrawable(this, R.drawable.uikit_social_facebook, true);
        mImageView4.setBackgroundDrawable(d4);

        Drawable d5 = smd.getSocialDrawable(this, R.drawable.uikit_social_linkedin, true);
        mImageView5.setBackgroundDrawable(d5);

        Drawable d6 = smd.getSocialDrawable(this, R.drawable.uikit_social_twitter, true);
        mImageView6.setBackgroundDrawable(d6);

        Drawable d7 = smd.getSocialDrawable(this, R.drawable.uikit_social_youtube, true);
        mImageView7.setBackgroundDrawable(d7);

        Drawable d8 = smd.getSocialDrawable(this, R.drawable.uikit_social_instagram, true);
        mImageView8.setBackgroundDrawable(d8);
    }
}
