package com.arloor.taskmanager;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StepContext {
    String currentStep;
    String url;
    JSONObject data;
    List<StepContext> newStepContexts =new LinkedList<>();
    Map<String,String> cookies=new HashMap<>();

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

    public void setNewStepContexts(List<StepContext> newStepContexts) {
        this.newStepContexts = newStepContexts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
