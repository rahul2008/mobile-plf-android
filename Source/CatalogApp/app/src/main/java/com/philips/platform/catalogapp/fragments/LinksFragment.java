package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentLinksBinding;
import com.philips.platform.uid.utils.UIDClickableSpanWrapper;

public class LinksFragment extends BaseFragment {
    public static final String PHILIPS_SITE = "http://www.philips.com";
    UIDClickableSpanWrapper.ClickInterceptor clickInterceptor = new UIDClickableSpanWrapper.ClickInterceptor() {
        @Override
        public boolean interceptClick(CharSequence tag) {
            if (tag.equals(PHILIPS_SITE)) {
                if (!isNetworkConnected()) {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                } else {
                    launchWebFragment(tag);
                }
                return true;
            }
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLinksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_links, container, false);
        binding.setFrag(this);
        binding.linkDescription.setMovementMethod(LinkMovementMethod.getInstance());
        binding.linkDescription.setSpanClickInterceptor(clickInterceptor);
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

    private void launchWebFragment(CharSequence tag) {
        Bundle bundle = new Bundle();
        bundle.putString(WebFragment.KEY_URL, String.valueOf(tag));
        WebFragment fragment = new WebFragment();
        fragment.setArguments(bundle);
        showFragment(fragment);
    }
}
