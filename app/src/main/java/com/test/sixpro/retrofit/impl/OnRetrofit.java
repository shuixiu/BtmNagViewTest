package com.test.sixpro.retrofit.impl;

import java.io.File;
import java.util.List;
import java.util.Map;


public interface OnRetrofit {

    interface OnQueryMapListener<T> {

        void onMap(Map<String, String> map);

        void onSuccess(T t);

        void onError(Throwable e);
    }


    interface OnDownLoadListener {

        void onSuccess(int terms);

        void onError(Throwable e);

//        void hasDown(String path);
    }

    interface OnUpLoadListener<T> {

        void onFormDataPartMap(Map<String, String> map);

        void onFileList(List<File> fileList);

        void onSuccess(T t);

        void onError(Throwable e);
    }
}
