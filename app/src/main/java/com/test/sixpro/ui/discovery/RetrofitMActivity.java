package com.test.sixpro.ui.discovery;

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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseActivity;
import com.test.sixpro.content.Urls;
import com.test.sixpro.modle.IndutyMustBean;
import com.test.sixpro.modle.LatLng;
import com.test.sixpro.retrofit.WRetrofit;
import com.test.sixpro.retrofit.impl.OnRetrofit;
import com.test.sixpro.utils.LogInfo;
import com.test.sixpro.utils.SMSMethod;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetrofitMActivity extends BaseActivity {


    @BindView(R.id.tv_content)
    TextView mTv_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);


        initTitleView();
        title_font.setText("retrofit");
    }

    @OnClick(R.id.bt_get)
    void reGet(View view) {
        WRetrofit.create().build(Urls.SERVER)
                .isShowDialog(true)
                .doGet(RetrofitMActivity.this, IndutyMustBean.class, Urls.URL_METHOD, new OnRetrofit.OnQueryMapListener<IndutyMustBean>() {
                    @Override
                    public void onMap(Map<String, String> map) {

                    }

                    @Override
                    public void onSuccess(IndutyMustBean latLng) {
                        LogInfo.log("wwn","phoneBean---->" + latLng.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        WRetrofit.create().cancleAll();
    }

    @OnClick(R.id.bt_post)
    void rePost(View view) {

    }

    @OnClick(R.id.bt_file)
    void reFile(View view) {

    }

    @OnClick(R.id.bt_down)
    void reDown(View view) {

    }

}
