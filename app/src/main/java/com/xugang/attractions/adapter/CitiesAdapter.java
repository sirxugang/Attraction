package com.xugang.attractions.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xugang.attractions.R;
import com.xugang.attractions.modle.CityNameAndIdBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2016-10-20.
 */
public class CitiesAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Context context;
    private List<CityNameAndIdBean.ShowapiResBodyBean.ListBean> cities;

    public CitiesAdapter(Context context, List<CityNameAndIdBean.ShowapiResBodyBean.ListBean> cities) {
        this.context = context;
        this.cities = cities;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (cities.size() == 0)
            return 30;
        return cities.size();
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
            convertView = inflater.inflate(R.layout.item_cities, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (cities.size() != 0) {
            holder.tvCityName.setText(cities.get(position).getName());
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivPro)
        ImageView ivPro;
        @BindView(R.id.tvCityName)
        TextView tvCityName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
