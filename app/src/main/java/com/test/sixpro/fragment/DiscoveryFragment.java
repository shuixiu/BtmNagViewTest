package com.test.sixpro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseFragment;

/**
 * Created by Administrator on 2019/2/18.
 */

public class DiscoveryFragment extends BaseFragment {
    private TextView fragment;

    public static DiscoveryFragment newInstance(String title) {
        DiscoveryFragment di = new DiscoveryFragment();
        return di;
    }
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);

        fragment = view.findViewById(R.id.fragment);

        fragment.setText("DiscoveryFragment");


        Log.d("wwn", "DiscoveryFragment1111111");
        return view;
    }

    @Override
    protected void initData() {
        Log.d("wwn", "DiscoveryFragment");
    }
}
