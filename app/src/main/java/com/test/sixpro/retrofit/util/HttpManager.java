package com.test.sixpro.retrofit.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.test.sixpro.retrofit.WRetrofitApp;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by allens on 2017/12/8.
 */

public class HttpManager {

    private static HttpManager mInstance;
    private Retrofit retrofit;
    private Map<String, String> headMap;

    private HttpManager() {
    }

    public static HttpManager create() {
        if (mInstance == null) {
            synchronized (HttpManager.class) {
                mInstance = new HttpManager();
            }
        }
        return mInstance;
    }

    public HttpManager build(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        retrofit = builder
                .client(createOKHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .baseUrl(baseUrl)
                .build();
        return this;
    }

   /* public void cancleAll(){
        createOKHttpClient().dispatcher().cancelAll();
    }

    public void cancleTag(String tag){
        Log.d("wwn","bt_money_pay_cancle");
        Dispatcher dispatcher = createOKHttpClient().dispatcher();
        synchronized (dispatcher){
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
    }*/

    private OkHttpClient createOKHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (WRetrofitApp.isDebug())
            builder.addInterceptor(InterceptorUtil.LogInterceptor());//添加拦截
        if (headMap != null && headMap.size() > 0)
            builder.addInterceptor(InterceptorUtil.HeaderInterceptor(headMap));//添加请求头
        return builder
                .readTimeout(WRetrofitApp.getReadTimeout(), TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRetrofitApp.getWriteTimeout(), TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(WRetrofitApp.getConnectTimeout(), TimeUnit.SECONDS)//设置连接超时时间
//                .retryOnConnectionFailure(true)//连接失败后是否重新连接

                //添加HTTPS 证书
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
    }

    //拼接URL
    public String prepareParam(Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        if (paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }


    //获取接口类
    public <T> T getService(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    //添加请求头
    public void addHeard(Map<String, String> heardMap) {
        this.headMap = heardMap;
    }



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

}
