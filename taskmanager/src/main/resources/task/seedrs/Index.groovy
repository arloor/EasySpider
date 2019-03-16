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

class Index implements Step{

    @Override
    HttpUriRequest createRequest(StepContext context) {
        HttpGet request=new HttpGet(context.getUrl())
        return request
    }

    @Override
    void doParse(CloseableHttpResponse response,StepContext context) {
        println response.getStatusLine()
        String str= EntityUtils.toString(response.getEntity())

        Document doc= Jsoup.parse(str);
        Elements elements=doc.select(".Card-link")
        for (int i = 0; i < elements.size(); i++) {
            Element element=elements.get(i)
            String href=element.attr("href")
            String href1=href.substring(1)
            context.data.put("href",href1)
            context.data.put("name",element.attr("data-campaign-name"))

            StepContext newContext=StepContext.derive(context);
            newContext.setCurrentStep("Card")
            newContext.setUrl("https://www.seedrs.com"+href)
            context.getNewStepContexts().add(newContext)
        }


    }
}
