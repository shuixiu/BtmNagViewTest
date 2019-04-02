package com.test.sixpro.ui.discovery.view;

import com.test.sixpro.bean.IndutyMustBean;


public interface IContentView {

    //显示List数据
    void showContentView(IndutyMustBean beans);

    //显示其他数据   显示其他view

    void showErrorView(Throwable e);
}
