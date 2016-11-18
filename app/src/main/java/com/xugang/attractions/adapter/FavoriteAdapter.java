package com.xugang.attractions.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xugang.attractions.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2016-10-22.
 */
public class FavoriteAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Context context;
    private List<String> attrs;

    public FavoriteAdapter(Context context, List<String> attrs) {
        this.context = context;
        this.attrs = attrs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (attrs.size() == 0) return 0;
        return attrs.size();
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
            convertView = inflater.inflate(R.layout.item_favorite, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (attrs.size() != 0) {
            holder.tvAttrName.setText(attrs.get(position));
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvAttrName)
        TextView tvAttrName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
