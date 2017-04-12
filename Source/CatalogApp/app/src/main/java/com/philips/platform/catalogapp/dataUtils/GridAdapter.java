package com.philips.platform.catalogapp.dataUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.philips.platform.catalogapp.R;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<GridData> {

    private Context mContext;
    private ArrayList<GridData> cardList;
    GridDataHelper gridDataHelper;
    boolean isdisabled, isSecondary;
    int templateSelection;

    public GridAdapter(Context context, ArrayList<GridData> cardList) {
        super(context, 0, cardList);
        this.mContext = context;
        this.cardList = new ArrayList<>(cardList);

        gridDataHelper = new GridDataHelper(mContext);
        isdisabled = gridDataHelper.isSetDisableStateEnabled();
        isSecondary = gridDataHelper.isSecondaryActionEnabled();
        templateSelection = gridDataHelper.getTemplateSelection();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return !isdisabled;
    }

    @Override
    public int getCount() {
        if (cardList == null) {
            return 0;
        }
        return cardList.size();
    }

    @Override
    public GridData getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridData gridData = cardList.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            switch (templateSelection) {
                case 2:
                    convertView = layoutInflater.inflate(R.layout.uid_gridview_item_solid_icon, null);
                    break;
                case 3:
                    convertView = layoutInflater.inflate(R.layout.uid_gridview_item_gradient_icon, null);
                    break;
                default:
                    convertView = layoutInflater.inflate(R.layout.uid_gridview_item_plain_icon, null);
            }

            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.uid_gridview_thumbnail);
            Glide.with(mContext).load(gridData.getThumbnail()).into(thumbnail);

            Label title = (Label) convertView.findViewById(R.id.uid_gridview_title);
            if (title != null) {
                title.setText(gridData.getTitle());
            }

            Label description = (Label) convertView.findViewById(R.id.uid_gridview_description);
            if (description != null) {
                description.setText(gridData.getDescription());
            }

            ImageButton star_icon = new ImageButton(mContext);

            if (isSecondary) {
                star_icon = (ImageButton) convertView.findViewById(R.id.uid_gridview_icon);
                int icon_drawable = gridData.isFavorite() ? R.drawable.star_filled : R.drawable.star_outlined;

                Drawable drawable = VectorDrawableCompat.create(mContext.getResources(), icon_drawable, mContext.getTheme());
                DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.uidColorWhite));
                star_icon.setImageDrawable(drawable);
            }

            ViewHolder viewHolder = new ViewHolder(title, description, thumbnail, star_icon);
            convertView.setTag(viewHolder);
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            Glide.with(mContext).load(gridData.getThumbnail()).into(viewHolder.thumbnail);

            if (viewHolder.title != null) {
                viewHolder.title.setText(gridData.getTitle());
            }

            if (viewHolder.description != null) {
                viewHolder.description.setText(gridData.getDescription());
            }

            if (isSecondary) {
                int icon_drawable = gridData.isFavorite() ? R.drawable.star_filled : R.drawable.star_outlined;
                Drawable drawable = VectorDrawableCompat.create(mContext.getResources(), icon_drawable, mContext.getTheme());
                DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.uidColorWhite));
                viewHolder.star_icon.setImageDrawable(drawable);
            }
        }

        if (isdisabled) {
            convertView.setAlpha(Float.parseFloat(mContext.getResources().getString(R.string.gridview_item_opacity)));
        }

        return convertView;
    }

    static class ViewHolder {
        private Label title;
        private Label description;
        private ImageView thumbnail;
        private ImageButton star_icon;

        public ViewHolder(Label title, Label description, ImageView thumbnail, ImageButton star_icon) {
            this.title = title;
            this.description = description;
            this.thumbnail = thumbnail;
            this.star_icon = star_icon;
        }
    }

    public void updateGrid(ArrayList<GridData> newList) {
        this.cardList.clear();
        this.cardList = new ArrayList<>(newList);
        this.notifyDataSetChanged();
    }
}
