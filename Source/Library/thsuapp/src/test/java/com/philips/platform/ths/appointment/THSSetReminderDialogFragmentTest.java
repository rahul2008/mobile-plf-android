/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.view.View;

import com.americanwell.sdk.entity.consumer.RemindOptions;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSSetReminderDialogFragmentTest {
    THSSetReminderDialogFragment mthsSetReminderDialogFragment;

    @Mock
    THSDialogFragmentCallback<String> thsDialogFragmentCallbackMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mthsSetReminderDialogFragment = new THSSetReminderDialogFragment();
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        when(thsDialogFragmentCallbackMock.getReminderOptions()).thenReturn(RemindOptions.NO_REMINDER);
        SupportFragmentTestUtil.startFragment(mthsSetReminderDialogFragment);
    }

    @Test
    public void setDialogFragmentCallback() throws Exception {
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        assertNotNull(mthsSetReminderDialogFragment.thsDialogFragmentCallback);
    }

    @Test
    public void onClick_cancel_reminder_dialog() throws Exception {
        final View viewById = mthsSetReminderDialogFragment.getView().findViewById(R.id.cancel_reminder_dialog);
        viewById.performClick();
    }

    @Test
    public void onClick_set_reminder_15_mins() throws Exception {
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        mthsSetReminderDialogFragment.radioGroup.check(R.id.ths_rb_15_mins);
        final View viewById = mthsSetReminderDialogFragment.getView().findViewById(R.id.set_reminder_confirmation_button);
        viewById.performClick();
        verify(thsDialogFragmentCallbackMock).onPostData(THSConstants.THS_15_MINS_REMINDER);
    }

    @Test
    public void onClick_set_reminder_ths_rb_no_reminder() throws Exception {
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        mthsSetReminderDialogFragment.radioGroup.check(R.id.ths_rb_no_reminder);
        final View viewById = mthsSetReminderDialogFragment.getView().findViewById(R.id.set_reminder_confirmation_button);
        viewById.performClick();
        verify(thsDialogFragmentCallbackMock).onPostData(THSConstants.THS_NO_REMINDER_STRING);
    }

    @Test
    public void onClick_set_reminder_ths_rb_one_hour() throws Exception {
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        mthsSetReminderDialogFragment.radioGroup.check(R.id.ths_rb_one_hour);
        final View viewById = mthsSetReminderDialogFragment.getView().findViewById(R.id.set_reminder_confirmation_button);
        viewById.performClick();
        verify(thsDialogFragmentCallbackMock).onPostData(THSConstants.THS_ONE_HOUR_REMINDER);
    }

    @Test
    public void onClick_set_reminder_ths_rb_four_hours() throws Exception {
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        mthsSetReminderDialogFragment.radioGroup.check(R.id.ths_rb_four_hours);
        final View viewById = mthsSetReminderDialogFragment.getView().findViewById(R.id.set_reminder_confirmation_button);
        viewById.performClick();
        verify(thsDialogFragmentCallbackMock).onPostData(THSConstants.THS_FOUR_HOURS_REMINDER);
    }

    @Test
    public void onClick_set_reminder_ths_rb_eight_hours() throws Exception {
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        mthsSetReminderDialogFragment.radioGroup.check(R.id.ths_rb_eight_hours);
        final View viewById = mthsSetReminderDialogFragment.getView().findViewById(R.id.set_reminder_confirmation_button);
        viewById.performClick();
        verify(thsDialogFragmentCallbackMock).onPostData(THSConstants.THS_EIGHT_HOURS_REMINDER);
    }

    @Test
    public void onClick_set_reminder_ths_rb_one_day() throws Exception {
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        mthsSetReminderDialogFragment.radioGroup.check(R.id.ths_rb_one_day);
        final View viewById = mthsSetReminderDialogFragment.getView().findViewById(R.id.set_reminder_confirmation_button);
        viewById.performClick();
        verify(thsDialogFragmentCallbackMock).onPostData(THSConstants.THS_ONE_DAY_REMINDER);
    }

    @Test
    public void onClick_set_reminder_ths_rb_one_week() throws Exception {
        mthsSetReminderDialogFragment.setDialogFragmentCallback(thsDialogFragmentCallbackMock);
        mthsSetReminderDialogFragment.radioGroup.check(R.id.ths_rb_one_week);
        final View viewById = mthsSetReminderDialogFragment.getView().findViewById(R.id.set_reminder_confirmation_button);
        viewById.performClick();
        verify(thsDialogFragmentCallbackMock).onPostData(THSConstants.THS_ONE_WEEK_REMINDER);
    }

}