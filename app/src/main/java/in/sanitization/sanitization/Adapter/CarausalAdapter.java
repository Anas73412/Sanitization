package in.sanitization.sanitization.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.sanitization.sanitization.Model.Slider_model;
import in.sanitization.sanitization.R;

import static in.sanitization.sanitization.Config.BaseUrl.IMG_SLIDER_URL;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,June,2020
 */
public class CarausalAdapter extends PagerAdapter {
    ArrayList<Slider_model> list;
    Context context;
    LayoutInflater layoutInflater;

    public CarausalAdapter(ArrayList<Slider_model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.row_caraosal,container,false);
        ImageView img_slider;
        TextView tv_title;
        img_slider=view.findViewById(R.id.img_slider);
        tv_title=view.findViewById(R.id.tv_title);
        Glide.with(context)
                .load(IMG_SLIDER_URL + list.get(position).getSlider_image())
                .placeholder(R.drawable.logo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(img_slider);
        tv_title.setText(""+list.get(position).getSlider_title());
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
