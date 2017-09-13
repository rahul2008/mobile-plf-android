package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentLinksBinding;
import com.philips.platform.catalogapp.events.OptionMenuClickedEvent;
import com.philips.platform.uid.text.utils.UIDClickableSpan;
import com.philips.platform.uid.text.utils.UIDClickableSpanWrapper;
import com.philips.platform.uid.view.widget.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LinksFragment extends BaseFragment {
    public static final String PHILIPS_SITE = "http://www.philips.com";

    private boolean wasConfluenceHelpShowing = true;
    private PopupWindow confluenceHelpPopUP;
    private View mainLayout;

    UIDClickableSpanWrapper.ClickInterceptor clickInterceptor = new UIDClickableSpanWrapper.ClickInterceptor() {
        @Override
        public boolean interceptClick(CharSequence tag) {
            if (tag != null && tag.equals(PHILIPS_SITE)) {
                if (!isNetworkConnected() && getContext() != null) {
                    Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_LONG).show();
                    return true;
                }
            }
            return false;
        }
    };

    private Runnable buttonFragmentRunnable = new Runnable() {
        @Override
        public void run() {
            launchButtonFragment();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLinksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_links, container, false);
        binding.setFrag(this);
        mainLayout = binding.mainLayout;
        binding.linkDescription.setSpanClickInterceptor(clickInterceptor);
        addLinksToButton(binding);
        showConfluenceHelpPopUP(savedInstanceState);

        return binding.getRoot();
    }

    private void showConfluenceHelpPopUP(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            wasConfluenceHelpShowing = savedInstanceState.getBoolean("showingHelp");
        }
        if (wasConfluenceHelpShowing) {
            mainLayout.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && LinksFragment.this.isVisible()) {
                        confluenceHelpPopUP = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        confluenceHelpPopUP.setContentView(getConfluencePageHelpView());
                        confluenceHelpPopUP.showAsDropDown(getActivity().findViewById(R.id.uid_toolbar));
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(OptionMenuClickedEvent event) {
        dismissPopUP();
    }


    @Override
    public int getPageTitle() {
        return R.string.page_title_links;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("showingHelp", wasConfluenceHelpShowing);
    }

    @Override
    public void onDestroy() {
        dismissPopUP();
        super.onDestroy();
    }

    private void dismissPopUP() {
        if (confluenceHelpPopUP != null && confluenceHelpPopUP.isShowing()) {
            confluenceHelpPopUP.dismiss();
        }
    }

    private boolean isNetworkConnected() {
        Context context = getContext();
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;
        }
        return false;
    }

    private void launchButtonFragment() {
        Bundle bundle = new Bundle();
        ButtonFragment fragment = new ButtonFragment();
        fragment.setArguments(bundle);
        showFragment(fragment);
    }

    private void addLinksToButton(FragmentLinksBinding binding) {
        UIDClickableSpan span = new UIDClickableSpan(buttonFragmentRunnable);
        Label buttonLabel = binding.buttonLink;
        SpannableString string = SpannableString.valueOf(buttonLabel.getText());
        string.setSpan(span, 0, string.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        buttonLabel.setText(string);
    }

    protected View getConfluencePageHelpView() {
        View view = View.inflate(getContext(), R.layout.uid_notification_bg_white, null);
        ((TextView) view.findViewById(R.id.uid_notification_title)).setText(R.string.info_notification_note);
        ((TextView) view.findViewById(R.id.uid_notification_content)).setText(R.string.info_notification_bar_content);
        final TextView gotIButton = (TextView) view.findViewById(R.id.uid_notification_btn_1);

        gotIButton.setText(R.string.info_notification_got_it);
        gotIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.VISIBLE);
                confluenceHelpPopUP.dismiss();
                wasConfluenceHelpShowing = false;
            }
        });

        view.findViewById(R.id.uid_notification_title).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_btn_1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_btn_2).setVisibility(View.GONE);

        return view;
    }
}
