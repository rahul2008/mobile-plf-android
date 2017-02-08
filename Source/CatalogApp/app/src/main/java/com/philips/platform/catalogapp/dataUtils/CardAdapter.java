package com.philips.platform.catalogapp.dataUtils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.philips.platform.catalogapp.R;

import java.util.List;

public class CardAdapter extends BaseAdapter{

    private Context mContext;
    private List<CardData> cardList;

    public CardAdapter(Context context,List<CardData> cardList) {
        this.mContext = context;
        this.cardList = cardList;
    }

    @Override
    public int getCount() {
        if(cardList == null){
            return 0;
        }
        return cardList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CardData cardData = cardList.get(position);


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.uid_gridview_item_two_line, null);
        }

        final TextView title = (TextView) convertView.findViewById(R.id.title);
        final TextView description = (TextView) convertView.findViewById(R.id.description);
        final ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

        Glide.with(mContext).load(cardData.getThumbnail()).into(thumbnail);
        title.setText(cardData.getTitle());
        description.setText(cardData.getDescription());

        return convertView;
    }

}
