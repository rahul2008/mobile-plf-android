package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentLinksBinding;
import com.philips.platform.uid.utils.UIDClickableSpan;
import com.philips.platform.uid.utils.UIDClickableSpanWrapper;
import com.philips.platform.uid.view.widget.Label;

public class LinksFragment extends BaseFragment {
    public static final String PHILIPS_SITE = "http://www.philips.com";
    UIDClickableSpanWrapper.ClickInterceptor clickInterceptor = new UIDClickableSpanWrapper.ClickInterceptor() {
        @Override
        public boolean interceptClick(CharSequence tag) {
            if (tag!= null && tag.equals(PHILIPS_SITE)) {
                if (!isNetworkConnected() && getContext() != null) {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLinksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_links, container, false);
        binding.setFrag(this);
        binding.linkDescription.setMovementMethod(LinkMovementMethod.getInstance());
        binding.linkDescription.setSpanClickInterceptor(clickInterceptor);
        addLinkButton(binding);
        return binding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_links;
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

    private void addLinkButton(FragmentLinksBinding binding) {
        UIDClickableSpan span = new UIDClickableSpan(buttonFragmentRunnable);
        Label buttonLabel = binding.buttonLink;
        SpannableString string = SpannableString.valueOf(buttonLabel.getText());
        string.setSpan(span, 0, string.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        buttonLabel.setText(string);

        buttonLabel.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
