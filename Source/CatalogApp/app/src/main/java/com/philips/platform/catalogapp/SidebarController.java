/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.philips.platform.catalogapp.dataUtils.SidebarRightListViewAdapter;
import com.philips.platform.uid.drawable.SeparatorDrawable;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDUtils;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;
import com.philips.platform.uid.view.widget.SideBar;

public class SidebarController {

    private Context context;
    private MainActivity mainActivity;
    private ViewDataBinding activityMainBinding;

    private SideBar sideBarLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView contentThemedLeftRecyclerView;
    private RecyclerView navigationThemedLeftRecyclerView;
    private ListView contentThemedRightListView;
    private ListView navigationThemedRightListView;
    private static final String LEFT_SELECTED_POSITION = "LEFT_SELECTED_POSITION";
    private static final String IS_NAVIGATION_THEMED_LEFT_CONTAINER_VISIBLE = "IS_NAVIGATION_THEMED_LEFT_CONTAINER_VISIBLE";
    private static final String IS_NAVIGATION_THEMED_RIGHT_CONTAINER_VISIBLE = "IS_NAVIGATION_THEMED_RIGHT_CONTAINER_VISIBLE";
    private int leftRecyclerViewSelectedPosition = 0;
    private boolean isNavigationThemedLeftContainerVisible;
    private boolean isNavigationThemedRightContainerVisible;
    private LinearLayout sidebarLeftRoot;
    private SidebarFrameLayoutContainer contentThemedLeftSidebarRoot;
    private SidebarFrameLayoutContainer navigationThemedLeftSidebarRoot;
    private SidebarNavigationViewContainer contentThemedRightSidebarRoot;
    private SidebarNavigationViewContainer navigationThemedRightSidebarRoot;

    public SidebarController(final MainActivity mainActivity, final ViewDataBinding activityMainBinding) {
        context = mainActivity;
        this.mainActivity = mainActivity;
        this.activityMainBinding = activityMainBinding;
        initSidebarComponents();
    }

    private void initSidebarComponents(){

        sideBarLayout = (SideBar) activityMainBinding.getRoot().findViewById(R.id.sidebar_layout);
        sideBarLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        sideBarLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        sidebarLeftRoot = (LinearLayout) activityMainBinding.getRoot().findViewById(R.id.sidebar_left_root);
        contentThemedLeftSidebarRoot = (SidebarFrameLayoutContainer) activityMainBinding.getRoot().findViewById(R.id.sidebar_content_themed_left_root);
        navigationThemedLeftSidebarRoot = (SidebarFrameLayoutContainer) activityMainBinding.getRoot().findViewById(R.id.sidebar_navigation_themed_left_root);

        contentThemedRightSidebarRoot = (SidebarNavigationViewContainer) activityMainBinding.getRoot().findViewById(R.id.sidebar_content_themed_right_root);
        navigationThemedRightSidebarRoot = (SidebarNavigationViewContainer) activityMainBinding.getRoot().findViewById(R.id.sidebar_navigation_themed_right_root);

        initLeftSidebarRecyclerViews();
        initRightSidebarListViews();

        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // event when click home button
                if (mainActivity.getNavigationController().hasBackStack()) {
                    mainActivity.onBackPressed();
                }
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(mainActivity, sideBarLayout, mainActivity.getNavigationController().getToolbar(), R.string.sidebar_open,  R.string.sidebar_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
    }

    private void initRightSidebarListViews() {

        contentThemedRightListView = (ListView) activityMainBinding.getRoot().findViewById(R.id.sidebar_content_themed_right_listview);
        navigationThemedRightListView = (ListView) activityMainBinding.getRoot().findViewById(R.id.sidebar_navigation_themed_right_listview);

        ImageView sidebarRightImg = (ImageView) activityMainBinding.getRoot().findViewById(R.id.sidebar_right_header_image);
        sidebarRightImg.setPadding(0, UIDUtils.getStatusBarHeight(context), 0, 0);

        final SeparatorDrawable contentThemedSeparatorDrawable = new SeparatorDrawable(context);
        contentThemedRightListView.setDivider(contentThemedSeparatorDrawable);
        contentThemedRightListView.setDividerHeight(contentThemedSeparatorDrawable.getHeight());

        ArrayAdapter contentThemedArrayAdapter = new SidebarRightListViewAdapter(context, R.layout.sidebar_right_listview_item, context.getResources().getStringArray(R.array.sidebar_right_menu_items), false);
        contentThemedRightListView.setAdapter(contentThemedArrayAdapter);
        contentThemedRightListView.setItemChecked(0, true);
        contentThemedRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contentThemedRightListView.setItemChecked(position, true);
                navigationThemedRightListView.setItemChecked(position, true);
                sideBarLayout.closeDrawer(GravityCompat.END);
            }
        });

        final SeparatorDrawable navigationThemedSeparatorDrawable = new SeparatorDrawable(ThemeUtils.getNavigationThemedContext(context));
        navigationThemedRightListView.setDivider(navigationThemedSeparatorDrawable);
        navigationThemedRightListView.setDividerHeight(navigationThemedSeparatorDrawable.getHeight());

        ArrayAdapter navigationThemedArrayAdapter = new SidebarRightListViewAdapter(ThemeUtils.getNavigationThemedContext(context), R.layout.sidebar_right_listview_item, context.getResources().getStringArray(R.array.sidebar_right_menu_items), true );
        navigationThemedRightListView.setAdapter(navigationThemedArrayAdapter);
        navigationThemedRightListView.setItemChecked(0, true);
        navigationThemedRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigationThemedRightListView.setItemChecked(position, true);
                contentThemedRightListView.setItemChecked(position, true);
                sideBarLayout.closeDrawer(GravityCompat.END);
            }
        });
    }

    private void initLeftSidebarRecyclerViews() {

        RecyclerViewSeparatorItemDecoration contentThemedSeparatorItemDecoration = new RecyclerViewSeparatorItemDecoration(context);
        RecyclerViewSeparatorItemDecoration navigationThemedSeparatorItemDecoration = new RecyclerViewSeparatorItemDecoration(ThemeUtils.getNavigationThemedContext(context));
        DataHolderView contentThemedDataHolderView = getIconDataHolderView(context);
        DataHolderView navigationThemedDataHolderView = getIconDataHolderView(ThemeUtils.getNavigationThemedContext(context));

        contentThemedLeftRecyclerView = (RecyclerView) activityMainBinding.getRoot().findViewById(R.id.sidebar_content_themed_left_recyclerview);
        contentThemedLeftRecyclerView.setAdapter(new SidebarRecyclerViewAdapter(contentThemedDataHolderView.dataHolders, false));
        contentThemedLeftRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        contentThemedLeftRecyclerView.addItemDecoration(contentThemedSeparatorItemDecoration);

        navigationThemedLeftRecyclerView = (RecyclerView) activityMainBinding.getRoot().findViewById(R.id.sidebar_navigation_themed_left_recyclerview);
        navigationThemedLeftRecyclerView.setAdapter(new SidebarRecyclerViewAdapter(navigationThemedDataHolderView.dataHolders, true));
        navigationThemedLeftRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        navigationThemedLeftRecyclerView.addItemDecoration(navigationThemedSeparatorItemDecoration);
    }

    @NonNull
    private DataHolderView getIconDataHolderView(Context context) {
        DataHolderView dataHolderView = new DataHolderView();
        dataHolderView.addIconItem(R.drawable.ic_add_folder, R.string.menu1, context);
        dataHolderView.addIconItem(R.drawable.ic_home, R.string.menu2, context);
        dataHolderView.addIconItem(R.drawable.ic_lock, R.string.menu3, context);
        dataHolderView.addIconItem(R.drawable.ic_alarm, R.string.menu4, context);
        dataHolderView.addIconItem(R.drawable.ic_bottle, R.string.menu5, context);
        dataHolderView.addIconItem(R.drawable.ic_location, R.string.menu6, context);
        return dataHolderView;
    }

    private class SidebarRecyclerViewAdapter extends RecyclerView.Adapter {

        private ObservableArrayList<DataHolder> dataHolders;
        private LayoutInflater inflater;
        private boolean isNavigationContext;

        private SidebarRecyclerViewAdapter(@NonNull final ObservableArrayList<DataHolder> dataHolders, boolean isNavigationContext) {
            this.dataHolders = dataHolders;
            this.isNavigationContext = isNavigationContext;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(isNavigationContext)
                inflater = inflater.cloneInContext(ThemeUtils.getNavigationThemedContext(parent.getContext()));
            View v = inflater.inflate(R.layout.sidebar_left_recyclerview_item, parent, false);

            return new SidebarRecyclerViewBindingHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((SidebarRecyclerViewBindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((SidebarRecyclerViewBindingHolder) holder).getBinding().executePendingBindings();

            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    holder.itemView.setSelected(leftRecyclerViewSelectedPosition == position);
                }
            });

            ((SidebarRecyclerViewBindingHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    contentThemedLeftRecyclerView.getAdapter().notifyItemChanged(leftRecyclerViewSelectedPosition);
                    navigationThemedLeftRecyclerView.getAdapter().notifyItemChanged(leftRecyclerViewSelectedPosition);
                    leftRecyclerViewSelectedPosition = position;
                    holder.itemView.setSelected(true);
                    contentThemedLeftRecyclerView.getAdapter().notifyItemChanged(leftRecyclerViewSelectedPosition);
                    navigationThemedLeftRecyclerView.getAdapter().notifyItemChanged(leftRecyclerViewSelectedPosition);
                    sideBarLayout.closeDrawer(GravityCompat.START);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataHolders.size();
        }

        class SidebarRecyclerViewBindingHolder extends RecyclerView.ViewHolder {
            private ViewDataBinding binding;

            SidebarRecyclerViewBindingHolder(@NonNull View rowView) {
                super(rowView);
                binding = DataBindingUtil.bind(rowView);
            }

            public ViewDataBinding getBinding() {
                return binding;
            }
        }
    }

    public SideBar getSideBar() {
        return sideBarLayout;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    public void onSaveInstance(final Bundle outState) {
        outState.putInt(LEFT_SELECTED_POSITION, leftRecyclerViewSelectedPosition);
        outState.putBoolean(IS_NAVIGATION_THEMED_LEFT_CONTAINER_VISIBLE, isNavigationThemedLeftContainerVisible);
        outState.putBoolean(IS_NAVIGATION_THEMED_RIGHT_CONTAINER_VISIBLE, isNavigationThemedRightContainerVisible);
    }

    public void initSidebarContainerState(final Bundle savedInstanceState) {
        leftRecyclerViewSelectedPosition = savedInstanceState.getInt(LEFT_SELECTED_POSITION);
        isNavigationThemedLeftContainerVisible = savedInstanceState.getBoolean(IS_NAVIGATION_THEMED_LEFT_CONTAINER_VISIBLE);
        isNavigationThemedRightContainerVisible = savedInstanceState.getBoolean(IS_NAVIGATION_THEMED_RIGHT_CONTAINER_VISIBLE);
        if(isNavigationThemedLeftContainerVisible){
            showNavigationThemedLeftComponents();
        } else {
            showContentThemedLeftComponents();
        }
        if(isNavigationThemedRightContainerVisible){
            showNavigationThemedRightComponents();
        } else {
            showContentThemedRightComponents();
        }
    }

    public void showContentThemedLeftComponents(){
        isNavigationThemedLeftContainerVisible = false;
        navigationThemedLeftSidebarRoot.setVisibility(View.GONE);
        sidebarLeftRoot.setBackgroundColor(getContentMappedBGColor());
        contentThemedLeftSidebarRoot.setVisibility(View.VISIBLE);
    }

    public void showNavigationThemedLeftComponents(){
        isNavigationThemedLeftContainerVisible = true;
        contentThemedLeftSidebarRoot.setVisibility(View.GONE);
        sidebarLeftRoot.setBackgroundColor(getNavigationMappedBGColor());
        navigationThemedLeftSidebarRoot.setVisibility(View.VISIBLE);
    }

    public void showContentThemedRightComponents(){
        isNavigationThemedRightContainerVisible = false;
        navigationThemedRightSidebarRoot.setVisibility(View.GONE);
        contentThemedRightSidebarRoot.setVisibility(View.VISIBLE);
    }

    public void showNavigationThemedRightComponents(){
        isNavigationThemedRightContainerVisible = true;
        contentThemedRightSidebarRoot.setVisibility(View.GONE);
        navigationThemedRightSidebarRoot.setVisibility(View.VISIBLE);
    }

    private int getContentMappedBGColor(){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uidContentPrimaryBackgroundColor});
        int sidebarBGColor = 0;
        if (typedArray != null) {
            sidebarBGColor = typedArray.getColor(0, Color.WHITE);
            typedArray.recycle();
        }
        return sidebarBGColor;
    }

    private int getNavigationMappedBGColor(){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uidNavigationPrimaryBackgroundColor});
        int sidebarBGColor = 0;
        if (typedArray != null) {
            sidebarBGColor = typedArray.getColor(0, Color.WHITE);
            typedArray.recycle();
        }
        return sidebarBGColor;
    }

}