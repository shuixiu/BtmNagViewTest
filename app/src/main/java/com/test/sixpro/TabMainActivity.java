package com.test.sixpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.test.sixpro.base.BaseActivity;
import com.test.sixpro.modle.TabEntity;
import com.test.sixpro.utils.HomeButtomData;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class TabMainActivity extends BaseActivity {

    private CommonTabLayout mTabLayout;
    private ArrayList<Fragment> mFragmensts;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mFragmensts = HomeButtomData.getListFragments("TabLayout");
        for (int i = 0; i < HomeButtomData.mTabTitle.length; i++) {
            mTabEntities.add(new TabEntity(HomeButtomData.mTabTitle[i], HomeButtomData.mTabRes[i], HomeButtomData.mTabResPressed[i]));
        }
        mTabLayout = (CommonTabLayout) findViewById(R.id.bottom_tab_layout);
        mTabLayout.setTabData(mTabEntities, this, R.id.home_container, mFragmensts);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }
            @Override
            public void onTabReselect(int position) {
            }
        });
        mTabLayout.setCurrentTab(0);
    }


}

