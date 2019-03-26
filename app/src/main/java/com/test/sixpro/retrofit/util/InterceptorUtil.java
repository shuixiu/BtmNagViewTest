package com.test.sixpro.retrofit.util;

import android.util.Log;


import com.test.sixpro.retrofit.WRetrofitApp;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class InterceptorUtil {


    //创建拦截器
    public static HttpLoggingInterceptor LogInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(final String message) {
                Log.e(WRetrofitApp.getLogTag(), "retrofitBack -> " + message); //打印retrofit日志
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }


    //添加请求头
    public static Interceptor HeaderInterceptor(final Map<String, String> headMap) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder1 = request.newBuilder();
                Request build = null;
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    build = builder1.addHeader(entry.getKey(), entry.getValue()).build();
                }
                return chain.proceed(build);
            }
        };

    }
}
