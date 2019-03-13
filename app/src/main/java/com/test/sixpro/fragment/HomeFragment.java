package com.test.sixpro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseFragment;
import com.test.sixpro.fragment.home.RxRetrofitActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/2/18.
 */

public class HomeFragment extends BaseFragment {
    private TextView fragment;
    private Button bt_rx;
    public static HomeFragment newInstance(String title) {
        HomeFragment ho = new HomeFragment();
        return ho;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);
        ButterKnife.bind(this, view);
        fragment = view.findViewById(R.id.fragment);
        fragment.setText("HomeFragment");

        bt_rx = view.findViewById(R.id.bt_rx);
        Log.d("wwn", "HomeFragment1111111");
        return view;
    }

    @Override
    protected void initData() {
        Log.d("wwn", "HomeFragment");
        bt_rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,RxRetrofitActivity.class));
            }
        });

    }
}
