package com.xugang.attractions.alipaydemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xugang.attractions.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExternalFragment extends Fragment {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.pay)
    Button pay;
    @BindView(R.id.h5pay)
    Button h5pay;
    private String name;
    private String price;
    private View view;
    private String summary;

    public ExternalFragment(String name, String price, String summary) {
        this.name = name;
        this.price = price;
        this.summary = summary;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.pay_external, container, false);
            ButterKnife.bind(this, view);
        }

        tvName.setText(name);
        tvPrice.setText(price);
        tvInfo.setText(summary);

        return view;
    }
}
