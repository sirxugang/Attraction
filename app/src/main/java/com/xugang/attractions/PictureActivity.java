package com.xugang.attractions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.xugang.attractions.adapter.PictureAdapter;
import com.xugang.attractions.modle.AttractionInfo;
import com.xugang.attractions.util.Config;
import com.xugang.attractions.util.HttpUtil;
import com.xugang.attractions.util.JsonUtil;
import com.xugang.attractions.util.ThreadUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class PictureActivity extends AppCompatActivity {

    private static final String TAG = "test";
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.rgPicture)
    GridView rgPicture;
    private String attrName;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Config.MSG_PICTURE_GOT) {
                String json = (String) msg.obj;
                AttractionInfo attractionInfo = JsonUtil.parseAttractionInfo(json);
                if (attractionInfo != null) {
                    List<AttractionInfo.ShowapiResBodyBean.PagebeanBean.ContentlistBean.PicListBean> picList = attractionInfo.getShowapi_res_body().getPagebean().getContentlist().get(0).getPicList();
                    rgPicture.setAdapter(new PictureAdapter(PictureActivity.this, picList));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        attrName = getIntent().getStringExtra("attrName");

        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtil.getAttractionsByName(attrName);
                Message msg = handler.obtainMessage();
                msg.what = Config.MSG_PICTURE_GOT;
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
