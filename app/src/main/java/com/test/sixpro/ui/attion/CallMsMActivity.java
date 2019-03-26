package com.test.sixpro.ui.attion;

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
import android.widget.Toast;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseActivity;
import com.test.sixpro.utils.LogInfo;
import com.test.sixpro.utils.SMSMethod;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CallMsMActivity extends BaseActivity {

    private String salePhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_msm);
        ButterKnife.bind(this);


        initTitleView();
        title_font.setText("通讯");
//        ll_back.setVisibility(View.VISIBLE);
//        ll_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    @OnClick(R.id.bt_msm_laogong)
    void msmLaogong(View view) {

        SMSMethod.getInstance(CallMsMActivity.this).SendMessage("13263201160", "老公看到短信回个电话");
    }
    @OnClick(R.id.bt_msm_laodi)
    void msmLaodi(View view) {
        SMSMethod.getInstance(CallMsMActivity.this).SendMessage("15933151095", "老弟看到短信回个电话");
    }
    @OnClick(R.id.bt_msm_mama)
    void msmLaoma(View view) {
        SMSMethod.getInstance(CallMsMActivity.this).SendMessage("13483485449", "老妈看到短信回个电话");
    }

    @OnClick(R.id.bt_msm_baba)
    void msmLaoba(View view) {
        SMSMethod.getInstance(CallMsMActivity.this).SendMessage("13223175260", "老爸看到短信回个电话");
    }

    @OnClick(R.id.bt_laogong)
    void callLaogong(View vew) {
        callPhone("13263201160");
    }

    @OnClick(R.id.bt_laodi)
    void callLaodi(View vew) {
        callPhone("15933151095");
    }

    @OnClick(R.id.bt_mama)
    void callMama(View vew) {
        callPhone("13483485449");
    }
    @OnClick(R.id.bt_baba)
    void callBaba(View vew) {
        callPhone("13223175260");
    }

    public void callPhone(String phoneNum) {

        salePhone = phoneNum;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(CallMsMActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CallMsMActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 3);
            } else {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum)));
            }
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum)));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        LogInfo.log("wwn", "onRequestPermissionsResult");
        switch (requestCode) {
            case 3:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogInfo.log("wwn", "onRequestPermissionsResult=====3");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + salePhone)));
                } else {
                    Toast.makeText(CallMsMActivity.this, "请授权拨打电话权限", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", CallMsMActivity.this.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
