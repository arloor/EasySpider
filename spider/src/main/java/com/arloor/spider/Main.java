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

        int page=1;
        int maxPage=13;
        for (int i = page; i <=maxPage ; i++) {
            StepContext initStepContext =StepContext.init("Index","https://www.seedrs.com/invest?order_by=closing_date&page="+i+"&previously_funded=true");
            initStepContext.getData().put("page",i);
            stepQueue.add(initStepContext);
        }
        Thread graberThread=new Thread(new Graber());
        graberThread.start();
    }
}
