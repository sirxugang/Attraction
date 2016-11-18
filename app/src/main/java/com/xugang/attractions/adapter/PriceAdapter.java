package com.xugang.attractions.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xugang.attractions.R;
import com.xugang.attractions.modle.AttractionInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2016-10-24.
 */
public class PriceAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context context;
    List<AttractionInfo.ShowapiResBodyBean.PagebeanBean.ContentlistBean.PriceListBean.EntityListBean> prices;

    public PriceAdapter(Context context, List<AttractionInfo.ShowapiResBodyBean.PagebeanBean.ContentlistBean.PriceListBean.EntityListBean> prices) {
        this.context = context;
        this.prices = prices;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (prices.size() == 0)
            return 0;
        return prices.size();
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
            convertView = inflater.inflate(R.layout.item_price, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (prices.size() != 0) {
            holder.tvName.setText(prices.get(position).getTicketName());
            holder.tvPrice.setText("￥" + prices.get(position).getAmountAdvice());
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvPrice)
        TextView tvPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
