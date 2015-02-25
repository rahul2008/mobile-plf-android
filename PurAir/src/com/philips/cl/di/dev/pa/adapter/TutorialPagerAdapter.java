package com.philips.cl.di.dev.pa.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.TutorialPagerActivity;
import com.philips.cl.di.dev.pa.fragment.AirTutorialViewFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class TutorialPagerAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	private static final int[] CONTENT = new int[] {
			R.string.tutorial_dashboard_desc, R.string.tutorial_lOC_desc,
			R.string.tutorial_rOC_desc };
	private static final int[] IMAGES = new int[] { R.drawable.tutorial_page1,
			R.drawable.tutorial_page2, R.drawable.tutorial_page3 };
	private static final int[] INSTRUCTION = new int[] {
			R.string.dashboard_instruction,
			R.string.status_instruction,
			R.string.loc_roc_remote_instruction };

	private int mCount = CONTENT.length;
	private TutorialPagerActivity activity;

	public TutorialPagerAdapter(FragmentManager fm,
			TutorialPagerActivity tutorialPagerActivity) {
		super(fm);
		activity = tutorialPagerActivity;
	}

	@Override
	public Fragment getItem(int position) {
		
		AirTutorialViewFragment fragment = new AirTutorialViewFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(AirTutorialViewFragment.KEY_DESCRIP, CONTENT[position]);
		bundle.putInt(AirTutorialViewFragment.KEY_IMG, IMAGES[position]);
		bundle.putInt(AirTutorialViewFragment.KEY_INSTR, INSTRUCTION[position]);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String desc = activity.getString(TutorialPagerAdapter.CONTENT[position]);
		return desc;
	}

	@Override
	public int getIconResId(int index) {
		return IMAGES[index];
	}

	
}
