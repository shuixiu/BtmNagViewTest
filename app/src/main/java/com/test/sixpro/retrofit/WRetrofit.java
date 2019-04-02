package com.test.sixpro.retrofit;


import android.app.Activity;
import android.app.Dialog;


import com.test.sixpro.retrofit.impl.ApiService;
import com.test.sixpro.retrofit.impl.OnRetrofit;
import com.test.sixpro.retrofit.observer.MyObserver;
import com.test.sixpro.retrofit.util.DownLoadUtil;
import com.test.sixpro.retrofit.util.HttpManager;
import com.test.sixpro.retrofit.util.UpLoadUtil;
import com.test.sixpro.retrofit.util.WDialogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;


public class WRetrofit {
    private static WRetrofit mInstance;
    private ApiService apiService;

    private WRetrofit() {
    }

    public static WRetrofit create() {
        if (mInstance == null) {
            synchronized (WRetrofit.class) {
                mInstance = new WRetrofit();
            }
        }
        return mInstance;
    }


    public WRetrofit build(String BaseUrl) {
//        WDialogUtil.create().setShow(true); // 默认显示Dialog
        apiService = HttpManager.create().build(BaseUrl).getService(ApiService.class);
        return this;
    }


    //添加请求头
    public WRetrofit addHeard(Map<String, String> heardMap) {
        HttpManager.create().addHeard(heardMap);
        return this;
    }

    public WRetrofit isShowDialog(boolean isShow) {
        WDialogUtil.create().setShow(isShow);
        return this;
    }


    //显示自定义dialog
    public WRetrofit setDialog(Dialog dialog) {
        WDialogUtil.create().setShow(true);
        WDialogUtil.create().setDialog(dialog);
        return this;
    }

    /**
     * @param tClass
     * @param Url      当map  中没有参数的时候 需要上传url 如http://apis.juhe.cn/
     *                 反之 http://apis.juhe.cn/mobile/get?phone=18856907654&key=5778e9d9cf089fc3b093b162036fc0e1
     * @param listener
     * @Description
     * @Return void
     */
    public <T> void doGet(final Class<T> tClass, String Url, final OnRetrofit.OnQueryMapListener<T> listener) {
        HashMap<String, String> map = new HashMap<>();
        listener.onMap(map);
        if (map.size() > 0) {
            String param = HttpManager.create().prepareParam(map);
            if (param.trim().length() >= 1) {
                Url += "?" + param;
            }
        }
        apiService
                .getGetData(Url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().getObserver( tClass, listener));
    }


    /**
     * @param tClass
     * @param UrlPath  如 bother/call/can
     * @param listener
     * @Description
     * @Return void
     */
    public <T> void doPost(final Class<T> tClass, String UrlPath, final OnRetrofit.OnQueryMapListener<T> listener) {
        HashMap<String, String> map = new HashMap<>();
        listener.onMap(map);
        apiService
                .getPostData(UrlPath, map)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().getObserver(tClass, listener));
    }

    public void cancleAll(){

        MyObserver.create().dispose();
    }
    /**
     * @param url          下载地址
     * @param downLoadPath 下载路径  1234
     * @param FileName     下载的文件名  1234.jpg
     * @param listener
     * @Description
     * @Return void
     */
    public void doDownLoad(String url, String downLoadPath, String FileName, OnRetrofit.OnDownLoadListener listener) {
        apiService.downloadFile(url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .subscribe(MyObserver.create().createDownLoadObserver(downLoadPath, FileName, listener));
    }


    /**
     * @param url
     * @param downLoadPath
     * @param listener
     * @Description 文件名 是下载时候的文件名
     * @Return void
     */
    public void doDownLoad(String url, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
        apiService.downloadFile(url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .subscribe(MyObserver.create().createDownLoadObserver(downLoadPath, url, listener));
    }


    /**
     * @param url          下载地址
     * @param downLoadPath 下载路径  1234
     * @param FileName     下载的文件名  1234.jpg
     * @param listener
     * @Description 下载大文件
     * @Return void
     */
    public void doDownLoadBig(String url, String downLoadPath, String FileName, OnRetrofit.OnDownLoadListener listener) {
        String newFilePath = DownLoadUtil.create().createFile(downLoadPath) + FileName;
        DownLoadUtil.create().downLoadBig(apiService, url, newFilePath, listener);
    }

    /**
     * @param url          下载地址
     * @param downLoadPath 下载路径  1234
     * @param listener
     * @Description
     * @Return void
     */
    public void doDownLoadBig(String url, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
        String newFilePath = DownLoadUtil.create().createFile(downLoadPath) + DownLoadUtil.create().getFileName(url);
        DownLoadUtil.create().downLoadBig(apiService, url, newFilePath, listener);
    }

    /**
     * @param urlKey
     * @Description
     * @Return void
     */
    public void stopDown(String urlKey) {
        DownLoadUtil.create().stopDown(urlKey);
    }


    public <T> void doUpLoad(Class<T> tClass, String url, OnRetrofit.OnUpLoadListener<T> listener) {
        HashMap<String, String> FormDataPartMap = new HashMap<>();
        ArrayList<File> fileArrayList = new ArrayList<>();
        listener.onFormDataPartMap(FormDataPartMap);
        listener.onFileList(fileArrayList);
        MultipartBody multipartBody = UpLoadUtil.create().createMultipartBody(FormDataPartMap, fileArrayList);
        apiService.upLoad(url, multipartBody)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .subscribe(MyObserver.create().createUpLoad(tClass, listener));
    }


}
