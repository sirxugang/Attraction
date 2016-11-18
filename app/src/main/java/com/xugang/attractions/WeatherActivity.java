package com.xugang.attractions;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xugang.attractions.adapter.VpAdapter;
import com.xugang.attractions.fragment.VpFragmentOne;
import com.xugang.attractions.fragment.VpFragmentThree;
import com.xugang.attractions.fragment.VpFragmentTwo;
import com.xugang.attractions.modle.WeatherBean;
import com.xugang.attractions.util.Config;
import com.xugang.attractions.util.HttpUtil;
import com.xugang.attractions.util.JsonUtil;
import com.xugang.attractions.util.ThreadUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import cn.jpush.android.api.JPushInterface;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ivApp)
    ImageView ivApp;
    @BindView(R.id.rbOne)
    RadioButton rbOne;
    @BindView(R.id.rbTwo)
    RadioButton rbTwo;
    @BindView(R.id.rbThree)
    RadioButton rbThree;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.sdvWeatherPicture)
    SimpleDraweeView sdvWeatherPicture;
    @BindView(R.id.tvWendu)
    TextView tvWendu;
    @BindView(R.id.tvTianQi)
    TextView tvTianQi;
    private VpFragmentOne vpFragmentOne;
    private VpFragmentTwo vpFragmentTwo;
    private VpFragmentThree vpFragmentThree;
    List<Fragment> fragments = new ArrayList<>();
    private VpAdapter vpAdapter;
    private String attrid;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Config.MSG_WEATHER_GOT) {
                String json = (String) msg.obj;
                WeatherBean weatherBean = JsonUtil.parseWeatherBean(json);
                if (weatherBean != null) {
                    WeatherBean.ShowapiResBodyBean.NowBean nowWeather = weatherBean.getShowapi_res_body().getNow();
                    WeatherBean.ShowapiResBodyBean.F1Bean fone = weatherBean.getShowapi_res_body().getF1();
                    EventBus.getDefault().postSticky(fone);

                    WeatherBean.ShowapiResBodyBean.F2Bean ftwo = weatherBean.getShowapi_res_body().getF2();
                    EventBus.getDefault().postSticky(ftwo);

                    WeatherBean.ShowapiResBodyBean.F3Bean fthree = weatherBean.getShowapi_res_body().getF3();
                    EventBus.getDefault().postSticky(fthree);

                    sdvWeatherPicture.setImageURI(Uri.parse(nowWeather.getWeather_pic()));
                    tvWendu.setText(nowWeather.getTemperature());
                    tvTianQi.setText(nowWeather.getWeather());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        attrid = getIntent().getStringExtra("attrid");
        if ("3660".equals(attrid)) attrid = "809";
        initFragment();

        initData();

    }

    private void initData() {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtil.getWeatherBean(attrid);
                Message msg = handler.obtainMessage();
                msg.what = Config.MSG_WEATHER_GOT;
                msg.obj = json;
                handler.sendMessage(msg);
            }
        });
    }

    private void initFragment() {
        vpFragmentOne = new VpFragmentOne();
        vpFragmentTwo = new VpFragmentTwo();
        vpFragmentThree = new VpFragmentThree();
        fragments.add(vpFragmentOne);
        fragments.add(vpFragmentTwo);
        fragments.add(vpFragmentThree);
        vpAdapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(vpAdapter);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbOne:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.rbTwo:
                        vp.setCurrentItem(1);
                        break;
                    case R.id.rbThree:
                        vp.setCurrentItem(2);
                        break;
                }
            }
        });
    }


    @OnPageChange(R.id.vp)
    public void onPageSelected(int position) {
        for (int i = 0; i < rg.getChildCount(); i++) {
            RadioButton button = (RadioButton) rg.getChildAt(i);
            if (i == position) {
                button.setChecked(true);
            } else {
                button.setChecked(false);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @OnClick(R.id.tvBack)
    public void onClick(View v) {
        super.onBackPressed();
    }
}
