package com.xugang.attractions.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xugang.attractions.R;
import com.xugang.attractions.modle.HotelBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2016-10-24.
 */
public class HotelAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context context;
    List<HotelBean.ShowapiResBodyBean.ListBean> hotels;

    public HotelAdapter(Context context, List<HotelBean.ShowapiResBodyBean.ListBean> hotels) {
        this.context = context;
        this.hotels = hotels;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (hotels.size() == 0) return 0;
        return hotels.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hotel, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (hotels != null && hotels.size() != 0) {
            HotelBean.ShowapiResBodyBean.ListBean hotel = hotels.get(position);
            holder.sdvHotelPicture.setImageURI(Uri.parse(hotel.getImg()));
            holder.tvHotalName.setText(hotel.getHotelName());
            holder.tvBrowse.setText("浏览次数:" + hotel.getViewCount());
            holder.tvStarLevel.setText(hotel.getStarRatedName());
            holder.tvGoodCount.setText(hotel.getCommentGood());
            holder.tvHotelPrice.setText("￥" + hotel.getLowestPrice());
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.sdvHotelPicture)
        SimpleDraweeView sdvHotelPicture;
        @BindView(R.id.tvHotalName)
        TextView tvHotalName;
        @BindView(R.id.tvBrowse)
        TextView tvBrowse;
        @BindView(R.id.tvStarLevel)
        TextView tvStarLevel;
        @BindView(R.id.tvGoodCount)
        TextView tvGoodCount;
        @BindView(R.id.tvHotelPrice)
        TextView tvHotelPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
