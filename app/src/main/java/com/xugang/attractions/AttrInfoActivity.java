package com.xugang.attractions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xugang.attractions.dao.UserDao;
import com.xugang.attractions.modle.AttractionInfo;
import com.xugang.attractions.modle.User;
import com.xugang.attractions.util.Config;
import com.xugang.attractions.util.GreenDaoUtils;
import com.xugang.attractions.util.HttpUtil;
import com.xugang.attractions.util.JsonUtil;
import com.xugang.attractions.util.ShareUtil;
import com.xugang.attractions.util.ThreadUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class AttrInfoActivity extends AppCompatActivity {
    private static final String TAG = "test";
    @BindView(R.id.cbFavorite)
    CheckBox cbFavorite;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.ivLocation)
    ImageView ivLocation;
    private boolean flagInfo = true;
    private boolean flagAttention = true;
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.sdvAttrPicture)
    SimpleDraweeView sdvAttrPicture;
    @BindView(R.id.tvAttrName)
    TextView tvAttrName;
    @BindView(R.id.tvOpenTime)
    TextView tvOpenTime;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvAttrInfoI)
    TextView tvAttrInfoI;
    @BindView(R.id.tvAttrInfoII)
    TextView tvAttrInfoII;
    @BindView(R.id.tvMore)
    TextView tvMore;
    @BindView(R.id.tvAttentionI)
    TextView tvAttentionI;
    @BindView(R.id.tvAttentionII)
    TextView tvAttentionII;
    @BindView(R.id.tvMoreII)
    TextView tvMoreII;
    @BindView(R.id.tvAttrPrice)
    TextView tvAttrPrice;
    @BindView(R.id.tvAttrPicture)
    TextView tvAttrPicture;
    @BindView(R.id.tvAttrWeather)
    TextView tvAttrWeather;
    @BindView(R.id.tvAttrHotal)
    TextView tvAttrHotal;
    private String userName;
    private String attrName;
    private String cityId;
    private String attrid;
    private String summary;
    private String lon;
    private String lat;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.MSG_ATTRACTION_INFO:
                    String json = (String) msg.obj;
                    AttractionInfo attractionInfo = JsonUtil.parseAttractionInfo(json);
                    if (attractionInfo != null) {
                        AttractionInfo.ShowapiResBodyBean.PagebeanBean.ContentlistBean contentlistBean = attractionInfo.getShowapi_res_body().getPagebean().getContentlist().get(0);
                        lat = contentlistBean.getLocation().getLat();
                        lon = contentlistBean.getLocation().getLon();
                        summary = contentlistBean.getSummary();
                        attrid = contentlistBean.getId();
                        cityId = contentlistBean.getCityId();
                        attrName = contentlistBean.getName();
                        if (attrName != null) tvAttrName.setText(attrName);
                        String opentime = contentlistBean.getOpentime();
                        if (opentime != null) tvOpenTime.setText(opentime);
                        String address = contentlistBean.getAddress();
                        if (address != null) tvLocation.setText(address);
                        String picUrlSmall = contentlistBean.getPicList().get(0).getPicUrl();
                        if (picUrlSmall != null) sdvAttrPicture.setImageURI(Uri.parse(picUrlSmall));
                        String content = contentlistBean.getContent();
                        if (content != null) {
                            tvAttrInfoI.setText(content);
                            tvAttrInfoII.setText(content);
                        }
                        String attention = contentlistBean.getAttention();
                        if (attention != null) {
                            tvAttentionI.setText(attention);
                            tvAttentionII.setText(attention);
                        }
                        if (userName != null) {
                            List<User> list = userDao.queryBuilder()
                                    .where(UserDao.Properties.Name.eq(userName))
                                    .build().list();
                            for (int i = 0; i < list.size(); i++) {
                                User user = list.get(i);
                                if (user.getAttrname().equals(attrName)) {
                                    cbFavorite.setChecked(true);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    };
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attr_info);

        ShareSDK.initSDK(this, Config.APP_KEY_SHARE);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        userDao = GreenDaoUtils.getSingleTon().getmDaoSession().getUserDao();
        getData();

        cbChecked();
    }

    @OnClick(R.id.tvLocation)
    public void onClick(View v) {
        Intent intent = new Intent(this, LocationActivity.class);
        Float latf = Float.valueOf(lat);
        Float lonf = Float.valueOf(lon);
        Log.e(TAG, "onClick: "+latf+"---" +lonf);
        intent.putExtra("latf", latf);
        intent.putExtra("lonf", lonf);
        startActivity(intent);
    }

    private void cbChecked() {
        cbFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = cbFavorite.isChecked();
                if (checked) {
                    if (userName == null) {
                        cbFavorite.setChecked(false);
                        startActivity(new Intent(AttrInfoActivity.this, LoadActivity.class));
                    } else {
                        long result = userDao.insert(new User(null, userName, null, attrName));
                    }
                } else {
                    List<User> list = userDao.queryBuilder()
                            .where(UserDao.Properties.Attrname.eq(attrName))
                            .where(UserDao.Properties.Name.eq(userName))
                            .build().list();
                    userDao.delete(list.get(0));
                }
            }
        });
    }

    @OnClick(R.id.tvMore)
    public void onTvMoreClick(View v) {
        if (flagInfo) {
            tvAttrInfoI.setVisibility(View.GONE);
            tvAttrInfoII.setVisibility(View.VISIBLE);
            tvMore.setText("收起");
            flagInfo = false;
        } else {
            tvAttrInfoI.setVisibility(View.VISIBLE);
            tvAttrInfoII.setVisibility(View.GONE);
            tvMore.setText("查看更多");
            flagInfo = true;
        }
    }

    @OnClick(R.id.tvShare)
    public void onTvShareClick(View v) {
        ShareUtil.showShare(this, attrName, summary);
    }

    @OnClick(R.id.tvAttrPrice)
    public void onTvAttrPriceClick(View view) {
        Intent intent = new Intent(this, PriceActivity.class);
        intent.putExtra("attrName", attrName);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_top, R.anim.exit_top);
    }

    @OnClick(R.id.tvAttrWeather)
    public void onTvAttrWeatherClick(View view) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("attrid", attrid);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_top, R.anim.exit_top);
    }

    @OnClick(R.id.tvAttrHotal)
    public void onTvAttrHotalClick(View view) {
        Intent intent = new Intent(this, HotelActivity.class);
        intent.putExtra("cityId", cityId);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_top, R.anim.exit_top);
    }

    @OnClick(R.id.tvAttrPicture)
    public void onTvAttrPictureClick(View view) {
        Intent intent = new Intent(this, PictureActivity.class);
        intent.putExtra("attrName", attrName);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_top, R.anim.exit_top);
    }

    @OnClick(R.id.tvMoreII)
    public void onTvMoreIIClick(View v) {
        if (flagAttention) {
            tvAttentionI.setVisibility(View.GONE);
            tvAttentionII.setVisibility(View.VISIBLE);
            tvMoreII.setText("收起");
            flagAttention = false;
        } else {
            tvAttentionI.setVisibility(View.VISIBLE);
            tvAttentionII.setVisibility(View.GONE);
            tvMoreII.setText("查看更多");
            flagAttention = true;
        }
    }

    @OnClick(R.id.tvBack)
    public void onTvBackClick(View v) {
        super.onBackPressed();
    }

    private void getData() {
        final String attrname = getIntent().getStringExtra("attrname");
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtil.getAttractionsByName(attrname);
                Message msg = handler.obtainMessage();
                msg.what = Config.MSG_ATTRACTION_INFO;
                msg.obj = json;
                handler.sendMessage(msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 90, sticky = true)
    public void setName(String name) {
        this.userName = name;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
