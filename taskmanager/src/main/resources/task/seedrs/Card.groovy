package task.seedrs

import com.arloor.taskmanager.Step
import com.arloor.taskmanager.StepContext
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class Card implements Step{
    @Override
    HttpUriRequest createRequest(StepContext context) {
        HttpGet request=new HttpGet(context.getUrl())
        return request
    }

    @Override
    void doParse(CloseableHttpResponse response, StepContext context) {
        println response.getStatusLine()
        String str= EntityUtils.toString(response.getEntity())
        EntityUtils.consume(response.getEntity())

        Document doc= Jsoup.parse(str);
        Elements elements=doc.select(".idea")
        StringBuffer sb=new StringBuffer()
        for (int i = 0; i < elements.size(); i++) {
            Element element=elements.get(i)
            Elements nodes=element.children()
            for (int j = 0; j < nodes.size(); j++) {
                Element node=nodes.get(j)
                String tagName=node.tagName();
                if(tagName=="img"){
                    sb.append "![]("+node.attr("src").replaceAll("https://seedrs.imgix.net/","")+")\n\n"
                    StepContext newContext=StepContext.derive(context,"Image");
                    newContext.setUrl(node.attr("src"))
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
            StepContext newContext=null
            Elements navs=doc.select(".Tabs-nav.Tabs-async.js-campaign-nav li")
            boolean marketExists=false
            for (int j = 0; j < navs.size(); j++) {
                if(navs.get(j).attr("data-section")=="market"){
                    newContext=StepContext.derive(context,"Market");
                    newContext.setUrl("https://www.seedrs.com"+navs.get(j).attr("data-tab-url"))
                    marketExists=true
                    break
                }
            }
            if(!marketExists){
                newContext=StepContext.derive(context,"Save");
            }
            newContext.data.put("idea",content)
            context.newStepContexts.add(newContext)

            //下载图片
        }
    }
}
