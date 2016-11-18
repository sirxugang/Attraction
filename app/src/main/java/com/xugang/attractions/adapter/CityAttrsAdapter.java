package com.xugang.attractions.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xugang.attractions.R;
import com.xugang.attractions.modle.CityAttractions;
import com.xugang.attractions.myinterface.OnRCVItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2016-10-18.
 */
public class CityAttrsAdapter extends RecyclerView.Adapter<CityAttrsAdapter.ViewHolder> {
    List<CityAttractions.ShowapiResBodyBean.PagebeanBean.ContentlistBean> attrs;
    private static final String TAG = "test";
    private int currentPosition = -1;
    Context context;
    private int layoutManager;
    public static final int LAYOUT_MANAGER_LINEAR = 0;
    public static final int LAYOUT_MANAGER_GRID = 1;
    private OnRCVItemClickListener listener;

    public void setListener(OnRCVItemClickListener listener) {
        this.listener = listener;
    }

    public CityAttrsAdapter(Context context, List<CityAttractions.ShowapiResBodyBean.PagebeanBean.ContentlistBean> attrs, int layoutManager) {
        this.attrs = attrs;
        this.context = context;
        this.layoutManager = layoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (layoutManager == LAYOUT_MANAGER_LINEAR) {
            view = LayoutInflater.from(context).inflate(R.layout.item_main_city_attrs_linearlayout, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_main_city_attrs_gridlayout, parent, false);
        }

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (attrs.size() != 0) {
            String name = attrs.get(holder.getLayoutPosition()).getName();
            if (name != null) {
                holder.tvAttrName.setText(attrs.get(position).getName());
            }

            holder.tvAttrInfo.setText(attrs.get(position).getSummary());
            String price = attrs.get(position).getPrice();
            if (price != null) {
                if (layoutManager == LAYOUT_MANAGER_LINEAR) {
                    holder.tvAttrPrice.setText("￥" + price + "\n元起");
                } else {
                    holder.tvAttrPrice.setText("￥" + price + "元起");
                }
            } else {
                holder.tvAttrPrice.setText("");
            }
            List<CityAttractions.ShowapiResBodyBean.PagebeanBean.ContentlistBean.PicListBean> picList = attrs.get(position).getPicList();
            if (picList.size() != 0)
                holder.sdvAttrPicture.setImageURI(Uri.parse(picList.get(0).getPicUrlSmall()));

            holder.cvRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onRCVItemClick(attrs.get(position).getName());
                }
            });

            if (currentPosition == position) {
                holder.splRoot.openPane();
            } else {
                holder.splRoot.closePane();
            }
            holder.splRoot.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {

                }

                @Override
                public void onPanelOpened(View panel) {
                    currentPosition = position;
                    notifyDataSetChanged();
                }

                @Override
                public void onPanelClosed(View panel) {
                    if (currentPosition == position) {
                        currentPosition = -1;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (attrs.size() == 0)
            return 20;
        return attrs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sdvAttrPicture)
        SimpleDraweeView sdvAttrPicture;
        @BindView(R.id.tvAttrName)
        TextView tvAttrName;
        @BindView(R.id.tvAttrInfo)
        TextView tvAttrInfo;
        @BindView(R.id.tvAttrPrice)
        TextView tvAttrPrice;
        @BindView(R.id.llRoot)
        LinearLayout llRoot;
        @BindView(R.id.splRoot)
        SlidingPaneLayout splRoot;
        @BindView(R.id.cvRoot)
        CardView cvRoot;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
