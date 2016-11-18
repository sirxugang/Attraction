package com.xugang.attractions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends AppCompatActivity {

    private static final String TAG = "testt";
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.rbNomal)
    RadioButton rbNomal;
    @BindView(R.id.rbWeixing)
    RadioButton rbWeixing;
    @BindView(R.id.rb)
    RadioGroup rb;
    @BindView(R.id.cbJiaotong)
    CheckBox cbJiaotong;
    @BindView(R.id.cbReli)
    CheckBox cbReli;
    private BaiduMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        float latf = getIntent().getFloatExtra("latf", -1);
        float lonf = getIntent().getFloatExtra("lonf", -1);

        Log.e(TAG, "onCreate: " + latf + "----" + lonf);

        map = mapView.getMap();

        //定义Maker坐标点
        LatLng point = new LatLng(latf, lonf);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_geo);

//构建MarkerOption，用于在地图上添加Marker
        //设置可拖拽
        OverlayOptions options = new MarkerOptions()
                .position(point)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .period(10)
                .draggable(true);  //设置手势拖拽

        //在地图上添加Marker，并显示
        map.addOverlay(options);

        onClicks();
    }

    private void onClicks() {
        cbJiaotong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    map.setTrafficEnabled(true);
                } else {
                    map.setTrafficEnabled(false);
                }
            }
        });

        cbReli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    map.setBaiduHeatMapEnabled(true);
                } else {
                    map.setBaiduHeatMapEnabled(false);
                }
            }
        });

        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbNomal:
                        map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.rbWeixing:
                        map.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
