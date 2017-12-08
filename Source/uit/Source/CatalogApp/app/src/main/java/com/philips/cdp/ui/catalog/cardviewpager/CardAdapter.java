package com.philips.cdp.ui.catalog.cardviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
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
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_verydarkBaseColor});
        int color = typedArray.getColor(0, -1);
        typedArray.recycle();

        holder.cardImage.setImageDrawable(VectorDrawable.create(mContext, R.drawable.uikit_heart_icon));
        holder.crossIcon.setImageDrawable(VectorDrawable.create(mContext, R.drawable.uikit_cross_icon));
        holder.crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " Clicked Close Button", Toast.LENGTH_SHORT).show();
            }
        });
        holder.linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " Clicked Link", Toast.LENGTH_SHORT).show();
            }
        });
        holder.cardImage.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        if (position == 1) {
            holder.topLayout.setBackgroundResource(R.drawable.uikit_food);
        }
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

        ImageView cardImage, crossIcon;
        TextView linkText;
        RelativeLayout topLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.cardImage);
            crossIcon = (ImageView) itemView.findViewById(R.id.cross);
            linkText = (TextView) itemView.findViewById(R.id.uikit_cards_link_text);
            topLayout = (RelativeLayout) itemView.findViewById(R.id.top_layout);

        }
    }


}

