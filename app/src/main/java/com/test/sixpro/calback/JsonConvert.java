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
package com.test.sixpro.calback;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {

        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

//        if (rawType == String.class) {
//            //noinspection unchecked
//            return (T) body.string();
//        } else if (rawType == JSONObject.class) {
//            //noinspection unchecked
//            return (T) new JSONObject(body.string());
//        } else if (rawType == JSONArray.class) {
//            //noinspection unchecked
//            return (T) new JSONArray(body.string());
//        } else {
//            T t = Convert.fromJson(jsonReader, rawType);
//            response.close();
//            return t;
//        }
        return null;
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
//        T t = Convert.fromJson(jsonReader, type);
//        response.close();/
//        return t;

        return null;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
//        if (rawType != LzyResponse.class) {
        // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
//            T t = Convert.fromJson(jsonReader, type);
//            response.close();
//            return t;
//        } else {
//            if (typeArgument == Void.class) {
        // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
//                SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
//                response.close();
        //noinspection unchecked
//                return (T) simpleResponse.toLzyResponse();
//            } else {
        // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
//                LzyResponse lzyResponse = Convert.fromJson(jsonReader, type);
//                response.close();
//                int code = lzyResponse.code;
        //这里的0是以下意思
        //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
//                if (code == 0) {
        //noinspection unchecked
//                    return (T) lzyResponse;
//                } else if (code == 104) {
//                    throw new IllegalStateException("用户授权信息无效");
//                } else if (code == 105) {
//                    throw new IllegalStateException("用户收取信息已过期");
//                } else {
        //直接将服务端的错误信息抛出，onError中可以获取
//                    throw new IllegalStateException("错误代码：" + code + "，错误信息：" + lzyResponse.msg);
//                }
//            }
        return null;
    }
}
//}
