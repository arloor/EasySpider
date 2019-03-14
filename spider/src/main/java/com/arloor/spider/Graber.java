package com.arloor.spider;

import com.arloor.spidertask.Step;
import com.arloor.spidertask.StepContainer;
import com.arloor.spidertask.StepContext;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.List;

public class Graber implements Runnable {
    MyHttpClient client=new MyHttpClient(700);

    @Override
    public void run() {
        StepContext stepContext=null;
        while((stepContext= Main.stepQueue.poll())!=null){
            String stepName=stepContext.getCurrentStep();
            if(!stepName.equals("Save")){
                Step step= StepContainer.getStepByName(stepName);
                HttpUriRequest request=step.createRequest(stepContext);
                System.out.println(request);
                CloseableHttpResponse response=client.doRequest(request);
                if(response==null){
                    continue;
                }
                try {
                    step.doParse(response,stepContext);
                    List<StepContext> newStepContexts=stepContext.getNewStepContexts();
                    Main.stepQueue.addAll(newStepContexts);
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //todo: save
                StepContainer.getSave().save(stepContext.getData());
            }

        }
    }

}
