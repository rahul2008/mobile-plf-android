/*
package com.philips.cdp.digitalcare.rateandreview.test;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;

import com.philips.cdp.digitalcare.activity.DigitalCareActivity;
import com.philips.cdp.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cdp.digitalcare.rateandreview.fragments.ProductWriteReviewFragment;

*/
/**
 * This is the testing Class to test the Product Review features testing.
 *
 * @author ritesh.jha@philips.com
 * @since 27 Aug 2015
 *//*

public class ProductReviewTest extends
        ActivityInstrumentationTestCase2<DigitalCareActivity> {

    private RateThisAppFragment mRateThisAppScreen = null;
    private Activity mLauncherActivity = null;
    private Context mContext, context = null;
 //   private ProductWriteReviewFragment mProductWriteReviewFragment = null;

    public ProductReviewTest() {
        super(DigitalCareActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mLauncherActivity = getActivity();
        mContext = getInstrumentation().getTargetContext();
        context = getInstrumentation().getContext();
        mProductWriteReviewFragment = new ProductWriteReviewFragment();
    }


     */
/*
    @SmallTest
    public void testTheLauncherContext() {
        assertNotNull(mLauncherActivity);
    }

    @SmallTest
    public void testProductCtnForReview() {
        String ctn = mProductWriteReviewFragment.getCtn();
        assertNotNull(ctn);
    }

    @SmallTest
    public void testProductRating() {
        boolean valid = false;
        RatingBar ratingBar = new RatingBar(context);
        ratingBar.setRating(1);
        float ratingValue = mProductWriteReviewFragment.getRatingValue(ratingBar);

        if (ratingValue > 0 && ratingValue <= 6) {
            valid = true;
        }
        assertTrue(valid);
    }

    @SmallTest
    public void testReviewSummary() {
        EditText reviewSummaryEditText = new EditText(context);
        reviewSummaryEditText.setText("Review Summary");
        String summary = mProductWriteReviewFragment.getReviewSummaryValue(reviewSummaryEditText);
        assertNotNull(summary);
    }

    @SmallTest
    public void testReviewDescription() {
        EditText reviewDescEditText = new EditText(context);
        reviewDescEditText.setText("Review Description");
        String desc = mProductWriteReviewFragment.getReviewDescriptionValue(reviewDescEditText);
        assertNotNull(desc);
    }

    @SmallTest
    public void testReviewDescriptionCharCount() {
        boolean valid = false;
        EditText reviewDescCountEditText = new EditText(context);
        reviewDescCountEditText.setText("The character count for the description of product review should be more than 50. This has been instructed in GUI specification also ");
        String desc = mProductWriteReviewFragment.getReviewDescriptionValue(reviewDescCountEditText);
        if (desc.length() >= 50) {
            valid = true;
        }
        assertTrue(valid);
    }

    @SmallTest
    public void testNickName() {
        EditText nickNameEditText = new EditText(context);
        nickNameEditText.setText("nick name");
        String desc = mProductWriteReviewFragment.getNickNameValue(nickNameEditText);
        assertNotNull(desc);
    }

    @SmallTest
    public void testNickNameCharCount() {
        boolean valid = false;
        EditText nickNameEditText = new EditText(context);
        nickNameEditText.setText("nick name should be at least 4 char long, as per bazaar voice validation");
        String desc = mProductWriteReviewFragment.getNickNameValue(nickNameEditText);
        if (desc.length() >= 4) {
            valid = true;
        }
        assertTrue(valid);
    }

  @SmallTest
    public void testEmailId() {
        EditText emailIDEditText = new EditText(context);
        emailIDEditText.setText("emailvalidation@gmail.com");
        String desc = mProductWriteReviewFragment.getEmailValue(emailIDEditText);
        assertNull(desc);
    }

    @SmallTest
    public void testEmailIdValidation() {
        boolean valid = false;
        EditText emailIDEditText = new EditText(context);
        emailIDEditText.setText("emailvalidation@gmail.com");
        String desc = mProductWriteReviewFragment.getEmailValue(emailIDEditText);
        valid = desc.contains("@");
        //TODO : More validations are required.
        assertTrue(valid);
    }

    @SmallTest
    public void testLegalPolicyAcceptance() {
        boolean valid = false;
        CheckBox LegalTermSwitchView = new CheckBox(mContext);
        LegalTermSwitchView.setChecked(true);
        valid = mProductWriteReviewFragment.getLegalTermValue(LegalTermSwitchView);
        assertTrue(valid);
    }

    @SmallTest
    public void testProductTitle() {
        String productTitle = mProductWriteReviewFragment.getProductTitle();
    } *//*

}*/
