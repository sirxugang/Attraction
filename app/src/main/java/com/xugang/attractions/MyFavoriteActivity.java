package com.xugang.attractions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xugang.attractions.adapter.FavoriteAdapter;
import com.xugang.attractions.dao.UserDao;
import com.xugang.attractions.modle.User;
import com.xugang.attractions.util.GreenDaoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cn.jpush.android.api.JPushInterface;

public class MyFavoriteActivity extends AppCompatActivity {

    @BindView(R.id.lvMyFavorite)
    ListView lvMyFavorite;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tvLoad)
    TextView tvLoad;
    private String userName;
    private UserDao userDao;
    private List<String> attrs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        ButterKnife.bind(this);
        userDao = GreenDaoUtils.getSingleTon().getmDaoSession().getUserDao();
        EventBus.getDefault().register(this);
        if (userName != null) {
            tvLoad.setVisibility(View.GONE);
            List<User> list = userDao.queryBuilder()
                    .where(UserDao.Properties.Name.eq(userName))
                    .build().list();
            for (int i = 0; i < list.size(); i++) {
                User user = list.get(i);
                String attrname = user.getAttrname();
                if (attrname != null) {
                    attrs.add(attrname);
                }
            }
            lvMyFavorite.setAdapter(new FavoriteAdapter(this, attrs));
        } else {
            tvLoad.setVisibility(View.VISIBLE);
            tvLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MyFavoriteActivity.this, LoadActivity.class));
                }
            });
        }
    }

    @OnItemClick(R.id.lvMyFavorite)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AttrInfoActivity.class);
        intent.putExtra("attrname", attrs.get(position));
        startActivity(intent);
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 80, sticky = true)
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
