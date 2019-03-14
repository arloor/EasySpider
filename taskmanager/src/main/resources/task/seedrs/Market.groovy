package task.seedrs

import com.alibaba.fastjson.JSONObject
import com.arloor.taskmanager.Step
import com.arloor.taskmanager.StepContext
import org.apache.http.Header
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class Market implements Step{
    @Override
    HttpUriRequest createRequest(StepContext context) {
        HttpGet request=new HttpGet(context.getUrl());
        return request
    }

    @Override
    void doParse(CloseableHttpResponse response, StepContext context) {
        println response.getStatusLine()
        String str= EntityUtils.toString(response.getEntity())

        Document doc= Jsoup.parse(str);
        Elements elements=doc.select(".market")
        StringBuffer sb=new StringBuffer()
        for (int i = 0; i < elements.size(); i++) {
            Element element=elements.get(i)
            Elements nodes=element.children()
            for (int j = 0; j < nodes.size(); j++) {
                Element node=nodes.get(j)
                String tagName=node.tagName();
                if(tagName=="img"){
                    sb.append "![]("+node.attr("src")+")\n\n"
                    StepContext newContext=new StepContext();
                    newContext.setUrl(node.attr("src"))
                    newContext.setCurrentStep("Image");
                    context.newStepContexts.add(newContext)
                }
                if(tagName.startsWith("h")){
                    int titleLevel=Integer.parseInt(tagName.substring(1))
                    for (int k = 0; k <titleLevel ; k++) {
                        sb.append "#"
                    }
                    sb.append " "+node.html()+"\n\n"
                }
                if(tagName.startsWith("p")){
                    sb.append node.html()+"\n\n"
                }
            }
            String content=sb.toString()
            StepContext newContext=new StepContext()
            JSONObject data=context.data.clone()
            data.put("market",content)
            newContext.setData(data)
            newContext.setCurrentStep("Save")
            context.newStepContexts.add(newContext)

            //下载图片

        }
    }
}
