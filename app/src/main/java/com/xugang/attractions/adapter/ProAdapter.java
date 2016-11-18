package com.xugang.attractions.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xugang.attractions.R;
import com.xugang.attractions.modle.ProNameAndIdBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2016-10-20.
 */
public class ProAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Context context;
    private List<ProNameAndIdBean.ShowapiResBodyBean.ListBean.CityBean> pros;

    public ProAdapter(Context context, List<ProNameAndIdBean.ShowapiResBodyBean.ListBean.CityBean> pros) {
        this.context = context;
        this.pros = pros;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (pros.size() == 0)
            return 30;
        return pros.size();
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
            convertView = inflater.inflate(R.layout.item_pro, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (pros.size() != 0) {
            holder.tvProName.setText(pros.get(position).getName());
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ivPro)
        ImageView ivPro;
        @BindView(R.id.tvProName)
        TextView tvProName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
