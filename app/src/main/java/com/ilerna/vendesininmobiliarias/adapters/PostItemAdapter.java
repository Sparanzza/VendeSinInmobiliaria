package com.ilerna.vendesininmobiliarias.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.models.SlideItemPost;

import java.util.List;

public class PostItemAdapter extends PagerAdapter {

    private Context context;
    private List<SlideItemPost> slideItemPost;

    public PostItemAdapter(Context context, List<SlideItemPost> theSlideItemsModelClassList) {
        this.context = context;
        this.slideItemPost = theSlideItemsModelClassList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.details_post_slider, null);

        TextView caption_title = view.findViewById(R.id.sliderTitle);

        new Utils.ImageDownloadTasK((ImageView) view.findViewById(R.id.imageSlider)).execute(slideItemPost.get(position).getImage());
        caption_title.setText(slideItemPost.get(position).getTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return slideItemPost.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
