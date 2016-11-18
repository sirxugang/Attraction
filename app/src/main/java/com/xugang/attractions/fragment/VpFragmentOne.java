package com.xugang.attractions.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xugang.attractions.R;
import com.xugang.attractions.modle.WeatherBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2016-10-25.
 */
public class VpFragmentOne extends Fragment {

    private static final String TAG = "test";
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.sdvDay)
    SimpleDraweeView sdvDay;
    @BindView(R.id.sdvNight)
    SimpleDraweeView sdvNight;
    @BindView(R.id.llRootI)
    LinearLayout llRootI;
    @BindView(R.id.tvTianQi)
    TextView tvTianQi;
    @BindView(R.id.tvWenDu)
    TextView tvWenDu;
    @BindView(R.id.tvFengxiang)
    TextView tvFengxiang;
    @BindView(R.id.tvFengLi)
    TextView tvFengLi;
    @BindView(R.id.tvZiWaiXian)
    TextView tvZiWaiXian;
    @BindView(R.id.tvTianQiII)
    TextView tvTianQiII;
    @BindView(R.id.tvWenDuII)
    TextView tvWenDuII;
    @BindView(R.id.tvFengxiangII)
    TextView tvFengxiangII;
    @BindView(R.id.tvFengLiII)
    TextView tvFengLiII;
    @BindView(R.id.llRoot)
    LinearLayout llRoot;
    @BindView(R.id.tvRiChuRiLuo)
    TextView tvRiChuRiLuo;
    @BindView(R.id.tvJiangShui)
    TextView tvJiangShui;
    @BindView(R.id.tvday)
    TextView tvday;
    @BindView(R.id.tvnight)
    TextView tvnight;
    private View view;
    private AnimationDrawable drawable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.vp_one, container, false);
            ButterKnife.bind(this, view);
            EventBus.getDefault().register(this);
        }
        drawable = ((AnimationDrawable) iv.getDrawable());
        drawable.start();
        tvday.setText("今天白天");
        tvnight.setText("今天夜间");
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 90, sticky = true)
    public void setInfo(WeatherBean.ShowapiResBodyBean.F1Bean fone) {
        Log.e(TAG, "setInfo: ");
        sdvDay.setImageURI(Uri.parse(fone.getDay_weather_pic()));
        tvTianQi.setText(fone.getDay_weather());
        tvWenDu.setText(fone.getDay_air_temperature() + "°");
        tvFengxiang.setText(fone.getDay_wind_direction());
        tvFengLi.setText(fone.getDay_wind_power());
        tvZiWaiXian.setText(fone.getZiwaixian());
        tvJiangShui.setText(fone.getJiangshui());

        sdvNight.setImageURI(Uri.parse(fone.getNight_weather_pic()));
        tvTianQiII.setText(fone.getNight_weather());
        tvWenDuII.setText(fone.getNight_air_temperature() + "°");
        tvFengxiangII.setText(fone.getNight_wind_direction());
        tvFengLiII.setText(fone.getNight_wind_power());

        tvRiChuRiLuo.setText(fone.getSun_begin_end());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
