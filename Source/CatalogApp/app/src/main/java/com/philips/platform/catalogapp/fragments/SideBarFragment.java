package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.DataHolder;
import com.philips.platform.catalogapp.DataHolderView;
import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.NavigationController;
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

    public SideBar sideBar;
    private RecyclerView recyclerView;
    private RecyclerViewSeparatorItemDecoration separatorItemDecoration;
    //SidebarViewBinding sidebarViewBinding;
    FragmentSideBarBinding fragmentSideBarBinding;


    ActionBarDrawerToggle drawerToggle;

    @Override
    public int getPageTitle() {
        return R.string.page_title_sidebar;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*SidebarViewBinding sidebarViewBinding;
        FragmentSideBarBinding fragmentSideBarBinding;*/

        final Context context = getContext();

        /*sidebarViewBinding = DataBindingUtil.inflate(inflater, R.layout.sidebar_header_view, container, false);
        sidebarViewBinding.setFrag(this);

        fragmentSideBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_bar, container, false);
        fragmentSideBarBinding.setFrag(this);

        separatorItemDecoration = new RecyclerViewSeparatorItemDecoration(getContext());
        recyclerView = ((RecyclerView) sidebarViewBinding.sidebarRecyclerview.findViewById(R.id.uid_recyclerview_recyclerview));

        //enableRecyclerViewSeparator(separatorItemDecoration, recyclerView);
        initializeRecyclerView(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sideBar = (SideBar) sidebarViewBinding.sidebarLayout.findViewById(R.id.sidebar_layout);
        sideBar.setDrawerListener(drawerToggle);
        setupDrawerToggle();*/

        return fragmentSideBarBinding.getRoot();
    }

    void setupDrawerToggle(){
        drawerToggle = new android.support.v7.app.ActionBarDrawerToggle(((MainActivity) getActivity()),sideBar,((MainActivity) getActivity()).getNavigationController().getToolbar(),R.string.show_side_bar, R.string.page_title_sidebar);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        drawerToggle.syncState();
    }

    /*@Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        //EventBus.getDefault().post(new OptionMenuClickedEvent(item.toString()));
        switch (item.getItemId()) {
            case android.R.id.home:
                sideBar.openDrawer(GravityCompat.START);
                return true;
        }

        return true;
    }*/

    public boolean showSideBar(){
        sideBar.openDrawer(GravityCompat.START);
        //sideBar.openDrawer(Gravity.LEFT);
        return true;
    }

    private void initializeRecyclerView(Context context) {
        DataHolderView dataHolderView = getIconDataHolderView(context);
        recyclerView.setAdapter(new SideBarFragment.RecyclerViewAdapter(dataHolderView.dataHolders));
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
            SideBarFragment.RecyclerViewAdapter.BindingHolder holder = new SideBarFragment.RecyclerViewAdapter.BindingHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((SideBarFragment.RecyclerViewAdapter.BindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((SideBarFragment.RecyclerViewAdapter.BindingHolder) holder).getBinding().executePendingBindings();

            Resources.Theme theme = ThemeUtils.getTheme(holder.itemView.getContext(), null);
            Context themedContext = UIDContextWrapper.getThemedContext(holder.itemView.getContext(), theme);
            ColorStateList colorStateList = ThemeUtils.buildColorStateList(themedContext, R.color.uid_recyclerview_background_selector);
            final int selectedStateColor = colorStateList.getDefaultColor();

            ((SideBarFragment.RecyclerViewAdapter.BindingHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    boolean isSelected = holder.itemView.isSelected();
                    holder.itemView.setSelected(!isSelected);
                    holder.itemView.setBackgroundColor(isSelected ? Color.TRANSPARENT : selectedStateColor);
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
