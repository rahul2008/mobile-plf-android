package com.philips.cl.di.dev.pa.screens;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.interfaces.SizeCallback;
import com.philips.cl.di.dev.pa.screens.adapters.MenuListAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.CustomHorizontalScrollView;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class BaseActivity.
 * This class contains all the base / common functionalities.
 */
public abstract class BaseActivity extends Activity {

	/** The scroll view. */
	protected CustomHorizontalScrollView scrollView;
	
	/** The left menu , centerview , right settings. */
	protected View leftMenu, centerView, rightSettings;
	
	/** The menu out. */
	protected boolean menuOut ;
	
	/** The inflater. */
	protected LayoutInflater inflater;
	
	/** The screenwidth. */
	private static int screenwidth ; 
	
	/** The Constant TAG. */
	private static final String TAG = BaseActivity.class.getName();
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "--BaseActivity.onCreate--");
		inflater = LayoutInflater.from(this);
		scrollView = (CustomHorizontalScrollView) inflater.inflate(
				R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		screenwidth = Utils.getScreenWidth(this);
		initializeLeftAndRightNavigationalViews();
		centerView = getCenterView();
		populateScrollView();

	}

	/**
	 * Initialize left and right navigational views.
	 */
	void initializeLeftAndRightNavigationalViews() {
		// Left Menu - Common for all activities
		leftMenu = inflater.inflate(R.layout.left_menu, null);
		ListView lvMenu = (ListView) leftMenu.findViewById(R.id.lvMenu);
		Utils.getIconArray();
		Utils.getLabelArray();
		lvMenu.setAdapter(new MenuListAdapter(getApplicationContext(), 0, Utils
				.getIconArray(), Utils.getLabelArray()));
		// Settings Page - Common
		rightSettings = inflater.inflate(R.layout.activity_settings, null);

	}

	/**
	 * Populate scroll view.
	 */
	void populateScrollView() {
		final View[] children = new View[] { leftMenu, centerView,
				rightSettings };
		scrollView.initViews(children, AppConstants.SCROLLTOVIEWID, new SizeCallbackForMenu(
				getApplicationContext()));

	}

	/**
	 * Gets the center view.
	 *
	 * @return the center view
	 */
	protected abstract View getCenterView();

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's
	 * width.
	 */
	static class ClickListenerForScrolling implements OnClickListener {
		
		/** The scroll view. */
		HorizontalScrollView scrollView;
		
		/** The menu. */
		View menu;
		/**
		 * Menu must NOT be out/shown to start with.
		 */
		boolean menuOut = false;
		
		/** The settings out. */
		boolean settingsOut = false;

		/**
		 * Instantiates a new click listener for scrolling.
		 *
		 * @param scrollView the scroll view
		 * @param menu the menu
		 */
		public ClickListenerForScrolling(HorizontalScrollView scrollView,
				View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
		}

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			int menuWidth = menu.getMeasuredWidth();
			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);
			switch (v.getId()) {
			case R.id.ivMenu:
				if (!menuOut) {
					int left = 0;
					scrollView.smoothScrollTo(left, 0);
				} else {
					int left = menuWidth;
					scrollView.smoothScrollTo(left, 0);
				}
				menuOut = !menuOut;
				break;

			case R.id.ivSettings:
				if (!settingsOut) {
					scrollView.smoothScrollBy(screenwidth, 0);
				} else {
					scrollView.smoothScrollTo(0, 0);
				}
				settingsOut = !settingsOut;
				break;
			}
		}
	}

	/**
	 * Helper that remembers the width of the 'slide' button, so that the
	 * 'slide' button remains in view, even when the menu is showing.
	 */
	static class SizeCallbackForMenu implements SizeCallback {
		
		/** The width. */
		int Width;
		/** The context. */
		Context context;

		/**
		 * Instantiates a new size callback for menu.
		 *
		 * @param context the context
		 */
		public SizeCallbackForMenu(Context context) {
			super();
			this.context = context;
		}

		/* (non-Javadoc)
		 * @see com.philips.cl.di.dev.pa.interfaces.SizeCallback#onGlobalLayout()
		 */
		@Override
		public void onGlobalLayout() {
			Width= Utils.getScreenWidth(context);
		}

		/* (non-Javadoc)
		 * @see com.philips.cl.di.dev.pa.interfaces.SizeCallback#getViewSize(int, int, int, int[])
		 */
		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = (int)((Width) * (AppConstants.SCALELEFTMENU));
			}
		}
	}

}
