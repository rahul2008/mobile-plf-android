package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import org.hamcrest.Matchers;

import java.util.EnumSet;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.IsPixelAsExpectedMatcher.isImageSimilar;
import static com.philips.cdp.ui.catalog.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static org.hamcrest.Matchers.allOf;

/*
*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.

*/



public class InputFieldTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public InputFieldTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testInputFieldUnfocusedWithoutEntry() {
        onView(withText("Image Navigation")).perform(swipeUp());
        onView(withText("Input Text Fields")).perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
//        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
//                .check(matches(isOutlineColorSimilar("#b9b9b9")));

//        onView(withId(R.id.input_field_2))
//                .check(matches(isOutlineColorSimilar("#b9b9b9")));
//        onView(withId(R.id.input_field_2))
//                .check(matches(isBackgroundColorSimilar("#ffffff")));
//        onView(withId(R.id.input_field_2))
//                .check(matches(isTextColorSimilar("#b9b9b9")));
/*
        onView(withId(R.id.input_field_2))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
*/


    }

    public void testDBThemeInputFieldFocusedWithEntry() {
        // Apply Dark blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Verify the UI parameters
        onView(withId(R.id.input_field_1))
                .check(matches(isOutlineColorSimilar("#03478")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextColorSimilar("#0f204b")));
        onView(withId(R.id.input_field_1))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testBOThemeInputFieldFocusedWithEntry() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Verify the UI parameters
        onView(withId(R.id.input_field_1))
                .check(matches(isOutlineColorSimilar("#e9830")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextColorSimilar("#0f204b")));
        onView(withId(R.id.input_field_1))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }

    public void testInputFieldUnFocusedWithEntry(){
        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("test@test.com"));

        //Make the input field unfocused
        onView(withId(R.id.input_field_2)).perform(click());

        //Verify UI parameters for input field 1
        onView(withId(R.id.input_field_1))
                .check(matches(isOutlineColorSimilar("#b9b9b9")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextColorSimilar("#0f204b")));
        onView(withId(R.id.input_field_1))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testDBThemeInputFieldWithIncorrectEntry(){
        // Apply Dark blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(withId(R.id.input_field_2)).perform(click());

        //Verify UI parameters for input field 1 with incorrect entry
        onView(withId(R.id.input_field_1))
                .check(matches(isOutlineColorSimilar("#e9830")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextColorSimilar("#e9830")));
        onView(withId(R.id.input_field_1))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));

    }

    public void testBOThemeInputFieldWithIncorrectEntry(){
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(withId(R.id.input_field_2)).perform(click());

        //Verify UI parameters for input field 1 with incorrect entry
        onView(withId(R.id.input_field_1))
                .check(matches(isOutlineColorSimilar("#cd202c")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextColorSimilar("#cd202c")));
        onView(withId(R.id.input_field_1))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }

    public void testDisableInputField(){
    //Verifying disabled field color codes
        onView(withId(R.id.input_field_disabled))
                .check(matches(isOutlineColorSimilar("#b9b9b9")));
        onView(withId(R.id.input_field_1))
                .check(matches(isTextColorSimilar("#b9b9b9")));
        onView(withId(R.id.input_field_1))
                .check(matches(isBackgroundColorSimilar("#888888"))); // Reinoud has to get back on this

    }

    public void testWarningIconDBTheme(){
        // Apply Dark Blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.incorrect_icon_dbtheme);

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(withId(R.id.input_field_2)).perform(click());

        //Verifying whether incorrect icon is pixel perfect
        onView(withId(R.id.error_image))
                .check(matches(isImageSimilar(expectedBitmap)));

    }

    public void testWarningIconBOTheme(){
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(withId(R.id.input_field_2)).perform(click());

        //Verifying background color of incorrect icon
        onView(withId(R.id.error_image))
                .check(matches(isOutlineColorSimilar("#cd202c")));

    }

    public void testWarningMessageDBTheme(){
        // Apply Dark Blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(withId(R.id.input_field_2)).perform(click());

        //Verify warning text
        onView(withId(R.id.error_image))
                .check(matches(isTextColorSimilar("#e9830")));
        onView(withId(R.id.error_image))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));


    }

    public void testWarningMessageBOTheme(){
        // Apply Bright orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(withId(R.id.input_field_2)).perform(click());

        //Verify warning text
        onView(withId(R.id.error_image))
                .check(matches(isTextColorSimilar("#cd202c")));
        //fontsize to be updated
    }

    public void testHeightInputFields() {

        onView(withId(R.id.input_field_1))
                .check(matches(isHeightSimilar(44)));
        onView(withId(R.id.input_field_2))
                .check(matches(isHeightSimilar(44)));
        onView(withId(R.id.input_field_disabled))
                .check(matches(isHeightSimilar(44)));

    }

    public void testIncorrectMessagePushesContentDown(){
        onView(withText("Input Text Fields")).perform(click());
        onView(withId(R.id.input_field_1)).perform(click());
        onView(withId(R.id.input_field_1)).perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(withId(R.id.input_field_2)).perform(click());
        onView(withId(R.id.error_image)).check(isAbove(withId(R.id.input_field_2)));
    }

    }

// Fonttype to be verified manually
//correct disabled field tests










