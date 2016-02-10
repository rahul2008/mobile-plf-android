package com.philips.hor_productselection_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.philips.hor_productselection_android.Product;
import com.philips.hor_productselection_android.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 310190678 on 20-Jan-16.
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    ArrayList<Product> mList = null;


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mCtnView = null;
        public TextView mCatalogView = null;
        public TextView mCategoryView = null;

        public ViewHolder(View itemView) {
            super(itemView);

            mCtnView = (CheckBox) itemView.findViewById(R.id.ctn_name);
            mCategoryView = (TextView) itemView.findViewById(R.id.catagory_name);
            mCatalogView = (TextView) itemView.findViewById(R.id.catalog_name);
        }
    }


    public SampleAdapter(ArrayList<Product> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final Product product = mList.get(i);
        String ctn = product.getmCtn();
        String category = product.getmCategory();
        String catalog = product.getmCatalog();

        if (ctn != null)
            viewHolder.mCtnView.setText(ctn);
        else
            viewHolder.mCtnView.setText("");

        if (category != null)
            viewHolder.mCategoryView.setText(category);
        else
            viewHolder.mCategoryView.setText("");

        if (catalog != null)
            viewHolder.mCatalogView.setText(catalog);
        else
            viewHolder.mCatalogView.setText("");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
