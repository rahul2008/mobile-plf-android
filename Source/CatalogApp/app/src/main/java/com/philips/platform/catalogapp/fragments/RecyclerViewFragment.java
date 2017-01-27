package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.DataHolder;
import com.philips.platform.catalogapp.DataHolderView;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentRecyclerviewBinding;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDUtils;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

public class RecyclerViewFragment extends BaseFragment {

    public static ObservableBoolean isIconLayout = new ObservableBoolean(Boolean.FALSE);
    private FragmentRecyclerviewBinding fragmentRecyclerviewBinding;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final Context context = getContext();

        DataHolderView dataHolderView = isIconLayout.get() ? getIconDataHolderView(context) : getTwoLinesDataHolderView(context);

        fragmentRecyclerviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false);
        fragmentRecyclerviewBinding.setFrag(this);

        fragmentRecyclerviewBinding.recyclerviewRecyclerview.getRecyclerView().addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
        fragmentRecyclerviewBinding.recyclerviewRecyclerview.getRecyclerView().setAdapter(new RecyclerViewAdapter(dataHolderView.dataHolders));
        fragmentRecyclerviewBinding.recyclerviewRecyclerview.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentRecyclerviewBinding.getRoot();
    }

    @NonNull
    private DataHolderView getTwoLinesDataHolderView(Context context) {
        DataHolderView dataHolderView = new DataHolderView();
        dataHolderView.addTwoLineItem(R.string.listview_title_1, R.string.description1, context);
        dataHolderView.addTwoLineItem(R.string.listview_title_2, R.string.description2, context);
        dataHolderView.addTwoLineItem(R.string.listview_title_3, R.string.description3, context);
        dataHolderView.addTwoLineItem(R.string.listview_title_4, R.string.description4, context);
        dataHolderView.addTwoLineItem(R.string.listview_title_5, R.string.description5, context);
        dataHolderView.addTwoLineItem(R.string.listview_title_6, R.string.description6, context);
        return dataHolderView;
    }

    @NonNull
    private DataHolderView getIconDataHolderView(Context context) {
        DataHolderView dataHolderView = new DataHolderView();
        dataHolderView.addIconItem(R.drawable.ic_add_folder, R.string.listview_title_1, context);
        dataHolderView.addIconItem(R.drawable.ic_home, R.string.listview_title_2, context);
        dataHolderView.addIconItem(R.drawable.ic_lock, R.string.listview_title_3, context);
        dataHolderView.addIconItem(R.drawable.ic_alarm, R.string.listview_title_4, context);
        dataHolderView.addIconItem(R.drawable.ic_bottle, R.string.listview_title_5, context);
        dataHolderView.addIconItem(R.drawable.ic_location, R.string.listview_title_6, context);
        return dataHolderView;
    }

    static class RecyclerViewAdapter extends RecyclerView.Adapter {
        private ObservableArrayList<DataHolder> dataHolders;
        private int selectedStateColor = Color.TRANSPARENT;

        public RecyclerViewAdapter(@NonNull final ObservableArrayList<DataHolder> dataHolders) {
            this.dataHolders = dataHolders;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            int layoutId = isIconLayout.get() ? R.layout.recyclerview_one_line_icon : R.layout.recyclerview_two_line_text_item;
            View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            RecyclerViewAdapter.BindingHolder holder = new RecyclerViewAdapter.BindingHolder(v);
            selectedStateColor = getSelectedStateColor(parent.getContext());
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((RecyclerViewAdapter.BindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((RecyclerViewAdapter.BindingHolder) holder).getBinding().executePendingBindings();
            ((BindingHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
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

        private int getSelectedStateColor(Context context) {
            ColorStateList colorStateList = ContextCompat.getColorStateList(context, R.color.uid_recyclerview_background_selector);
            return colorStateList.getDefaultColor();
        }

        static class BindingHolder extends RecyclerView.ViewHolder {
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

    @Override
    public int getPageTitle() {
        return R.string.page_title_recyclerview;
    }
}
