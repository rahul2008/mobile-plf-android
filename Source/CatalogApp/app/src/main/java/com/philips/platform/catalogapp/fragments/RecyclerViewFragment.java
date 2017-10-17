/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.philips.platform.catalogapp.DataHolder;
import com.philips.platform.catalogapp.DataHolderView;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentRecyclerviewBinding;
import com.philips.platform.uid.drawable.SeparatorDrawable;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

public class RecyclerViewFragment extends BaseFragment {

    public ObservableBoolean isSeparatorEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean isHeaderEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean isIconTemplateSelected = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean isRecyclerViewDisabled = new ObservableBoolean(Boolean.FALSE);
    private FragmentRecyclerviewBinding fragmentRecyclerviewBinding;
    private RecyclerView recyclerView;
    private RecyclerViewSeparatorItemDecoration separatorItemDecoration;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final Context context = getContext();

        fragmentRecyclerviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false);
        fragmentRecyclerviewBinding.setFrag(this);

        fragmentRecyclerviewBinding.recyclerviewRecyclerview.findViewById(R.id.uid_recyclerview_header).setVisibility(isHeaderEnabled.get() ? View.VISIBLE : View.GONE);

        SeparatorDrawable separatorDrawable = new SeparatorDrawable(context);
        fragmentRecyclerviewBinding.getRoot().findViewById(R.id.divider).setBackground(separatorDrawable);

        separatorItemDecoration = new RecyclerViewSeparatorItemDecoration(getContext());
        recyclerView = ((RecyclerView) fragmentRecyclerviewBinding.recyclerviewRecyclerview.findViewById(R.id.uid_recyclerview_recyclerview));

        enableRecyclerViewSeparator(separatorItemDecoration, recyclerView);
        initializeRecyclerView(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ((Label)fragmentRecyclerviewBinding.recyclerviewRecyclerview.findViewById(R.id.uid_recyclerview_header)).setText(R.string.recyclerview_the_header);

        return fragmentRecyclerviewBinding.getRoot();
    }

    private void initializeRecyclerView(Context context) {
        DataHolderView dataHolderView = isIconTemplateSelected.get() ? getIconDataHolderView(context) : getTwoLinesDataHolderView(context);
        recyclerView.setAdapter(new RecyclerViewAdapter(dataHolderView.dataHolders));
    }

    private void enableRecyclerViewSeparator(RecyclerViewSeparatorItemDecoration separatorItemDecoration, RecyclerView recyclerView) {
        if (isSeparatorEnabled.get()) {
            recyclerView.addItemDecoration(separatorItemDecoration);
        } else {
            recyclerView.removeItemDecoration(separatorItemDecoration);
        }
    }

    @NonNull
    private DataHolderView getTwoLinesDataHolderView(Context context) {
        DataHolderView dataHolderView = new DataHolderView();
        dataHolderView.addTwoLineItem(R.string.title1, R.string.description1, context);
        dataHolderView.addTwoLineItem(R.string.title2, R.string.description2, context);
        dataHolderView.addTwoLineItem(R.string.title3, R.string.description3, context);
        dataHolderView.addTwoLineItem(R.string.title4, R.string.description4, context);
        dataHolderView.addTwoLineItem(R.string.title5, R.string.description5, context);
        dataHolderView.addTwoLineItem(R.string.title6, R.string.description6, context);
        return dataHolderView;
    }

    @NonNull
    private DataHolderView getIconDataHolderView(Context context) {
        DataHolderView dataHolderView = new DataHolderView();
        dataHolderView.addIconItem(R.drawable.ic_add_folder, R.string.title1, context);
        dataHolderView.addIconItem(R.drawable.ic_home, R.string.title2, context);
        dataHolderView.addIconItem(R.drawable.ic_lock, R.string.title3, context);
        dataHolderView.addIconItem(R.drawable.ic_alarm, R.string.title4, context);
        dataHolderView.addIconItem(R.drawable.ic_bottle, R.string.title5, context);
        dataHolderView.addIconItem(R.drawable.ic_location, R.string.title6, context);
        return dataHolderView;
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_recyclerview;
    }

    public void setHeaderEnabled(boolean isheaderEnabled) {
        this.isHeaderEnabled.set(isheaderEnabled);
        fragmentRecyclerviewBinding.recyclerviewRecyclerview.findViewById(R.id.uid_recyclerview_header).setVisibility(isHeaderEnabled.get() ? View.VISIBLE : View.GONE);
        fragmentRecyclerviewBinding.divider.findViewById(R.id.divider).setVisibility(isHeaderEnabled.get() ? View.INVISIBLE : View.VISIBLE);
    }

    public void setSeparatorEnabled(boolean isSeparatorEnabled) {
        this.isSeparatorEnabled.set(isSeparatorEnabled);
        enableRecyclerViewSeparator(separatorItemDecoration, recyclerView);
        recyclerView.invalidate();
    }

    public void setRecyclerViewEnabled(boolean isRecyclerViewEnabled) {
        this.isRecyclerViewDisabled.set(isRecyclerViewEnabled);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void setIsIconTemplateSelected(boolean isIconTemplateSelected) {
        this.isIconTemplateSelected.set(isIconTemplateSelected);
        initializeRecyclerView(getContext());
        recyclerView.invalidate();
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter {
        private ObservableArrayList<DataHolder> dataHolders;

        public RecyclerViewAdapter(@NonNull final ObservableArrayList<DataHolder> dataHolders) {
            this.dataHolders = dataHolders;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            int layoutId = isIconTemplateSelected.get() ? R.layout.recyclerview_one_line_icon : R.layout.recyclerview_two_line_text_item;
            View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            RecyclerViewAdapter.BindingHolder holder = new RecyclerViewAdapter.BindingHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((RecyclerViewAdapter.BindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((RecyclerViewAdapter.BindingHolder) holder).getBinding().executePendingBindings();

            if(isRecyclerViewDisabled.get()){
                holder.itemView.setEnabled(false);
                holder.itemView.setSelected(false);
                holder.itemView.setAlpha(Float.parseFloat(getContext().getResources().getString(R.string.listview_disabled_item_opacity)));
            } else {
                holder.itemView.setEnabled(true);
                holder.itemView.setAlpha(Float.parseFloat(getContext().getResources().getString(R.string.listview_enabled_item_opacity)));
            }

            ((BindingHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    boolean isSelected = holder.itemView.isSelected();
                    holder.itemView.setSelected(!isSelected);
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
