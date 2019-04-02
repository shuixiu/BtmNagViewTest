package com.test.sixpro.ui.discovery;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.test.sixpro.R;
import com.test.sixpro.base.BaseIContentActivity;
import com.test.sixpro.bean.IndutyMustBean;
import com.test.sixpro.ui.attion.universal.FuctionManager;
import com.test.sixpro.ui.discovery.persenter.IContentPersenter;
import com.test.sixpro.ui.discovery.view.IContentView;
import com.test.sixpro.utils.LogInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetrofitMActivity extends BaseIContentActivity<IContentView,IContentPersenter<IContentView>> implements IContentView {


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

    /**
     * 选择表示层
     * @return
     */
    @Override
    protected IContentPersenter<IContentView> createPersenter() {
        return new IContentPersenter<>();
    }

    @Override
    public void showErrorView(Throwable e) {
        mTv_content.setText(e.getMessage());
    }

    @Override
    public void showContentView(IndutyMustBean beans) {
        mTv_content.setText(beans.toString());
        LogInfo.log("wwn","per----->"+beans.getData().toString());
    }

    @OnClick(R.id.bt_get)
    void reGet(View view) {
        persenter.fetch();
    }

   @Override
    protected void onDestroy() {
        super.onDestroy();
        LogInfo.log("wwn","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogInfo.log("wwn","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogInfo.log("wwn","onStop");
    }

    @OnClick(R.id.bt_post)
    void rePost(View view) {
        FuctionManager.getInstance().invokeFunction("NoFunctionNoResult");
    }

    @OnClick(R.id.bt_file)
    void reFile(View view) {

    }

    @OnClick(R.id.bt_down)
    void reDown(View view) {

    }


}
