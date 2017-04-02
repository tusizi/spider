package utils;

import agent.UserAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Channel;
import entity.Search;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtils {

    //定义JSON反序列化类型,为什么定义Result 嵌套B,因为我获取的JSON结构如下
    //必须有set 和 get方法

    public static Channel getChannel(String url) {
        return get(url, Channel.class);
    }

    public static Search getSearch(String url) {
        return get(url, Search.class);
    }

    public static <T> T get(String url, Class<T> clazz) {
        try {

            //创建HTTP实例
            CloseableHttpClient httpClient = new DefaultHttpClient();

            //设置请求地址
            HttpGet getRequest = new HttpGet(url);

            //设置请求类型
            getRequest.addHeader("accept", "application/json");
            String randomUserAgent = UserAgent.getRandomUserAgent();
//            System.out.println("getChannel by " + randomUserAgent);
            getRequest.addHeader("User-Agent", randomUserAgent);

            //执行请求
            HttpResponse response = httpClient.execute(getRequest);

            //判断状态如果不等于200就是有异常
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            //定义JSON解析,解析出自己想要的东西
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(EntityUtils.toString(response.getEntity()), clazz);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
