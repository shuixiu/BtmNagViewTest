package com.test.sixpro.retrofit.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class UpLoadUtil {

    private static UpLoadUtil mInstance;

    public static UpLoadUtil create() {
        if (mInstance == null) {
            synchronized (UpLoadUtil.class) {
                mInstance = new UpLoadUtil();
            }
        }
        return mInstance;
    }


    public MultipartBody createMultipartBody(HashMap<String, String> FormDataPartMap, List<File> fileList) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : FormDataPartMap.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        for (File file : fileList) {
            builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
        return builder.build();
    }
}
