package com.test.sixpro.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseFragment;
import com.test.sixpro.ui.home.LocationChangeActivity;
import com.test.sixpro.utils.LogInfo;
import com.test.sixpro.utils.SMSMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/2/18.
 */

public class HomeFragment extends BaseFragment {
    private TextView fragment;
    private Button bt_rx;
    private View view;

    private String salePhone = "";
    public static HomeFragment newInstance(String title) {
        HomeFragment ho = new HomeFragment();
        return ho;
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        Log.d("wwn", "HomeFragment1111111");
        initTitleView();
        return view;
    }

    private void initTitleView() {
        View view_title = view.findViewById(R.id.in_title);
        TextView title_font = view_title.findViewById(R.id.title_font);
        title_font.setText("首页");

    }


    @OnClick(R.id.bt_location)
    void toLocationMap(View view){
        startActivity(new Intent(context,LocationChangeActivity.class));
    }
    @Override
    protected void initData() {
        Log.d("wwn", "HomeFragment");

    }
}
