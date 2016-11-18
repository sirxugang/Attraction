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

import com.xugang.attractions.adapter.ProAdapter;
import com.xugang.attractions.modle.ProNameAndIdBean;
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

public class ProChoiseActivity extends AppCompatActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.llRoot)
    LinearLayout llRoot;
    @BindView(R.id.lvpro)
    ListView lvpro;
    @BindView(R.id.ptr)
    PtrFrameLayout ptr;
    private String TAG = "test";
    private List<ProNameAndIdBean.ShowapiResBodyBean.ListBean.CityBean> pros = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.MSG_PRO_GOT:
                    String json = (String) msg.obj;
                    ProNameAndIdBean proNameAndIdBean = JsonUtil.parseProNameAndIdBean(json);
                    if (proNameAndIdBean != null) {
                        pros.addAll(proNameAndIdBean.getShowapi_res_body().getList().getList());
                        for (int i = 0; i < pros.size(); i++) {
                            if ("香港".equals(pros.get(i).getName()) || "澳门".equals(pros.get(i).getName()))
                                pros.remove(i);
                        }
                        proAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    private ProAdapter proAdapter;
    private PtrClassicDefaultHeader header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prochoes);
        ButterKnife.bind(this);
        initData();
        proAdapter = new ProAdapter(this, pros);
        lvpro.setAdapter(proAdapter);
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

    @OnItemClick(R.id.lvpro)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (pros.size() != 0) {
            Intent intent = new Intent(ProChoiseActivity.this, CitiesChoiseActivity.class);
            intent.putExtra("id", pros.get(position).getId());
            startActivity(intent);

        }
    }

    @OnClick(R.id.tvBack)
    public void onClick(View v) {
        super.onBackPressed();
    }

    private void initData() {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                String json = HttpUtil.getProvinceBean();
                Message msg = handler.obtainMessage();
                msg.what = Config.MSG_PRO_GOT;
                msg.obj = json;
                handler.sendMessage(msg);
            }
        });
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
