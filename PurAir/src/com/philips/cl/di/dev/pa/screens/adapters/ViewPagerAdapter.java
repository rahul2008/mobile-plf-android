package com.philips.cl.di.dev.pa.screens.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.pureairui.fragments.AirTutorialViewFragment;
import com.philips.cl.di.dev.pa.screens.AirTutorialActivity;
import com.viewpagerindicator.IconPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter{
	private static final int[] CONTENT = new int[] { 
		R.string.tutorial_dashboard_desc,
		R.string.tutorial_lOC_desc,
		R.string.tutorial_rOC_desc,
		R.string.tutorial_device_status_desc,
		R.string.tutorial_remote_control_desc};
	private static final int[] IMAGES = new int[] {
		R.drawable.air_tutorial_screen_one,
		R.drawable.air_tutorial_screen_two,
		R.drawable.air_tutorial_screen_three,
		R.drawable.air_purifier_screen4,
		R.drawable.air_purifier_screen5
	};
	private static final int[] INSTRUCTION= new int[]{
		R.string.dashboard_instruction,
		R.string.loc_roc_remote_instruction,
		R.string.loc_roc_remote_instruction,
		R.string.status_instruction,
		R.string.loc_roc_remote_instruction
	};

	private static final int[] instruction_list= new int[]{
		R.string.dashboard_instruction_1,
		R.string.dashboard_instruction_2,
		R.string.dashboard_instruction_3,
		R.string.dashboard_instruction_4
	};	

	private int mCount = CONTENT.length;
	private AirTutorialActivity activity;

	public ViewPagerAdapter(FragmentManager fm, AirTutorialActivity airTutorialActivity) {
		super(fm);
		activity=airTutorialActivity;
	}

	@Override
	public Fragment getItem(int position) {
		if(position==0)
			return AirTutorialViewFragment.newInstance(CONTENT[position], IMAGES[position], INSTRUCTION[position], instruction_list);
		else
			return AirTutorialViewFragment.newInstance(CONTENT[position], IMAGES[position], INSTRUCTION[position], null);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String desc= activity.getString(ViewPagerAdapter.CONTENT[position]);
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
