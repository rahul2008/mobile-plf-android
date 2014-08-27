package com.philips.cl.di.dev.pa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.TutorialPagerActivity;
import com.philips.cl.di.dev.pa.fragment.AirTutorialViewFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	private static final int[] CONTENT = new int[] {
			R.string.tutorial_dashboard_desc, R.string.tutorial_lOC_desc,
			R.string.tutorial_rOC_desc, R.string.tutorial_device_status_desc,
			R.string.tutorial_indoor_aqi_desc,
			R.string.tutorial_outdoor_aqi_desc };
	private static final int[] IMAGES = new int[] { R.drawable.tutorial_step1,
			R.drawable.tutorial_step2, R.drawable.tutorial_step3,
			R.drawable.tutorial_step4, R.drawable.tutorial_step5,
			R.drawable.tutorial_step6 };
	private static final int[] INSTRUCTION = new int[] {
			R.string.dashboard_instruction,
			R.string.loc_roc_remote_instruction,
			R.string.loc_roc_remote_instruction, R.string.status_instruction,
			R.string.indoor_outdoor_aqi_instruction,
			R.string.indoor_outdoor_aqi_instruction };

	private static final int[] INSTRUCTION_LIST = new int[] {
			R.string.dashboard_instruction_1, R.string.dashboard_instruction_2,
			R.string.dashboard_instruction_3, R.string.dashboard_instruction_4 };

	private int mCount = CONTENT.length;
	private TutorialPagerActivity activity;

	public ViewPagerAdapter(FragmentManager fm,
			TutorialPagerActivity tutorialPagerActivity) {
		super(fm);
		activity = tutorialPagerActivity;
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0)
			return AirTutorialViewFragment.newInstance(CONTENT[position],
					IMAGES[position], INSTRUCTION[position], INSTRUCTION_LIST);
		else
			return AirTutorialViewFragment.newInstance(CONTENT[position],
					IMAGES[position], INSTRUCTION[position], null);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String desc = activity.getString(ViewPagerAdapter.CONTENT[position]);
		return desc;
	}

	@Override
	public int getIconResId(int index) {
		return IMAGES[index];
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}
