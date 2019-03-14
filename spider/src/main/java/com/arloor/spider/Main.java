package com.arloor.spider;

import com.alibaba.fastjson.JSONObject;
import com.arloor.taskmanager.StepContainer;
import com.arloor.taskmanager.StepContext;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static Queue<StepContext> stepQueue=new LinkedBlockingQueue<>();

    public static void main(String[] args){
        StepContainer.list();
//        Step step=StepContainer.getStepByName("Index");

        int page=1;
        int maxPage=13;
        for (int i = page; i <=maxPage ; i++) {
            StepContext initStepContext =new StepContext();
            JSONObject data=new JSONObject();
            data.put("page",i);
            initStepContext.setData(data);
            initStepContext.setUrl("https://www.seedrs.com/invest?order_by=closing_date&page="+i+"&previously_funded=true");
//            initStepContext.setUrl("https://www.seedrs.com/learnamp");
            initStepContext.setCurrentStep("Index");
            stepQueue.add(initStepContext);
        }

        Thread graberThread=new Thread(new Graber());
        graberThread.start();
    }
}
