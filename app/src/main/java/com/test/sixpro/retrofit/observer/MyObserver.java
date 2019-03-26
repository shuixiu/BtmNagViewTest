package com.test.sixpro.retrofit.observer;

import android.app.Activity;

import com.google.gson.Gson;
import com.test.sixpro.retrofit.impl.OnRetrofit;
import com.test.sixpro.retrofit.util.DownLoadUtil;
import com.test.sixpro.retrofit.util.WDialogUtil;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;


public class MyObserver {

    public static MyObserver create() {
        return new MyObserver();
    }

    public Observer<ResponseBody> createDownLoadObserver(
            final String downLoadPath,
            final String fileNameOrUrl,
            final OnRetrofit.OnDownLoadListener listener) {
        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String newFilePath = null;
                String filePath = DownLoadUtil.create().createFile(downLoadPath);
                if (fileNameOrUrl.contains("https://")
                        || fileNameOrUrl.contains("http://")
                        || fileNameOrUrl.contains("ftp://")
                        || fileNameOrUrl.contains("rtsp://")
                        || fileNameOrUrl.contains("mms://")
                        ) {
                    newFilePath = filePath + DownLoadUtil.create().getFileName(fileNameOrUrl);
                } else {
                    newFilePath = filePath + fileNameOrUrl;
                }
                DownLoadUtil.create().downLoad(responseBody, newFilePath, listener);
            }

            @Override
            public void onError(Throwable e) {
                DownLoadUtil.create().handlerFailed(e, listener);
            }

            @Override
            public void onComplete() {
            }
        };
    }

    private CompositeDisposable compositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }


    public <T> Observer<? super ResponseBody> getObserver(final Activity activity, final Class<T> tClass, final OnRetrofit.OnQueryMapListener<T> listener) {
        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                WDialogUtil.create().show(activity);
                addDisposable(d);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Gson gson = new Gson();
                    T t = gson.fromJson(responseBody.string(), tClass);
                    listener.onSuccess(t);
                } catch (IOException e) {
                    listener.onError(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onComplete() {
                WDialogUtil.create().hide();
            }
        };
    }


    public <T> Observer<? super ResponseBody> createUpLoad(final Class<T> tClass, final OnRetrofit.OnUpLoadListener<T> listener) {

        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Gson gson = new Gson();
                    T t = gson.fromJson(responseBody.string(), tClass);
                    listener.onSuccess(t);
                } catch (IOException e) {
                    listener.onError(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onComplete() {
            }
        };
    }
}
