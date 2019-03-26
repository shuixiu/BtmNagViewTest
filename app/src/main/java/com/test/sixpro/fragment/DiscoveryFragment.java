package com.test.sixpro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseFragment;
import com.test.sixpro.ui.discovery.RetrofitMActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/2/18.
 */

public class DiscoveryFragment extends BaseFragment {
    private TextView fragment;
    private View view;
    public static DiscoveryFragment newInstance(String title) {
        DiscoveryFragment di = new DiscoveryFragment();
        return di;
    }
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discovery, null);

        ButterKnife.bind(this,view);

        fragment = view.findViewById(R.id.fragment);
        fragment.setText("DiscoveryFragment");
        Log.d("wwn", "DiscoveryFragment1111111");

        initTitleView();
        return view;
    }
    private void initTitleView() {

        View view_title = view.findViewById(R.id.in_title);
        TextView title_font = view_title.findViewById(R.id.title_font);
        title_font.setText("发现");
    }

    @OnClick(R.id.bt_retrofit)
    void toRetrofit(View view){

        startActivity(new Intent(getActivity(),RetrofitMActivity.class));
    }


    @Override
    protected void initData() {
        Log.d("wwn", "DiscoveryFragment");
    }
}
