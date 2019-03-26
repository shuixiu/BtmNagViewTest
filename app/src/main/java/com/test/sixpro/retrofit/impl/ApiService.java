package com.test.sixpro.retrofit.impl;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ApiService {


    @GET
    Observable<ResponseBody> getGetData(@Url String url);


    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> getPostData(@Path("path") String urlPath, @FieldMap Map<String, String> map);


    @Streaming //大文件时要加不然会OOM
    @GET
    Observable<ResponseBody> downloadFile(@Header("Range") String range, @Url String fileUrl);

    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


    //上传
    @POST()
    Observable<ResponseBody> upLoad(@Url() String url, @Body RequestBody Body);

}
