package com.philips.cdp.uikit.cards;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.customviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context mContext;

    public CardAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.uikit_card_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drawable drawable = VectorDrawable.create(mContext, R.drawable.uikit_heart);

        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
        int color = typedArray.getColor(0, -1);
        typedArray.recycle();

        holder.cardImage.setImageDrawable(drawable);
        holder.cardImage.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return 3;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        VectorDrawableImageView cardImage;
        TextView cardtitle;

        public ViewHolder(View itemView) {
            super(itemView);

            cardImage = (VectorDrawableImageView) itemView.findViewById(R.id.cardimage);

        }
    }


}

