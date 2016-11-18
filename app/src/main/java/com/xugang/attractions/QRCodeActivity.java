package com.xugang.attractions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.xugang.attractions.util.MyBitmapUtil;
import com.xugang.attractions.zxing.activity.CaptureActivity;
import com.xugang.attractions.zxing.encoding.EncodingHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class QRCodeActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("name");

    }

    public void getQRCode(View view) {
        if (name == null) {
            tv.setVisibility(View.VISIBLE);
        } else {
            try {
                tv.setVisibility(View.GONE);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bitmap = MyBitmapUtil.drawLogo(EncodingHandler.createQRCode(name, 300), BitmapFactory.decodeResource(getResources(), R.mipmap.qrcode_loge, opts));
                ivQRCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    public void ScanQRCode(View view) {
        startActivityForResult(new Intent(this, CaptureActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            String result = intent.getExtras().getString("result");
            tv.setText(result);
            tv.setVisibility(View.VISIBLE);
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
