package com.xugang.attractions;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xugang.attractions.adapter.HotelAdapter;
import com.xugang.attractions.modle.HotelBean;
import com.xugang.attractions.util.Config;
import com.xugang.attractions.util.HttpUtil;
import com.xugang.attractions.util.JsonUtil;
import com.xugang.attractions.util.ThreadUtil;
import com.xugang.attractions.weight.DoubleDatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class HotelActivity extends AppCompatActivity {

    private static final String TAG = "test";
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvComeDate)
    TextView tvComeDate;
    @BindView(R.id.tvLeaveDate)
    TextView tvLeaveDate;
    @BindView(R.id.tvQuery)
    TextView tvQuery;
    @BindView(R.id.lvHotel)
    ListView lvHotel;
    @BindView(R.id.ivQuery)
    ImageView ivQuery;
    private String cityId;
    private String leaveDate;
    private String comeDate;
    private AnimationDrawable ad;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Config.MSG_HOTEL_GOT) {
                String json = (String) msg.obj;
                HotelBean hotelBean = JsonUtil.parseHotelBean(json);
                Log.e(TAG, "handleMessage: ");
                if (hotelBean != null) {
                    List<HotelBean.ShowapiResBodyBean.ListBean> hotels = hotelBean.getShowapi_res_body().getList();
                    ad.stop();
                    ivQuery.setVisibility(View.GONE);
                    lvHotel.setAdapter(new HotelAdapter(HotelActivity.this, hotels));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        ButterKnife.bind(this);
        cityId = getIntent().getStringExtra("cityId");
        getDate();
    }

    @OnClick(R.id.tvQuery)
    public void onTvQueryClick(View v) {

        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int a = Integer.parseInt(comeDate);
        int b = Integer.parseInt(leaveDate);
        int c = Integer.parseInt(currentDate);

        if (a > b || a < c || b < c) {
            Snackbar.make(v, "您选择的时间有误,请重新选择,谢谢", Snackbar.LENGTH_LONG).show();
        } else if (a <= b && a >= c) {
            //播放动画
            ivQuery.setVisibility(View.VISIBLE);
            ad = ((AnimationDrawable) ivQuery.getDrawable());
            ad.start();

            ThreadUtil.execute(new Runnable() {
                @Override
                public void run() {
                    String json = HttpUtil.getHotelBean(cityId, comeDate, leaveDate);
                    Message msg = handler.obtainMessage();
                    msg.what = Config.MSG_HOTEL_GOT;
                    msg.obj = json;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    private void getDate() {
        tvComeDate.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DoubleDatePickerDialog(HotelActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                          int endDayOfMonth) {
                        comeDate = String.format("%d%d%d", startYear, (startMonthOfYear + 1), startDayOfMonth);
                        tvComeDate.setText(comeDate);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
            }
        });
        tvLeaveDate.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DoubleDatePickerDialog(HotelActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                          int endDayOfMonth) {
                        leaveDate = String.format("%d%d%d", startYear, (startMonthOfYear + 1), startDayOfMonth);
                        tvLeaveDate.setText(leaveDate);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
            }
        });
    }

    @OnClick(R.id.tvBack)
    public void onTvBackClick(View v) {
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
