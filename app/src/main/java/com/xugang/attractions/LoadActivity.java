package com.xugang.attractions;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xugang.attractions.dao.UserDao;
import com.xugang.attractions.modle.User;
import com.xugang.attractions.util.GreenDaoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

public class LoadActivity extends AppCompatActivity {

    @BindView(R.id.tvZhuxiao)
    TextView tvZhuxiao;
    private boolean ssoLoginSucceed;
    private static final String TAG = "test";
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.tilI)
    TextInputLayout tilI;
    @BindView(R.id.etUserPassword)
    EditText etUserPassword;
    @BindView(R.id.tilII)
    TextInputLayout tilII;
    @BindView(R.id.btnLoad)
    Button btnLoad;
    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.tvNewUser)
    TextView tvNewUser;
    @BindView(R.id.tvQQLoad)
    TextView tvQQLoad;
    private String userName;
    private String userPassword;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        userDao = GreenDaoUtils.getSingleTon().getmDaoSession().getUserDao();
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) tvHint.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.tvNewUser)
    public void onTvNewUserClick(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.tvQQLoad)
    public void onTvQQLoadClick(View v) {
        ssoLogin(ShareSDK.getPlatform(QQ.NAME));
    }

    /**
     * 使用第三方平台登录
     *
     * @param platform QQ、微博、微信等第三方平台
     */
    public void ssoLogin(Platform platform) {
        //如果平台为空
        if (platform == null) {
            return;
        }
        // true使用SSO授权，false不使用SSO授权
        platform.SSOSetting(true);

        if (platform.isAuthValid()) {
            //如果用户已经授权使用该平台账号登陆，则拿取用户信息进行显示
            String userId = platform.getDb().getUserId();
            if (userId != null) {
                String userName = platform.getDb().getUserName();
//                ((TextView) findViewById(R.id.tvName)).setText("你好，" + userName);
//                    login(userId,platform.getName());//相当于在自家服务器启动登录
                ssoLoginSucceed = true;
//              loginOnOurServer(userId,userName);//自家服务器的登录程序
                return;
            }
        } else {
            //如果用户未授权，则引导用户进行授权
            platform.authorize();
            platform.showUser(null);//获取用户信息
        }

        //用户授权成功与否的回调
        platform.setPlatformActionListener(new PlatformActionListener() {
            /**
             * 授权完成时的回调
             * @param platform 进行授权的平台
             * @param i 代表“Action”的类型，8=平台授权 1=
             * @param hashMap 操作成功返回的具体数据
             */
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e(TAG, "onComplete: " + platform.getName() + ",i=" + i + "map=" + hashMap);
                if (i == 8 && !ssoLoginSucceed) {
                    ssoLogin(platform);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "onError() called with: " + "platform = [" + platform + "], i = [" + i + "], throwable = [" + throwable + "]");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d(TAG, "onCancel() called with: " + "platform = [" + platform + "], i = [" + i + "]");
            }
        });
    }


    @OnClick(R.id.btnLoad)
    public void onBtnLoadClick(View v) {
        userName = etUserName.getText().toString();
        userPassword = etUserPassword.getText().toString();
        if (userName.length() == 0) {
            tvHint.setText("用户名不能为空");
            tvHint.setVisibility(View.VISIBLE);
        } else if (userPassword.length() < 6) {
            tvHint.setText("请正确输入密码");
            tvHint.setVisibility(View.VISIBLE);
        } else if (userPassword.length() >= 6 && userName.length() != 0) {
            List<User> list = userDao.queryBuilder()
                    .where(UserDao.Properties.Name.eq(userName))
                    .build().list();
            if (list != null && list.size() != 0) {
                String password = list.get(0).getPassword();
                if (password != null && userPassword.equals(password)) {
                    EventBus.getDefault().postSticky(userName);
                    startActivity(new Intent(this, MainActivity.class));
                    super.onBackPressed();
                }
            } else {
                tvHint.setText("请正确输入账号或者密码");
                tvHint.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.tvZhuxiao)
    public void onTvZhuxiaoClick() {
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        if (!platform.isAuthValid()) {
            Toast.makeText(this, "未发现授权", Toast.LENGTH_SHORT).show();
            ((TextView) findViewById(R.id.tvName)).setText("第三方登录信息");
            return;
        }

        platform.removeAccount(true);//移除授权
        ((TextView) findViewById(R.id.tvName)).setText("第三方登录信息");

        /**
         * 移除授权时并不会回调
         */
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e(TAG, "onComplete: " + platform.getName() + ",i=" + i + "map=" + hashMap);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "onError() called with: " + "platform = [" + platform + "], i = [" + i + "], throwable = [" + throwable + "]");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d(TAG, "onCancel() called with: " + "platform = [" + platform + "], i = [" + i + "]");
            }
        });
    }
}
