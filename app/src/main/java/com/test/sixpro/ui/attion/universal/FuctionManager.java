package com.test.sixpro.ui.attion.universal;

import android.text.TextUtils;

import com.test.sixpro.utils.LogInfo;

import java.util.HashMap;
import java.util.Map;

public class FuctionManager {

    private static volatile FuctionManager instance ;


    private Map<String, FunctionNoParamNoResult> noParamNoResultMap;
    private Map<String, FunctionHasParamHasResult> hasParamHasResultMap;


    private FuctionManager() {

        noParamNoResultMap = new HashMap<>();
        hasParamHasResultMap = new HashMap<>();
    }

    public static FuctionManager getInstance() {
        if (instance == null) {
            instance = new FuctionManager();
        }
        return instance;
    }
    //添加构造一个方法 用来添加Activity中的方法保存到对应的map中
    public void addFunction(FunctionNoParamNoResult function) {
        if(noParamNoResultMap==null){
            return;
        }
        noParamNoResultMap.put(function.functionName, function);
    }

    //添加执行没有参数没有返回值的方法
    public void invokeFunction(String functionName) {
        if (TextUtils.isEmpty(functionName)) {
            return;
        }
        if (noParamNoResultMap != null) {
            FunctionNoParamNoResult f = noParamNoResultMap.get(functionName);
            if (f != null) {
                f.function();
            } else {
                LogInfo.log("wwn", "找不到对应的方法：" + functionName);
            }
        }
    }

    //添加执行没有参数有返回值的方法
   /* public void addFunction(FunctionNoParamNoResult function) {
        if(noParamNoResultMap==null){
            return;
        }
        noParamNoResultMap.put(function.functionName, function);
    }
    public <T> T invokeFunction(String functionName,Class<T> t) {
        if (TextUtils.isEmpty(functionName)) {
            return null;
        }
        if (noParamNoResultMap != null) {
            FunctionNoParamNoResult f = noParamNoResultMap.get(functionName);
            if (f != null) {
                return t.cast(f.function());
            } else {
                LogInfo.log("wwn", "找不到对应的方法：" + functionName);
            }
        }
        return null;
    }*/

    //添加执行有参数有返回值的方法

    //添加执行有参数有返回值的方法
    public void addFunction(FunctionHasParamHasResult function) {
        if(hasParamHasResultMap==null){
            return;
        }
        hasParamHasResultMap.put(function.functionName, function);
    }
    public <T,P> T invokeFunction(String functionName,Class<T> t,P p) {
        if (TextUtils.isEmpty(functionName)) {
            return null;
        }
        if (hasParamHasResultMap != null) {
            FunctionHasParamHasResult f = hasParamHasResultMap.get(functionName);
            if (f != null) {
                return t.cast(f.function(p));
            } else {
                LogInfo.log("wwn", "找不到对应的方法：" + functionName);
            }
        }
        return null;
    }


    public void removeFunction(String functionName){
        if(noParamNoResultMap!=null){
            noParamNoResultMap.remove(functionName);
        }
    }
    public void removeAllNoParamNoResultFunction(){
        if(noParamNoResultMap!=null){
            noParamNoResultMap.clear();
            noParamNoResultMap=null;
        }
    }
    public void removeAllHasParamHasResultFunction(){
        if(hasParamHasResultMap!=null){
            hasParamHasResultMap.clear();
            hasParamHasResultMap=null;
        }
    }
    public void removeAllFunction(){
        if(noParamNoResultMap!=null){
            noParamNoResultMap.clear();
            noParamNoResultMap=null;
        }
        if(hasParamHasResultMap!=null){
            hasParamHasResultMap.clear();
            hasParamHasResultMap=null;
        }
    }
}
