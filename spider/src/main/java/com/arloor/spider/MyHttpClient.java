package com.arloor.spider;

import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;


/**
 * Created by arloor on 17-5-7.
 */

/**
 * todo:需要线程封闭
 */

public class MyHttpClient {
    private final CloseableHttpClient client;
    private final long waittime;
    private final int retryNum;

    /**
     * 构造方法
     * 使用STANDARD_STRICT的cookie策略
     */
    public MyHttpClient(long waittime, int retryNum) {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }
                }
        };

        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, trustAllCerts, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);

        PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager();
        // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
        poolConnManager.setMaxTotal(200);
        // 设置最大路由
        poolConnManager.setDefaultMaxPerRoute(200);

        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        this.client = HttpClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .setSSLSocketFactory(sslSocketFactory)
//                .setProxy(new HttpHost("localhost",8081))
                .build();
        this.waittime = waittime;
        this.retryNum = retryNum;
    }

    public MyHttpClient(long waittime) {
        this(waittime, 5);
    }

    public MyHttpClient() {
        this(500, 5);
    }



    public CloseableHttpResponse doRequest(HttpUriRequest request) {

        HttpUriRequest requestClone=request;
        //设置User-Agent
        requestClone.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36");
        try {
            boolean responseValid = false;
            //如果response不是200-300则重试retryNum
            CloseableHttpResponse response =null;
            int i = 0;
            while (!responseValid) {
                Thread.sleep(waittime);
                response=client.execute(requestClone);
                responseValid = response != null

                        //下面这个status需要在200-300,如果直接折秤200会有问题
                        //因为302 状态是登陆验证中经常出现的，githubdeno就因为这个错困扰了很久
                        &&response.getStatusLine().getStatusCode() < 400 && response.getStatusLine().getStatusCode() >= 200;
                if(!responseValid&&response!=null){
                    response.close();
                }
                if (i == retryNum) {
                    System.out.println("请求" + retryNum + "次仍然失败，放弃");
                    System.out.println("\n\n\n");
                    return null;
                }
                i++;
            }

            Thread.sleep(waittime);//线程等待waittime
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 关闭client
     * 每一个client都需要关闭
     */
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
