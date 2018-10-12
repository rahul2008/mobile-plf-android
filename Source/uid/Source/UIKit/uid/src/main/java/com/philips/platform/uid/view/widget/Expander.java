package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.UIDHelper;

public class Expander extends LinearLayout implements View.OnClickListener {
    private RelativeLayout ExpanderViewTitle;
    private Label ExpanderViewTitleDefaultLabel;


    private ImageView ExpanderViewTitleDefaultImageView;
    private RelativeLayout ExpanderViewContent;
    private Label ExpanderViewContentDefaultLabel;
    private Context mContext;
    private View titleBottomDivider;
    private View contentBottomDivider;


    public Expander(Context context, @Nullable AttributeSet attrs) {
        super(context);
        initializeViews(context, attrs);
    }

    public Expander( @NonNull final Context context){
        this(context,null);

    }
    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context));
        ViewGroup expanderLayout = (ViewGroup) inflater.inflate(R.layout.uid_expander, this);
        ExpanderViewTitle = (RelativeLayout) expanderLayout.findViewById(R.id.uid_expander_view_title);
        ExpanderViewTitle.setOnClickListener(this);
        ExpanderViewContent =(RelativeLayout)expanderLayout.findViewById(R.id.uid_expander_view_content);
        ExpanderViewTitleDefaultLabel = (Label) expanderLayout.findViewById(R.id.uid_expander_title_text);
        ExpanderViewTitleDefaultImageView = (ImageView) expanderLayout.findViewById(R.id.uid_expander_title_image);
        titleBottomDivider = (View) expanderLayout.findViewById(R.id.uid_expander_title_bottom_divider);
        contentBottomDivider = (View) expanderLayout.findViewById(R.id.uid_expander_content_bottom_divider);

    }



     public void setContentLayout(int resource, Context context){
         LayoutInflater layoutInflater = (LayoutInflater)
                 context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         View contentView = layoutInflater.inflate(resource, ExpanderViewContent, false);

         ExpanderViewContent.addView(contentView);
        // ExpanderViewContent.setVisibility(GONE);

    }

    public void setTitle(String title) {
        ExpanderViewTitleDefaultLabel.setText(title);
    }

    public void setTitleImage(Drawable drawable) {
        ExpanderViewTitleDefaultImageView.setImageDrawable(drawable);

    }

    public void setContent(String content) {
        ExpanderViewContentDefaultLabel.setText(content);

    }

    boolean isExpanded(){
        return true;
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.uid_expander_view_title) {
            expandOrCollapse();

        }
    }

    private void expandOrCollapse(){
        if(ExpanderViewContent.isShown()){
            ExpanderViewContent.setVisibility(View.GONE);
            contentBottomDivider.setVisibility(View.GONE);
            titleBottomDivider.setVisibility(VISIBLE);
        }else{
            titleBottomDivider.setVisibility(GONE);
            ExpanderViewContent.setVisibility(View.VISIBLE);
            contentBottomDivider.setVisibility(View.VISIBLE);
        }
    }
}
