package com.test.sixpro.retrofit.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;


import com.test.sixpro.retrofit.WRetrofitApp;
import com.test.sixpro.retrofit.impl.ApiService;
import com.test.sixpro.retrofit.impl.OnRetrofit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by allens on 2017/11/29.
 */

public class DownLoadUtil {


    private static DownLoadUtil mInstance;
    private String filePath;//下载文件的路径
    private String TAG_CurrentLength = "CurrentLength";

    public static DownLoadUtil create() {
        if (mInstance == null) {
            synchronized (DownLoadUtil.class) {
                mInstance = new DownLoadUtil();
            }
        }
        return mInstance;
    }


    //下载小文件
    public synchronized void downLoad(ResponseBody responseBody, String filepath, final OnRetrofit.OnDownLoadListener listener) {
        this.filePath = filepath;
        FileOutputStream fos = null;
        InputStream inputStream = responseBody.byteStream();
        long length = responseBody.contentLength();// 流的大小
        try {
            fos = new FileOutputStream(filepath, true);
            int n = 0;
            int currentLength = 0; //当前的长度
            byte[] buf = new byte[1024];
            while ((n = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, n);
                currentLength = currentLength + n;
                final int terms = (int) (((float) currentLength) / (length) * 100); // 计算百分比
                handlerSuccess(terms, listener);
            }
            fos.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            handlerFailed(e, listener);
        } catch (IOException e) {
            handlerFailed(e, listener);
        }
    }


    //下载大文件
    public synchronized void downLoadBig(ApiService apiService, final String url, final String filePath, final OnRetrofit.OnDownLoadListener listener) {
        this.filePath = filePath;
        startDown(url);//初始化 修改状态 为下载
        final Long readLength = ShareUtil.create(WRetrofitApp.getApplication()).getLong(TAG_CurrentLength, (long) 0);
        Observable<ResponseBody> observable = apiService.downloadFile("bytes=" +
                readLength +
                "-", url);
        observable
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        long length = responseBody.contentLength();// 流的大小
                        FileOutputStream fos = null;
                        InputStream inputStream = responseBody.byteStream();
                        try {
                            fos = new FileOutputStream(filePath, true);
                            int n = 0;
                            long currentLength = 0; //当前的长度
                            byte[] buf = new byte[1024 * 4];
                            while ((n = inputStream.read(buf)) != -1) {
                                if (isStopDown(url))
                                    break;
                                fos.write(buf, 0, n);
                                currentLength += n;
                                ShareUtil.create(WRetrofitApp.getApplication()).putLong(TAG_CurrentLength, currentLength + readLength);
                                final int terms = (int) (((float) (currentLength + readLength)) / (length + readLength) * 100); // 计算百分比
                                handlerSuccess(terms, listener);
                            }
                            fos.close();
                            inputStream.close();
                        } catch (FileNotFoundException e) {
                            handlerFailed(e, listener);
                        } catch (IOException e) {
                            handlerFailed(e, listener);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerFailed(e, listener);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    //停止下载
    public void stopDown(String urlKey) {
        ShareUtil.create(WRetrofitApp.getApplication()).putBoolean(urlKey, true);
    }

    private void startDown(String urlKey) {
        ShareUtil.create(WRetrofitApp.getApplication()).putBoolean(urlKey, false);
    }


    //判断是否需要停止下载   true  就是要停止下载
    private boolean isStopDown(String urlKey) {
        return ShareUtil.create(WRetrofitApp.getApplication()).getBoolean(urlKey, false);
    }


    //创建文件夹  返回文件夹地址
    public String createFile(String downLoadPath) {
        String newPath = Environment.getExternalStorageDirectory().getPath() + File.separator + downLoadPath + File.separator;
        File file = new File(newPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return newPath;
    }


    // 失败时候
    public void handlerFailed(Throwable throwable, final OnRetrofit.OnDownLoadListener listener) {
        Observable.just(throwable)
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.onError(throwable);
                    }
                });
    }

    // 成功时候
    @SuppressLint("CheckResult")
    private void handlerSuccess(int terms, final OnRetrofit.OnDownLoadListener listener) {
        Observable.just(terms)
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        listener.onSuccess(integer);
                        if (integer == 100) {
                            ShareUtil.create(WRetrofitApp.getApplication()).clear();
                            isAPK();
                        }
                    }
                });
    }

    //判断是不是APK
    private void isAPK() {
        File file = new File(filePath);
        if (file.getName().endsWith(".apk")) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(WRetrofitApp.getApplication(), WRetrofitApp.getApplication().getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri
            } else {
                uri = Uri.fromFile(file);
            }
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            WRetrofitApp.getApplication().startActivity(intent);
        }
    }

    //获取下载文件的名称
    public String getFileName(String downLoadUrl) {
        URL url = null;
        String filename = null;
        try {
            url = new URL(downLoadUrl);
            filename = url.getFile();
            return filename.substring(filename.lastIndexOf("/") + 1);
        } catch (MalformedURLException e) {
            return null;
        }
    }


    //判断是否已经下载
    public Boolean isAlreadyDownLoadFromUrl(String downLoadPath, String downLoadUrl) {
        String filePath = createFile(downLoadPath);
        String fileName = getFileName(downLoadUrl);
        String newFilePath = filePath + fileName;
        File file = new File(newFilePath);
        return file.exists();
    }

    public Boolean isAlreadyDownLoadFromFileName(String downLoadPath, String fileName) {
        String filePath = createFile(downLoadPath);
        String newFilePath = filePath + fileName;
        File file = new File(newFilePath);
        return file.exists();
    }
}
