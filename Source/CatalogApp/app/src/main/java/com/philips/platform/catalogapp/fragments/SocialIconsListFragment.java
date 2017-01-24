/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.databinding.library.baseAdapters.BR;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSocialiconGridBinding;
import com.philips.platform.catalogapp.themesettings.ThemeHelper;
import com.philips.platform.uid.thememanager.ContentColor;

import java.util.ArrayList;
import java.util.List;

public class SocialIconsListFragment extends BaseFragment {
    @Override
    public int getPageTitle() {
        return R.string.page_title_social_icon;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        FragmentSocialiconGridBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_socialicon_grid, container, false);
        dataBinding.setFragment(this);
        final List<Drawable> lists = initSocialIconsGridItems();
        dataBinding.primarySocialIconsGridView.setAdapter(new PrimarySocialIconsAdapter(lists));
        if (supportsWhiteSocialIcons()) {
            dataBinding.whiteSocialIconsGridView.setAdapter(new WhiteSocialIconsAdapter(lists));
        }
        return dataBinding.getRoot();
    }

    @NonNull
    private List<Drawable> initSocialIconsGridItems() {
        final List<Drawable> lists = new ArrayList<>();
        lists.add(getIcon(R.drawable.ic_social_media_amazon));
        lists.add(getIcon(R.drawable.ic_social_media_blog));
        lists.add(getIcon(R.drawable.ic_social_media_facebook));
        lists.add(getIcon(R.drawable.ic_social_media_google));
        lists.add(getIcon(R.drawable.ic_social_media_googleplus));
        lists.add(getIcon(R.drawable.ic_social_media_imgres));
        lists.add(getIcon(R.drawable.ic_social_media_instagram));
        lists.add(getIcon(R.drawable.ic_social_media_kaixin));
        lists.add(getIcon(R.drawable.ic_social_media_linkedin));
        lists.add(getIcon(R.drawable.ic_social_media_pinterest));
        lists.add(getIcon(R.drawable.ic_social_media_qq));
        lists.add(getIcon(R.drawable.ic_social_media_qzone));
        lists.add(getIcon(R.drawable.ic_social_media_renren));
        lists.add(getIcon(R.drawable.ic_social_media_sinaweibo));
        lists.add(getIcon(R.drawable.ic_social_media_stumbleupon));
        lists.add(getIcon(R.drawable.ic_social_media_twitter));
        lists.add(getIcon(R.drawable.ic_social_media_vkontacte));
        lists.add(getIcon(R.drawable.ic_social_media_wechat));
        lists.add(getIcon(R.drawable.ic_social_media_weibo));
        lists.add(getIcon(R.drawable.ic_social_media_youtube));
        return lists;
    }

    public boolean supportsWhiteSocialIcons() {
        final ThemeHelper themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getContext()));
        final ContentColor contentColor = themeHelper.initContentTonalRange();

        return !(contentColor == ContentColor.ULTRA_LIGHT || contentColor == ContentColor.VERY_LIGHT);
    }

    private Drawable getIcon(final int iconId) {
        return VectorDrawableCompat.create(getResources(), iconId, getContext().getTheme());
    }

    static class SocialIconsViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding bindingView;

        public SocialIconsViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBindingView() {
            return bindingView;
        }
    }

    static class PrimarySocialIconsAdapter extends BaseAdapter {

        final private List<Drawable> data;

        public PrimarySocialIconsAdapter(@NonNull final List<Drawable> lists) {
            this.data = lists;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(final int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, @NonNull View convertView, @NonNull final ViewGroup parent) {
            final Drawable drawable = data.get(position);
            SocialIconsViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(getLayoutId(), parent, false);
                holder = new SocialIconsViewHolder(view);
                convertView = view;
                convertView.setTag(holder);
            }
            holder = (SocialIconsViewHolder) convertView.getTag();
            holder.getBindingView().setVariable(BR.icon, drawable);
            holder.getBindingView().executePendingBindings();
            return holder.getBindingView().getRoot();
        }

        protected int getLayoutId() {
            return R.layout.primary_social_icon_item;
        }
    }

    static class WhiteSocialIconsAdapter extends PrimarySocialIconsAdapter {
        public WhiteSocialIconsAdapter(final List<Drawable> lists) {
            super(lists);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.white_social_icon_item;
        }
    }
}
