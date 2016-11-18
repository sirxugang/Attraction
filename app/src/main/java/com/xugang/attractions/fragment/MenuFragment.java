package com.xugang.attractions.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xugang.attractions.LoadActivity;
import com.xugang.attractions.LocationDemo;
import com.xugang.attractions.MyFavoriteActivity;
import com.xugang.attractions.ProChoiseActivity;
import com.xugang.attractions.QRCodeActivity;
import com.xugang.attractions.R;
import com.xugang.attractions.SetActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2016-10-19.
 */
public class MenuFragment extends Fragment {
    @BindView(R.id.cvFavoriteAttr)
    CardView cvFavoriteAttr;
    @BindView(R.id.cvQRCode)
    CardView cvQRCode;
    @BindView(R.id.tvSetting)
    TextView tvSetting;
    @BindView(R.id.cvLocation)
    CardView cvLocation;
    private String name;
    private static final String TAG = "test";
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.cvChoseCity)
    CardView cvChoseCity;
    @BindView(R.id.cvLoad)
    CardView cvLoad;
    @BindView(R.id.tvName)
    TextView tvName;
    private View view;
    SlidingMenu slidingMenu;

    public MenuFragment() {
    }

    public MenuFragment(SlidingMenu slidingMenu) {
        this.slidingMenu = slidingMenu;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragmet_menu_sliding_ii, container, false);
            ButterKnife.bind(this, view);
            EventBus.getDefault().register(this);
        }
        return view;
    }

    @OnClick(R.id.tvMenu)
    public void onTvMenuClick(View v) {
        slidingMenu.toggle();
    }

    @OnClick(R.id.cvLocation)
    public void onTvLocationClick(View v) {
        startActivity(new Intent(getActivity(), LocationDemo.class));
        slidingMenu.toggle();
    }

    @OnClick(R.id.tvSetting)
    public void onTvSettingClick(View v) {
        startActivity(new Intent(getActivity(), SetActivity.class));
        slidingMenu.toggle();
    }

    @OnClick(R.id.cvChoseCity)
    public void onCvChoseCityMenuClick(View v) {
        startActivity(new Intent(getActivity(), ProChoiseActivity.class));
        slidingMenu.toggle();
    }

    @OnClick(R.id.cvLoad)
    public void onCvLoadClick(View v) {
        startActivity(new Intent(getActivity(), LoadActivity.class));
        slidingMenu.toggle();
    }

    @OnClick(R.id.cvFavoriteAttr)
    public void onCvFavoriteAttrClick(View v) {
        startActivity(new Intent(getActivity(), MyFavoriteActivity.class));
        slidingMenu.toggle();
    }

    @OnClick(R.id.cvQRCode)
    public void onCvQRCodeClick(View v) {
        Intent intent = new Intent(getActivity(), QRCodeActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
        slidingMenu.toggle();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100, sticky = true)
    public void setName(String name) {
        this.name = name;
        tvName.setText(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
