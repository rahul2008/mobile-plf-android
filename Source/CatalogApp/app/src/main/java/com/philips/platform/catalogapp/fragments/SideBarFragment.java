package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.DataHolder;
import com.philips.platform.catalogapp.DataHolderView;
import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSideBarBinding;
//import com.philips.platform.catalogapp.databinding.SidebarViewBinding;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;
import com.philips.platform.uid.view.widget.SideBar;

/**
 * Created by Kunal on 31/07/17.
 */

public class SideBarFragment extends BaseFragment {

    private Context context;
    private SideBar sideBarLayout;
    //private ActionBarDrawerToggle drawerToggle;
    private RecyclerView recyclerView;
    private RecyclerViewSeparatorItemDecoration separatorItemDecoration;


    @Override
    public int getPageTitle() {
        return R.string.page_title_sidebar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();
        FragmentSideBarBinding fragmentSideBarBinding;
        fragmentSideBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_bar, container, false);
        fragmentSideBarBinding.setFrag(this);

        sideBarLayout = (SideBar) fragmentSideBarBinding.getRoot().findViewById(R.id.sidebar_layout);
        /*sideBarLayout.addHeaderView(R.layout.sidebar_left_header_view);
        sideBarLayout.addMenuView(R.layout.sidebar_left_menu_view);
        sideBarLayout.addFooterView(R.layout.sidebar_footer_view);*/

        //drawerToggle = setupDrawerToggle();

        separatorItemDecoration = new RecyclerViewSeparatorItemDecoration(context);
        recyclerView = ((RecyclerView) fragmentSideBarBinding.getRoot().findViewById(R.id.sidebar_left_recyclerview));
        recyclerView.addItemDecoration(separatorItemDecoration);
        initializeRecyclerView(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        /*drawerToggle.setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_hamburger_icon, context.getTheme()));
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // event when click home button
                if (navigationController.hasBackStack()) {
                    onBackPressed();
                } else {
                    //showSnackBar();
                    sideBarLayout.openDrawer(GravityCompat.START);
                }
            }
        });*/

        return fragmentSideBarBinding.getRoot();
    }

    public void showSideBar(){
        ((MainActivity)getActivity()).getSideBar().openDrawer(GravityCompat.START);
        //sideBarLayout.openDrawer(GravityCompat.START);
    }

    /*private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, sideBarLayout, navigationController.getToolbar(), R.string.sidebar_open,  R.string.sidebar_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }*/

    private void initializeRecyclerView(Context context) {
        DataHolderView dataHolderView = getIconDataHolderView(context);
        recyclerView.setAdapter(new RecyclerViewAdapter(dataHolderView.dataHolders));
        //findViewById(R.id.uid_recyclerview_header).setVisibility(View.GONE);
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

    class RecyclerViewAdapter extends RecyclerView.Adapter {
        private ObservableArrayList<DataHolder> dataHolders;

        public RecyclerViewAdapter(@NonNull final ObservableArrayList<DataHolder> dataHolders) {
            this.dataHolders = dataHolders;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_one_line_icon, parent, false);
            RecyclerViewAdapter.BindingHolder holder = new RecyclerViewAdapter.BindingHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((RecyclerViewAdapter.BindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((RecyclerViewAdapter.BindingHolder) holder).getBinding().executePendingBindings();

            Resources.Theme theme = ThemeUtils.getTheme(holder.itemView.getContext(), null);
            Context themedContext = UIDContextWrapper.getThemedContext(holder.itemView.getContext(), theme);
            ColorStateList colorStateList = ThemeUtils.buildColorStateList(themedContext, R.color.uid_recyclerview_background_selector);
            final int selectedStateColor = colorStateList.getDefaultColor();

            ((RecyclerViewAdapter.BindingHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    sideBarLayout.closeDrawer(GravityCompat.START);
                    /*boolean isSelected = holder.itemView.isSelected();
                    holder.itemView.setSelected(!isSelected);
                    holder.itemView.setBackgroundColor(isSelected ? Color.TRANSPARENT : selectedStateColor);*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataHolders.size();
        }

        class BindingHolder extends RecyclerView.ViewHolder {
            private ViewDataBinding binding;

            public BindingHolder(@NonNull View rowView) {
                super(rowView);
                binding = DataBindingUtil.bind(rowView);
            }

            public ViewDataBinding getBinding() {
                return binding;
            }
        }
    }
}
