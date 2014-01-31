package com.philips.cl.di.ews.test;

import com.philips.cl.di.ews.EasyWifiSetupActivity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class EasyWifiSetupActivityTest extends
		android.test.ActivityInstrumentationTestCase2<EasyWifiSetupActivity> {

	private static final Object ADAPTER_COUNT = 7;
	private EasyWifiSetupActivity mActivity;
	private ViewPager mPager;
	private PagerAdapter mPageData;
	private EasyWifiSetupStub mStub;

	public EasyWifiSetupActivityTest() {
		super("com.philips.cl.di.ews", EasyWifiSetupActivity.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mStub = new EasyWifiSetupStub(mActivity,mActivity,"iSiPiSi","http://192.168.1.1");
		mActivity.setService(mStub); 
		mPager = (ViewPager) mActivity.findViewById(com.philips.cl.di.ews.R.id.pager);
		mPageData = mPager.getAdapter();

	}

	public void test00PreConditions() {
		assertTrue(mPager != null);
		assertTrue(mPageData != null);
		assertEquals(mPageData.getCount(), ADAPTER_COUNT);
	}

	public void asyncTestNextPage(final int page) {
		final int previous =page -1;
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				mPager.requestFocus();
				mPager.setCurrentItem(previous);
				mActivity.goToPage(page);
			}
			
		});
		try {
			Thread.sleep(1000);
				assertEquals(page, mPager.getCurrentItem());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void asyncTestLastPage(final int page) {
		final int next = page +1;
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				mPager.requestFocus();
				mPager.setCurrentItem(next);
				mActivity.goBack(mPager);
			}
			
		});
		try {
			Thread.sleep(1000);
				assertEquals(page, mPager.getCurrentItem());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	
		public void testPage01() {
			asyncTestNextPage(1);
		}
		public void testPage02(){
			asyncTestNextPage(2);
		}

		public void testPage03() {
			asyncTestNextPage(3);
		}

		public void testPage04() {
			asyncTestNextPage(4);
		}

		public void testPage25() {
			asyncTestNextPage(5);
		}

		
		
		public void testPage10() {
			asyncTestLastPage(0);

		}
		public void testPage11() {
			asyncTestLastPage(1);

		}
		public void testPage12(){
			asyncTestLastPage(2);
		}

		public void testPage13() {
			asyncTestLastPage(3);
		}

		public void testPage14() {
			asyncTestLastPage(4);

		}

		public void testStart() {
			try{
				//require manual interaction
				EasyWifiSetupActivity.start(mActivity, "Philips Setup", "http://192.168.1.1/di/v1/wifi");
			}catch(Exception e){
				assertTrue(e.toString() =="Null");
			}
		}

		
}