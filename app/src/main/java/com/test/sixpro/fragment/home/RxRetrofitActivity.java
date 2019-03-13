package com.test.sixpro.fragment.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.test.sixpro.R;
import com.test.sixpro.base.BaseActivity;
import com.test.sixpro.content.Urls;
import com.test.sixpro.http.ServerApi;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Headers;

public class RxRetrofitActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rx);
        ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    @OnClick(R.id.bt_get)
    public void getRx(View view) {

        OkGo.<String>post(Urls.URL_METHOD)//
                .converter(new StringConvert())//
                .adapt(new ObservableResponse<String>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        Log.d("wwn", "accept");

                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                        Log.d("wwn", "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Response<String> response) {
                        Log.d("wwn", "onNext");

                        handleResponse(response);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        showToast("请求失败");
                    }

                    @Override
                    public void onComplete() {

                        Log.d("wwn", "onComplete");
                    }
                });


     /*   ServerApi.getString("", "")//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        Log.d("wwn", "accept");
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())  //
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                        Log.d("wwn", "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d("wwn", "onNext");
                        Log.d("wwn", s + "--------");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();            //请求失败
                        showToast("请求失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("wwn", "onComplete");
                    }
                });*/
    }
    protected <T> void handleResponse(Response<T> response) {
        StringBuilder sb;
        T body = response.body();
        if (body == null) {
            Log.d("wwn","--null");
        } else {
            if (body instanceof String) {
                Log.d("wwn",(String) body);
            }
        }

     /*   okhttp3.Response rawResponse = response.getRawResponse();
        if (rawResponse != null) {
            Headers responseHeadersString = rawResponse.headers();
            Set<String> names = responseHeadersString.names();
            sb = new StringBuilder();
            sb.append("url ： ").append(rawResponse.request().url()).append("\n\n");
            sb.append("stateCode ： ").append(rawResponse.code()).append("\n");
            for (String name : names) {
                sb.append(name).append(" ： ").append(responseHeadersString.get(name)).append("\n");
            }
            Log.d("wwn",sb.toString());
        } else {
            Log.d("wwn","--");
        }*/
    }
}
