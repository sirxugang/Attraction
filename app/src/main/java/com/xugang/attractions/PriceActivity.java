package com.xugang.attractions;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xugang.attractions.adapter.PriceAdapter;
import com.xugang.attractions.alipaydemo.PayDemoActivity;
import com.xugang.attractions.modle.AttractionInfo;
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

public class PriceActivity extends AppCompatActivity {

    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.lvAbultPrice)
    ListView lvAbultPrice;
    private String attrName;
    private String summary;
    List<AttractionInfo.ShowapiResBodyBean.PagebeanBean.ContentlistBean.PriceListBean.EntityListBean> entityList = new ArrayList<>();
    private Handler handler = new Handler() {
        private PriceAdapter priceAdapter;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.MSG_PRICE_GOT:
                    String json = (String) msg.obj;
                    AttractionInfo attractionInfo = JsonUtil.parseAttractionInfo(json);
                    if (attractionInfo != null) {
                        summary = attractionInfo.getShowapi_res_body().getPagebean().getContentlist().get(0).getSummary();
                        entityList = attractionInfo.getShowapi_res_body().getPagebean().getContentlist().get(0).getPriceList().get(0).getEntityList();
                        if (entityList != null && entityList.size() != 0) {
                            priceAdapter = new PriceAdapter(PriceActivity.this, entityList);
                            lvAbultPrice.setAdapter(priceAdapter);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        ButterKnife.bind(this);
        attrName = getIntent().getStringExtra("attrName");

        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtil.getAttractionsByName(attrName);
                Message msg = handler.obtainMessage();
                msg.what = Config.MSG_PRICE_GOT;
                msg.obj = json;
                handler.sendMessage(msg);
            }
        });
    }

    @OnItemClick(R.id.lvAbultPrice)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, PayDemoActivity.class);
        String ticketName = entityList.get(position).getTicketName();
        String amountAdvice = entityList.get(position).getAmountAdvice();
        intent.putExtra("summary",summary);
        intent.putExtra("name",ticketName);
        intent.putExtra("price",amountAdvice);
        startActivity(intent);
    }

    @OnClick(R.id.tvBack)
    public void onClick(View v) {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_bottom, R.anim.exit_bottom);
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
