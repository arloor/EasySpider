package com.arloor.taskmanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StepContext {
    String currentStep;
    String url;
    JSONObject data = new JSONObject();
    List<StepContext> newStepContexts = new LinkedList<>();
    private StepContext(String initStep,String url){
        this.currentStep=initStep;
        this.url=url;
    }

    private StepContext(){
    }

    //创建初始任务
    public static StepContext init(String initStep,String url){
        return new StepContext(initStep,url);
    }

    //由老的stepContext创建新的Stepcontext
    public static StepContext derive(StepContext oldContext,String nextStep){
        StepContext newContext=new StepContext();
        newContext.setCurrentStep(nextStep);
        newContext.setData((JSONObject) oldContext.data.clone());
        return newContext;
    }


    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public List<StepContext> getNewStepContexts() {
        return newStepContexts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addCookie(String key, String value) {
        JSONObject cookies = data.getJSONObject("cookies");
        if (cookies == null) {
            cookies = new JSONObject();
            data.put("cookies", cookies);
        }

        Map<String, Object> cookieMap = cookies.getInnerMap();
        for (Map.Entry<String, Object> cookie : cookieMap.entrySet()
        ) {
            if (cookie.getKey().equals(key)) {
                cookie.setValue(value);
                return;
            }
        }
        cookieMap.put(key, value);
    }

    public void addCookie(String setCookieStr) {
        String key = setCookieStr.substring(0, setCookieStr.indexOf("="));
        String value = setCookieStr.substring(setCookieStr.indexOf("=") + 1, setCookieStr.indexOf(";"));
        addCookie(key, value);
    }

    public String getCookieHeader() {
        JSONObject cookies = data.getJSONObject("cookies");
        if (cookies == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            Map<String, Object> cookieMap = cookies.getInnerMap();
            for (Map.Entry<String, Object> cookie : cookieMap.entrySet()
            ) {
                sb.append(cookie.getKey()+"="+cookie.getValue()+"; ");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
            return sb.toString();
        }
    }
}
