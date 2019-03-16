package com.arloor.spider;

import com.arloor.taskmanager.Step;
import com.arloor.taskmanager.StepContainer;
import com.arloor.taskmanager.StepContext;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Graber implements Runnable {
    private static Logger logger= LoggerFactory.getLogger(Graber.class);
    MyHttpClient client = new MyHttpClient(0);

    @Override
    public void run() {
        StepContext stepContext = null;
        while ((stepContext = Main.stepQueue.poll()) != null) {
            String stepName = stepContext.getCurrentStep();
            if (!stepName.equals("Save")) {
                Step step = StepContainer.getStepByName(stepName);
                HttpUriRequest request = step.createRequest(stepContext);
                //增加cookie
                String cookieHeader =stepContext.getCookieHeader();
                if (cookieHeader != null) {
//                    logger.info("Cookie: {}",cookieHeader);
                    request.setHeader("Cookie", cookieHeader);
                }
                System.out.println(request);
                CloseableHttpResponse response = client.doRequest(request);
                if (response == null) {
                    continue;
                }
                try {
                    step.doParse(response, stepContext);
                    List<StepContext> newStepContexts = stepContext.getNewStepContexts();
                    //遍历，找到Save的任务，直接save。这里保证了save命令提前运行。
                    Iterator<StepContext> iterator=newStepContexts.iterator();
                    while(iterator.hasNext()){
                        StepContext newStepContext=iterator.next();
                        if(newStepContext.getCurrentStep().equals("Save")){
                            StepContainer.getSave().save(newStepContext.getData());
                            iterator.remove();
                        }
                    }
                    Main.stepQueue.addAll(newStepContexts);
                    //增加cookie
                    Header[] setCookies = response.getHeaders("Set-Cookie");
                    for (Header setCookie : setCookies
                    ) {
                        newStepContexts.forEach(cell -> {
                            cell.addCookie(setCookie.getValue());
                        });
                    }
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //不应该走到这里哦，因为save会直接做的
            }

        }
    }


}
