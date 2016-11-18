package com.xugang.attractions.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xugang.attractions.R;
import com.xugang.attractions.modle.AttractionInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2016-10-24.
 */
public class PictureAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context context;
    List<AttractionInfo.ShowapiResBodyBean.PagebeanBean.ContentlistBean.PicListBean> picList;

    public PictureAdapter(Context context, List<AttractionInfo.ShowapiResBodyBean.PagebeanBean.ContentlistBean.PicListBean> picList) {
        this.context = context;
        this.picList = picList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (picList.size() == 0) return 0;
        return picList.size();
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
            convertView = inflater.inflate(R.layout.item_picture, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (picList.size() != 0) {
            holder.sdvPIcture.setImageURI(Uri.parse(picList.get(position).getPicUrl()));
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.sdvPIcture)
        SimpleDraweeView sdvPIcture;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
