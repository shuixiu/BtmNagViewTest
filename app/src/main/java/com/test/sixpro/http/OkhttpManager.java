package com.test.sixpro.http;


import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.test.sixpro.utils.LogInfo;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/29.
 */

public class OkhttpManager {


    public static String TAG = "debug-okhttp";
    /**
     * log 关闭为false
     */
    public static boolean isDebug = true;

    private static OkHttpClient client;
    // 超时时间
    public static final int TIME = 60;
    public static final int TIMEUNION = 10;
    public static final int TIMEOUT = 1000 * 30;

    //json请求
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");
    //username=111&pass=111请求
    public static final MediaType TYPENEW = MediaType
            .parse("application/x-www-form-urlencoded;charset=utf-8");

    private Handler handler = new Handler(Looper.getMainLooper());

    private int i = 0;
//    private Timer  timer;

    public OkhttpManager() {
        // TODO Auto-generated constructor stub
        this.init();
    }

    private void init() {

//        client = new OkHttpClient();
        client = new OkHttpClient.Builder().readTimeout(TIME, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(TIME, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(TIME, TimeUnit.SECONDS)//设置连接超时时间
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
        // 设置超时时间
//        .sslSocketFactory(createSSLSocketFactory())
//        .hostnameVerifier(new TrustAllHostnameVerifier())

//        client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS).build();

    }

    /**
     * post请求，String
     *
     * @param url
     * @param string
     * @param callback
     */
    public void postUnion(String url, String string, final HttpCallback callback) {

        RequestBody body = RequestBody.create(TYPENEW, string);
        final Request request = new Request.Builder().url(url).post(body).build();
        onStart(callback);
        OkHttpClient clientUnion = new OkHttpClient.Builder().readTimeout(TIMEUNION, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(TIMEUNION, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(TIMEUNION, TimeUnit.SECONDS)//设置连接超时时间
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();

        clientUnion.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();

                if (e instanceof SocketTimeoutException/* && i==TIME*/) {//响应超时
                    onTimeOut(callback, e.getMessage());
                }
                /*else if(e instanceof SocketException){//中断连接
                    e.printStackTrace();
                    client.newCall(request).enqueue(this);
                }*/
                else {
                    e.printStackTrace();
                    onError(callback, e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }

        });

    }

    public void cancleAll(String tag) {
        Log.d("wwn", "bt_money_pay_cancle");
        Dispatcher dispatcher = client.dispatcher();
        synchronized (dispatcher) {
            for (Call call : dispatcher.queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : dispatcher.runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }

    /**
     * post请求，String
     *
     * @param url
     * @param string
     * @param callback
     */
    public void postNew(String url, String string, final HttpCallback callback, String tag) {

        RequestBody body = RequestBody.create(TYPENEW, string);
        final Request request = new Request.Builder().url(url).post(body).tag(tag).build();
        onStart(callback);


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();

                if (e instanceof SocketTimeoutException/* && i==TIME*/) {//响应超时
                    onTimeOut(callback, e.getMessage());
                } else if (e instanceof SocketException) {//中断连接
                    e.printStackTrace();
                    Log.d("wwn", "SocketException");
//                    client.newCall(request).enqueue(this);
                } else {
                    e.printStackTrace();
                    onError(callback, e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }

        });

    }

    /**
     * post请求，String
     *
     * @param url
     * @param string
     * @param callback
     */
    public void postNew(String url, String string, final HttpCallback callback) {

        RequestBody body = RequestBody.create(TYPENEW, string);
        final Request request = new Request.Builder().url(url).post(body).build();
        onStart(callback);


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();

                if (e instanceof SocketTimeoutException/* && i==TIME*/) {//响应超时
                    onTimeOut(callback, e.getMessage());
                }
                /*else if(e instanceof SocketException){//中断连接
                    e.printStackTrace();
                    client.newCall(request).enqueue(this);
                }*/
                else {
                    e.printStackTrace();
                    onError(callback, e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }

        });

    }

    public void DownlodApk(String url, String string, final HttpCallback callback) {
        Request request = new Request.Builder().url(url).build();
        onStart(callback);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO Auto-generated method stub
                e.printStackTrace();
                onError(callback, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO Auto-generated method stub
                if (response.isSuccessful()) {
                    onDownload(callback, response);
                  /*  InputStream is = null;//输入流
                    FileOutputStream fos = null;//输出流
                    try {
                        is = response.body().byteStream();//获取输入流
                        long total = response.body().contentLength();//获取文件大小
                        view.setMax(total);//为progressDialog设置大小
                        if(is != null){
                            Log.d("SettingPresenter", "onResponse: 不为空");
                            File file = new File(Environment.getExternalStorageDirectory(),"Earn.apk");// 设置路径
                            fos = new FileOutputStream(file);
                            byte[] buf = new byte[1024];
                            int ch = -1;
                            int process = 0;
                            while ((ch = is.read(buf)) != -1) {
                                fos.write(buf, 0, ch);
                                process += ch;
                                view.downLoading(process);       //这里就是关键的实时更新进度了！
                            }

                        }
                        fos.flush();
                        // 下载完成
                        if(fos != null){
                            fos.close();
                        }
                        view.downSuccess();
                    } catch (Exception e) {
                        view.downFial();
                        Log.d("SettingPresenter",e.toString());
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                        }
                    }*/
                } else {
                    onError(callback, response.message());
                }
            }
        });

    }


    /**
     * post请求，json数据为body
     *
     * @param url
     * @param json
     * @param callback
     */
    public void postJson(String url, String json, final HttpCallback callback) {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();

        onStart(callback);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onError(callback, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }

        });

    }

    /**
     * post请求 map为body
     *
     * @param url
     * @param map
     * @param callback
     */
    public void post(String url, Map<String, Object> map, final HttpCallback callback) {

        // FormBody.Builder builder = new FormBody.Builder();
        // FormBody body=new FormBody.Builder().add("key", "value").build();

        /**
         * 创建请求的参数body
         */
        FormBody.Builder builder = new FormBody.Builder();

        /**
         * 遍历key
         */
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = "
                        + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());

            }
        }

        RequestBody body = builder.build();

        Request request = new Request.Builder().url(url).post(body).build();

        onStart(callback);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onError(callback, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    onSuccess(callback, response.body().string());

                } else {
                    onError(callback, response.message());
                }
            }

//
//            @Override
//            public void onFailure(Request arg0, IOException arg1) {
//                // TODO Auto-generated method stub
//                arg1.printStackTrace();
//                onError(callback, arg1.getMessage());
//            }
        });

    }

    /**
     * get请求
     *
     * @param url
     * @param callback
     */
    public void get(String url, final HttpCallback callback) {


        RequestBody body = RequestBody.create(TYPENEW, "");
        final Request request = new Request.Builder().url(url).post(body).build();
        onStart(callback);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                // TODO Auto-generated method stub

                e.printStackTrace();
                onError(callback, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO Auto-generated method stub

                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }


        });

    }

    public void uploadFileNew(String url, Map<String, String> maps, Map<String, File> fileMap, final HttpCallback callback) {

        //多个文件集合
//        ArrayList<File> list
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置为表单类型
        builder.setType(MultipartBody.FORM);

        for (String key : maps.keySet()) {
            //添加表单键值
            builder.addFormDataPart(key, maps.get(key));
        }

        if(fileMap!=null){
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {

                LogInfo.log("wwn",entry.getKey()+"------------");
                builder.addFormDataPart(entry.getKey(), entry.getValue().getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), entry.getValue()));
            }
        }


        Request request = new Request.Builder().url(url).post(builder.build()).build();
        onStart(callback);
        //发起异步请求，并加入回调
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onError(callback, e.getMessage());
                LogInfo.log("wwn",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }

            }
        });
    }


    /**
     * log信息打印
     *
     * @param params
     */
    public void debug(String params) {
        if (false == isDebug) {
            return;
        }

        if (null == params) {
            Log.d(TAG, "params is null");
        } else {
            Log.d(TAG, params);
        }
    }

    private void onStart(HttpCallback callback) {
        if (null != callback) {
            callback.onStart();
        }
    }

    private void onSuccess(final HttpCallback callback, final String data) {

        debug(data);

        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onSuccess(data);
                }
            });
        }
    }

    private void onError(final HttpCallback callback, final String msg) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onError(msg);
                }
            });
        }
    }

    private void onDownload(final HttpCallback callback, final Response response) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onDownload(response);
                }
            });
        }
    }

    private void onTimeOut(final HttpCallback callback, final String msg) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onTimeOut(msg);
                }
            });
        }
    }

    public static void CanCel() {
        client.dispatcher().cancelAll();
    }

    /**
     * http请求回调
     *
     * @author Flyjun
     */
    public static abstract class HttpCallback {
        // 开始
        public void onStart() {
        }


        // 成功回调
        public abstract void onSuccess(String data);

        // 失败回调
        public void onError(String msg) {
        }

        public void onDownload(Response response) {
        }


        // 响应超时
        public void onTimeOut(String msg) {
        }

    }


//    public static void post(String url, String json, Callback callback) throws IOException {
//        try {
//            OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
////            mBuilder.sslSocketFactory(createSSLSocketFactory());
////            mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
//            OkHttpClient client = mBuilder.build();
//
//            RequestBody body = RequestBody.create(JSON, json);
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .build();
//            client.newCall(request).enqueue(callback);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    //    private void setPayTimer(){
//
//        timer = new Timer();
//        i = 0;
//        // 创建一个TimerTask
//        // TimerTask是个抽象类,实现了Runnable接口，所以TimerTask就是一个子线程
//        TimerTask timerTask3 = new TimerTask() {
//            // 倒数3秒
//            @Override
//            public void run() {
//                // 定义一个消息传过去
//                i++;
//
//                Log.i("wwn",i+"-------------");
//            }
//        };
//        timer.schedule(timerTask3, 0, 1000);// 3秒后开始倒计时，倒计时间隔为1秒
//    }

}
