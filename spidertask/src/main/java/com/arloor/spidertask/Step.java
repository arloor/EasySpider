package com.arloor.spidertask;



import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public interface Step {
    HttpUriRequest createRequest(StepContext context);
    void doParse(CloseableHttpResponse response,StepContext context);
}
