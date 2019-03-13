/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.test.sixpro.http;

import android.graphics.Bitmap;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.BitmapConvert;
import com.lzy.okgo.convert.FileConvert;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.test.sixpro.content.Urls;

import java.io.File;
import java.lang.reflect.Type;

import io.reactivex.Observable;

public class ServerApi {

    public static Observable<String> getString(String header, String param) {
        HttpHeaders headers = new HttpHeaders();
        HttpParams params = new HttpParams();
        return RxUtils.request(HttpMethod.POST, Urls.URL_METHOD, String.class, params, headers);
    }

    public static <T> Observable<T> getData(Type type, String url, String header, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("aaa", header);
        HttpParams params = new HttpParams();
        params.put("bbb", param);
        return RxUtils.request(HttpMethod.POST, url, type, params, headers);
    }

//    public static Observable<Response<Bitmap>> getBitmap(String header, String param) {
//        return OkGo.<Bitmap>post(Urls.URL_IMAGE)//
//                .headers("aaa", header)//
//                .params("bbb", param)//
//                .converter(new BitmapConvert())//
//                .adapt(new ObservableResponse<Bitmap>());
//    }
//
//    public static Observable<Response<File>> getFile(String header, String param) {
//        return OkGo.<File>get(Urls.URL_DOWNLOAD)//
//                .headers("aaa", header)//
//                .params("bbb", param)//
//                .converter(new FileConvert())//
//                .adapt(new ObservableResponse<File>());
//    }
}
