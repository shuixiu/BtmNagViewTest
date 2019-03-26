package com.test.sixpro.retrofit;

import android.app.Application;


public class WRetrofitApp {
    private static Application application;
    private static boolean debug;
    private static String LogTag = "WRetrofit";
    private static int connectTimeout = 10;
    private static int readTimeout = 10;
    private static int writeTimeout = 10;
//    private static Activity baseActivity;
//    private static Fragment baseFragment;

    public static void init(Application app) {
        setApplication(app);
        setDebug(true);
    }

    public static void init(Application app, boolean debug) {
        setApplication(app);
        setDebug(debug);
    }


    /*  get set.*/
//    public static Fragment getBaseFragment() {
//        return baseFragment;
//    }
//
//    public static void setBaseFragment(Fragment baseFragment) {
//        XRetrofitApp.baseFragment = baseFragment;
//    }
//
//    public static Activity getBaseActivity() {
//        return baseActivity;
//    }
//
//    public static void setBaseActivity(Activity activity) {
//        XRetrofitApp.baseActivity = activity;
//    }

    public static String getLogTag() {
        return LogTag;
    }

    public static void setLogTag(String logTag) {
        LogTag = logTag;
    }

    public static int getConnectTimeout() {
        return connectTimeout;
    }

    public static void setConnectTimeout(int connectTimeout) {
        WRetrofitApp.connectTimeout = connectTimeout;
    }

    public static int getReadTimeout() {
        return readTimeout;
    }

    public static void setReadTimeout(int readTimeout) {
        WRetrofitApp.readTimeout = readTimeout;
    }

    public static int getWriteTimeout() {
        return writeTimeout;
    }

    public static void setWriteTimeout(int writeTimeout) {
        WRetrofitApp.writeTimeout = writeTimeout;
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        WRetrofitApp.application = application;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        WRetrofitApp.debug = debug;
    }
}
