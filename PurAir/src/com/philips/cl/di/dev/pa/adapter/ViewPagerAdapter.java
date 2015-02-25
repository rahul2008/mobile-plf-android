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
			R.string.tutorial_rOC_desc };
	private static final int[] IMAGES = new int[] { R.drawable.tutorial_page1,
			R.drawable.tutorial_page2, R.drawable.tutorial_page3 };
	private static final int[] INSTRUCTION = new int[] {
			R.string.dashboard_instruction,
			R.string.loc_roc_remote_instruction,
			R.string.loc_roc_remote_instruction };

	private int mCount = CONTENT.length;
	private TutorialPagerActivity activity;

	public ViewPagerAdapter(FragmentManager fm,
			TutorialPagerActivity tutorialPagerActivity) {
		super(fm);
		activity = tutorialPagerActivity;
	}

	@Override
	public Fragment getItem(int position) {

		return AirTutorialViewFragment.newInstance(CONTENT[position],
				IMAGES[position], INSTRUCTION[position]);
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

	
}
