package com.xugang.attractions;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xugang.attractions.adapter.CityAttrsAdapter;
import com.xugang.attractions.fragment.MenuFragment;
import com.xugang.attractions.modle.CityAttractions;
import com.xugang.attractions.modle.CityBean;
import com.xugang.attractions.myinterface.OnRCVItemClickListener;
import com.xugang.attractions.util.Config;
import com.xugang.attractions.util.HttpUtil;
import com.xugang.attractions.util.JsonUtil;
import com.xugang.attractions.util.ThreadUtil;
import com.xugang.attractions.weight.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity implements OnRCVItemClickListener {

    private static final String TAG = "test";
    private int currentPage = 1;
    private int currentCityId = 80;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.rvAttrs)
    MyRecyclerView rvAttrs;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cl)
    CoordinatorLayout cl;
    private List<CityAttractions.ShowapiResBodyBean.PagebeanBean.ContentlistBean> attrs = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private CityAttrsAdapter linearAttrsAdapter, gridAttrsAdapter, currentAdapter;
    private List<CityBean> citys = new ArrayList<>();
    private SlidingMenu slidingMenu;
    private MenuFragment menuFragment;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.MSG_ATTRACTIONS_CITYID_GOT:
                    String jsonAttr = (String) msg.obj;
                    CityAttractions cityAttractions = JsonUtil.parseCityAttractionsBean(jsonAttr);
                    if (cityAttractions != null) {
                        List<CityAttractions.ShowapiResBodyBean.PagebeanBean.ContentlistBean> contentlist = cityAttractions.getShowapi_res_body().getPagebean().getContentlist();
                        if (Integer.parseInt(contentlist.get(0).getCityId()) == currentCityId) {
                            if (contentlist.size() != 0) {
                                attrs.addAll(contentlist);
                                currentAdapter.notifyDataSetChanged();
                            }
                        } else {
                            currentCityId = Integer.parseInt(contentlist.get(0).getCityId());
                            attrs.clear();
                            attrs.addAll(contentlist);
                            currentAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case Config.MSG_CITY_GOT_BEIJING:
                    String jsonbeij = (String) msg.obj;
                    CityAttractions beijing = JsonUtil.parseCityAttractionsBean(jsonbeij);
                    if (beijing != null) {
                        List<CityAttractions.ShowapiResBodyBean.PagebeanBean.ContentlistBean> contentlist = beijing.getShowapi_res_body().getPagebean().getContentlist();
                        attrs.addAll(contentlist);
                        currentAdapter.notifyDataSetChanged();
                        currentCityId = 3;
                    }
                    break;
            }
        }
    };
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private long currentTimeMills, lastTimeMills;
    private int criteria = 10;
    private SoundPool soundPool;
    private int boomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initManagerAndAdapter();
        useLinearLayout();
        initSlidingMenu();
        tooBarOnClick();

        initSensor();
        initSoundPool();
    }

    private void initSoundPool() {
        if (Build.VERSION.SDK_INT > 20) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        boomId = soundPool.load(this, R.raw.boom, 1);
    }

    private void initSensor() {
        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {        //数据变化时回调
                currentTimeMills = System.currentTimeMillis();
                if (currentTimeMills - lastTimeMills < 1000) {
                    return;
                }
                lastTimeMills = currentTimeMills;
                float[] data = event.values;

                float accelerationX = data[0];
                float accelerationY = data[1];
//                float accelerationZ = data[2];

                if (accelerationX > criteria || accelerationY > criteria) {
                    soundPool.play(boomId, 1, 1, 0, 0, 1.0f);
                    getJson(currentCityId, currentPage, "");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {            //进度变化时回调
            }
        };
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void tooBarOnClick() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slidingMenu.isShown()) {
                    slidingMenu.toggle();
                } else {
                    slidingMenu.showContent();
                }
//                Intent intent = new Intent(MainActivity.this, ProChoiseActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        menuFragment = new MenuFragment(slidingMenu);
        slidingMenu.setMode(SlidingMenu.LEFT);
        //替换菜单布局为碎片
        slidingMenu.setMenu(R.layout.fragment_menu_sliding);
        getSupportFragmentManager().beginTransaction().replace(R.id.flRoot, menuFragment).commit();
        //阴影和宽度
        slidingMenu.setBehindWidth(320);
        slidingMenu.setShadowWidth(20);

        slidingMenu.setShadowDrawable(R.drawable.shape_shadow_sliding_i);
        slidingMenu.setFadeDegree(0.7f);  //视差
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
    }

    @OnClick(R.id.fab)
    public void onFabClick(View v) {
        if (attrs.size() != 0) {
            if (currentAdapter == linearAttrsAdapter) {
                useGridLayout();
                fab.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.menu_linear));
            } else {
                useLinearLayout();
                fab.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.menu_grid));
            }
        }
    }

    private void useGridLayout() {
        rvAttrs.setAdapter(gridAttrsAdapter);
        rvAttrs.setLayoutManager(gridLayoutManager);
        currentAdapter = gridAttrsAdapter;
    }

    private void useLinearLayout() {
        rvAttrs.setAdapter(linearAttrsAdapter);
        rvAttrs.setLayoutManager(linearLayoutManager);
        currentAdapter = linearAttrsAdapter;
    }

    private void initManagerAndAdapter() {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        linearAttrsAdapter = new CityAttrsAdapter(this, attrs, CityAttrsAdapter.LAYOUT_MANAGER_LINEAR);
        gridAttrsAdapter = new CityAttrsAdapter(this, attrs, CityAttrsAdapter.LAYOUT_MANAGER_GRID);
        linearAttrsAdapter.setListener(this);
        gridAttrsAdapter.setListener(this);
    }

    private void getJson(final int cityid, final int page, final String keyword) {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                String json = null;
                if (cityid == 3) {
                    json = HttpUtil.getAttractions();
                } else {

                    json = HttpUtil.getAttractions(cityid, page, keyword);
                }
                currentPage++;
                if (json != null) {
                    Message msg = handler.obtainMessage();
                    if (cityid == 3) {
                        msg.what = Config.MSG_CITY_GOT_BEIJING;
                    } else {
                        msg.what = Config.MSG_ATTRACTIONS_CITYID_GOT;
                    }
                    msg.obj = json;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String id = getIntent().getStringExtra("id");
        String cityname = getIntent().getStringExtra("cityname");
        if (id == null) {
            getJson(currentCityId, currentPage, "");
        } else {
            currentPage = 1;
            toolBar.setTitle(cityname + "▼旅游");
            getJson(Integer.parseInt(id), currentPage, "");
        }
    }

    @Override
    public void onRCVItemClick(String keyword) {
        Intent intent = new Intent(this, AttrInfoActivity.class);
        if ("虎魄VR(9D体验)".equals(keyword)) keyword = "虎魄VR";
        intent.putExtra("attrname", keyword);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
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
