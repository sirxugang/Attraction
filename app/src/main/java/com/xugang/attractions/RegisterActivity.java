package com.xugang.attractions;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.xugang.attractions.dao.UserDao;
import com.xugang.attractions.modle.User;
import com.xugang.attractions.util.GreenDaoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tilI)
    TextInputLayout tilI;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tilII)
    TextInputLayout tilII;
    @BindView(R.id.cbSure)
    CheckBox cbSure;
    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.btnRegiter)
    Button btnRegiter;
    @BindView(R.id.tv)
    TextView tv;
    private String userName;
    private String UserPassword;
    private boolean userAgreement;
    private UserDao userDao;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        userDao = GreenDaoUtils.getSingleTon().getmDaoSession().getUserDao();
        onClicks();
    }

    private void onClicks() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                if (str.length() >= 6) tvHint.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etName.addTextChangedListener(new TextWatcher() {
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
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("此App的一切解释权归本大人所有,爱用不用,不用拉到.不缺你这个傻不拉唧的,无可救药的.上剑不练练下贱的.金剑不练练淫贱的,2B用户!")
                        .create().show();
            }
        });
    }

    @OnClick(R.id.btnRegiter)
    public void onClick(View v) {
        UserPassword = etPassword.getText().toString();
        userName = etName.getText().toString();
        userAgreement = cbSure.isChecked();
        if (userName.length() == 0) {
            tvHint.setVisibility(View.VISIBLE);
            tvHint.setText("用户名不能为空");
        } else if (!userAgreement) {
            tvHint.setVisibility(View.VISIBLE);
            tvHint.setText("必须同意<用户协议>才可注册");
        } else if (UserPassword.length() < 6) {
            tvHint.setVisibility(View.VISIBLE);
            tvHint.setText("密码长度不能低于6位");
        } else if (userName.length() != 0 && UserPassword.length() >= 6 && userAgreement) {
            long id = userDao.insert(new User(null, userName, UserPassword, null));
            if (id > 0) {
                tvHint.setText("注册成功,3秒后跳转至登录界面 !");
                tvHint.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(RegisterActivity.this, LoadActivity.class));
                    }
                }, 3000);
                super.onBackPressed();
            } else {
                tvHint.setVisibility(View.VISIBLE);
                tvHint.setText("注册失败");
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

}
