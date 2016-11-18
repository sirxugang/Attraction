package com.xugang.attractions;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xugang.attractions.adapter.CitiesAdapter;
import com.xugang.attractions.modle.CityNameAndIdBean;
import com.xugang.attractions.util.Config;
import com.xugang.attractions.util.HttpUtil;
import com.xugang.attractions.util.JsonUtil;
import com.xugang.attractions.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import cn.jpush.android.api.JPushInterface;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class CitiesChoiseActivity extends AppCompatActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.llRoot)
    LinearLayout llRoot;
    @BindView(R.id.lvCities)
    ListView lvCities;
    @BindView(R.id.ptr)
    PtrFrameLayout ptr;
    private List<CityNameAndIdBean.ShowapiResBodyBean.ListBean> cities = new ArrayList<>();
    private String TAG = "testt";
    private CitiesAdapter citiesAdapter;
    private PtrClassicDefaultHeader header;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.MSG_CITY_GOT:
                    String json = (String) msg.obj;
                    CityNameAndIdBean cityNameAndIdBean = JsonUtil.parseCityNameAndIdBean(json);
                    if (cityNameAndIdBean != null) {
                        List<CityNameAndIdBean.ShowapiResBodyBean.ListBean> list = cityNameAndIdBean.getShowapi_res_body().getList();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getCityName() == null) {
                                cities.add(list.get(i));
                            }
                            citiesAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citieschoes);
        ButterKnife.bind(this);

        initData();
        citiesAdapter = new CitiesAdapter(this, cities);
        lvCities.setAdapter(citiesAdapter);
        update();
    }

    private void update() {
        header = new PtrClassicDefaultHeader(this);
        ptr.setHeaderView(header);
        ptr.addPtrUIHandler(header);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData(); //刷新
                ptr.refreshComplete();     //停止刷新
            }
        });
    }

    @OnItemClick(R.id.lvCities)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (cities.size() != 0) {
            Intent intent = new Intent(CitiesChoiseActivity.this, MainActivity.class);
            String str = cities.get(position).getId();
            String s = cities.get(position).getProId();
            if ("25".equals(s))
                str = "25";
            if ("27".equals(s))
                str = "27";
            if ("20".equals(s))
                str = "20";
            if ("3".equals(s))
                str = "3";
            intent.putExtra("id", str);
            intent.putExtra("cityname", cities.get(position).getName());
            startActivity(intent);
            super.onBackPressed();
        }
    }

    private void initData() {
        final String id = getIntent().getStringExtra("id");
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtil.getCitiesListBean(Integer.parseInt(id));
                Message msg = handler.obtainMessage();
                msg.what = Config.MSG_CITY_GOT;
                msg.obj = json;
                handler.sendMessage(msg);
            }
        });
    }

    @OnClick(R.id.tvBack)
    public void onClick(View v) {
        super.onBackPressed();
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

}
