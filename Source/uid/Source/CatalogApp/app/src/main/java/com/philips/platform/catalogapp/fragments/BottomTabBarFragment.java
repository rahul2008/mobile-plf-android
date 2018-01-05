/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.catalogapp.DataHolder;
import com.philips.platform.catalogapp.DataHolderView;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentBottomTabBarBinding;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;
import com.philips.platform.uid.view.widget.UIDTabItem;

public class BottomTabBarFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    private FragmentBottomTabBarBinding fragmentBottomTabBarBinding;
    private com.philips.platform.uid.view.widget.TabLayout tabLayoutIconOnly;
    private com.philips.platform.uid.view.widget.TabLayout tabLayoutWithTitle;
    private AppCompatImageView galleryIcon;
    private int [] resourceArray;
    private RelativeLayout iconLayout, listLayout;

    @Override
    public int getPageTitle() {
        return R.string.page_title_bottom_tab_bar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentBottomTabBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_tab_bar, container, false);
        fragmentBottomTabBarBinding.setFrag(this);
        tabLayoutIconOnly = fragmentBottomTabBarBinding.getRoot().findViewById(R.id.tab_layout_icon_only);
        tabLayoutIconOnly.addOnTabSelectedListener(this);
        tabLayoutWithTitle = fragmentBottomTabBarBinding.getRoot().findViewById(R.id.tab_layout_with_title);
        tabLayoutWithTitle.addOnTabSelectedListener(this);
        iconLayout = fragmentBottomTabBarBinding.getRoot().findViewById(R.id.icon_layout);
        listLayout = fragmentBottomTabBarBinding.getRoot().findViewById(R.id.list_layout);
        galleryIcon = fragmentBottomTabBarBinding.getRoot().findViewById(R.id.gallery_icon);
        resourceArray = new int[] {R.drawable.search_icon, R.drawable.cart_icon, R.drawable.notification_icon, R.drawable.profile_icon, R.drawable.threedotshorizontal_icon};

        initRecyclerView();
        initTabs();
        return fragmentBottomTabBarBinding.getRoot();
    }

    private void initRecyclerView() {
        DataHolderView dataHolderView = new DataHolderView();
        dataHolderView.addIconItem(R.drawable.capture_icon, R.string.capture, getContext());
        dataHolderView.addIconItem(R.drawable.balloonspeech_icon, R.string.speech, getContext());
        fragmentBottomTabBarBinding.recyclerView.addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
        fragmentBottomTabBarBinding.recyclerView.setAdapter(new RecyclerViewAdapter(dataHolderView.dataHolders));
        fragmentBottomTabBarBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    static class RecyclerViewAdapter extends RecyclerView.Adapter {
        private ObservableArrayList<DataHolder> dataHolders;

        private RecyclerViewAdapter(@NonNull final ObservableArrayList<DataHolder> dataHolders) {
            this.dataHolders = dataHolders;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabbar_recyclerview_item, parent, false);
            return new BindingHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((BindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((BindingHolder) holder).getBinding().executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return dataHolders.size();
        }

        static class BindingHolder extends RecyclerView.ViewHolder {

            private ViewDataBinding binding;

            private BindingHolder(@NonNull View rowView) {
                super(rowView);
                binding = DataBindingUtil.bind(rowView);
            }

            public ViewDataBinding getBinding() {
                return binding;
            }
        }
    }

    private void initTabs(){

        if(BottomTabSettingsFragment.isIconOnlyEnabled.get()) {

            tabLayoutIconOnly.setVisibility(View.VISIBLE);
            tabLayoutWithTitle.setVisibility(View.GONE);

            switch (BottomTabSettingsFragment.itemSelection.get()) {
                case 1:
                    if(tabLayoutIconOnly.getChildCount() > 4){
                        tabLayoutIconOnly.removeTabAt(4);
                    }
                    break;
                case 2:
                    if(tabLayoutIconOnly.getChildCount() < 5){
                        tabLayoutIconOnly.addView(getMoreItemIconOnly());
                    }
                    break;
                default:
                    break;
            }
        } else {
            tabLayoutIconOnly.setVisibility(View.GONE);
            tabLayoutWithTitle.setVisibility(View.VISIBLE);

            switch (BottomTabSettingsFragment.itemSelection.get()) {
                case 1:
                    if(tabLayoutWithTitle.getChildCount() > 4){
                        tabLayoutWithTitle.removeTabAt(4);
                    }
                    break;
                case 2:
                    if(tabLayoutWithTitle.getChildCount() < 5){
                        tabLayoutWithTitle.addView(getMoreItemWithTitle());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Drawable getMoreIconFontDrawable(){
        return new FontIconDrawable(getContext(), getResources().getString(com.philips.platform.uid.R.string.dls_threedotshorizontal), Typeface.createFromAsset(getContext().getAssets(), "fonts/iconfont.ttf"))
                .colorStateList(ThemeUtils.buildColorStateList(getContext(), com.philips.platform.uid.R.color.uid_tab_icon_selector))
                .sizeDp(24);
    }

    private UIDTabItem getMoreItemIconOnly() {
        UIDTabItem moreItemIconOnly = new UIDTabItem(getContext(), false);
        moreItemIconOnly.setIcon(getMoreIconFontDrawable());
        return moreItemIconOnly;
    }

    private UIDTabItem getMoreItemWithTitle() {
        UIDTabItem moreItemWithTitle = new UIDTabItem(getContext(), true);
        moreItemWithTitle.setIcon(getMoreIconFontDrawable());
        moreItemWithTitle.setTitle("More");
        return moreItemWithTitle;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        TypedArray typedArray = getContext().obtainStyledAttributes(new int[]{com.philips.platform.uid.R.attr.uidTabsDefaultNormalOffIconColor});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        if(tab.getPosition() == 4) {
            iconLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
        } else {
            listLayout.setVisibility(View.GONE);
            iconLayout.setVisibility(View.VISIBLE);
            galleryIcon.setImageDrawable(getResources().getDrawable(resourceArray[tab.getPosition()]));
            galleryIcon.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}