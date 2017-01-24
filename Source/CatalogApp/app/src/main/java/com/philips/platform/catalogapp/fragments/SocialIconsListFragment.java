/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.content.Context;
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

import com.philips.platform.catalogapp.BR;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSocialiconListBinding;
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
        FragmentSocialiconListBinding fragmentSocialiconGridBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_socialicon_list, container, false);
        fragmentSocialiconGridBinding.setFragment(this);
        final List<Integer> socialIconsGridItems = initSocialIconsGridItems();
        fragmentSocialiconGridBinding.primarySocialIconsGridView.setAdapter(new PrimarySocialIconsAdapter(socialIconsGridItems));
        showWhiteSocialIcons(fragmentSocialiconGridBinding, socialIconsGridItems);

        return fragmentSocialiconGridBinding.getRoot();
    }

    private void showWhiteSocialIcons(final FragmentSocialiconListBinding fragmentSocialiconGridBinding, final List<Integer> socialIconsGridItems) {
        if (supportsWhiteSocialIcons()) {
            fragmentSocialiconGridBinding.whiteSocialIconsGridView.setAdapter(new WhiteSocialIconsAdapter(socialIconsGridItems));
        }
    }

    @NonNull
    private List<Integer> initSocialIconsGridItems() {
        final List<Integer> lists = new ArrayList<>();
        lists.add(R.drawable.ic_social_media_amazon);
        lists.add(R.drawable.ic_social_media_blog);
        lists.add(R.drawable.ic_social_media_facebook);
        lists.add(R.drawable.ic_social_media_google);
        lists.add(R.drawable.ic_social_media_googleplus);
        lists.add(R.drawable.ic_social_media_imgres);
        lists.add(R.drawable.ic_social_media_instagram);
        lists.add(R.drawable.ic_social_media_kaixin);
        lists.add(R.drawable.ic_social_media_linkedin);
        lists.add(R.drawable.ic_social_media_pinterest);
        lists.add(R.drawable.ic_social_media_qq);
        lists.add(R.drawable.ic_social_media_qzone);
        lists.add(R.drawable.ic_social_media_renren);
        lists.add(R.drawable.ic_social_media_sinaweibo);
        lists.add(R.drawable.ic_social_media_stumbleupon);
        lists.add(R.drawable.ic_social_media_twitter);
        lists.add(R.drawable.ic_social_media_vkontacte);
        lists.add(R.drawable.ic_social_media_wechat);
        lists.add(R.drawable.ic_social_media_weibo);
        lists.add(R.drawable.ic_social_media_youtube);
        return lists;
    }

    public boolean supportsWhiteSocialIcons() {
        final ThemeHelper themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getContext()));
        final ContentColor contentColor = themeHelper.initContentTonalRange();

        return !(contentColor == ContentColor.ULTRA_LIGHT || contentColor == ContentColor.VERY_LIGHT);
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

        final private List<Integer> data;

        public PrimarySocialIconsAdapter(@NonNull final List<Integer> lists) {
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
            final int drawableid = data.get(position);
            SocialIconsViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                final ViewDataBinding inflate = DataBindingUtil.inflate(inflater, getLayoutId(), parent, false);
                final View view = inflate.getRoot();
                holder = new SocialIconsViewHolder(view);
                convertView = view;
                convertView.setTag(holder);
            }
            holder = (SocialIconsViewHolder) convertView.getTag();

            final Drawable icon = getIcon(drawableid, parent.getContext()).getConstantState().newDrawable().mutate();
            holder.getBindingView().setVariable(BR.icon, icon);
            holder.getBindingView().executePendingBindings();
            return holder.getBindingView().getRoot();
        }

        private Drawable getIcon(final int iconId, final Context context) {
            return VectorDrawableCompat.create(context.getResources(), iconId, context.getTheme());
        }

        protected int getSocialIconId() {
            return R.id.primarySocialIcon;
        }

        protected int getLayoutId() {
            return R.layout.primary_social_icon_item;
        }
    }

    static class WhiteSocialIconsAdapter extends PrimarySocialIconsAdapter {
        public WhiteSocialIconsAdapter(final List<Integer> lists) {
            super(lists);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.white_social_icon_item;
        }

        protected int getSocialIconId() {
            return R.id.whiteSocialIcon;
        }
    }
}
