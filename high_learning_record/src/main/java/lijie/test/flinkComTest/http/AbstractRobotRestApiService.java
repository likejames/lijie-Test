package lijie.test.flinkComTest.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractRobotRestApiService {

    private static Logger logger = LoggerFactory.getLogger(AbstractRobotRestApiService.class);

    protected String host;

    protected CloseableHttpClient httpClient;

    public AbstractRobotRestApiService(String host) {
        this.host = host;
        this.httpClient = HttpClients.custom().build();
    }

    /**
     * 执行GET请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    protected JSONObject exeHttpGet(String url) throws IOException {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        String responseJson = EntityUtils.toString(response.getEntity());
        JSONObject responseObj = JSON.parseObject(responseJson);

        logger.info("responseJson" + responseJson);
        httpClient.close();
        // 校验http返回结果
        if(this.checkResponse(responseObj)) return responseObj;
        return null;
    }

    /**
     * 执行POST请求
     *
     * @param httpPost
     * @return
     * @throws IOException
     */
    protected JSONObject exeHttpPost(HttpPost httpPost) throws IOException {
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String responseJson = EntityUtils.toString(response.getEntity());
        JSONObject responseObj = JSON.parseObject(responseJson);

        logger.info("responseJson" + responseJson);
        httpClient.close();
        // 校验http返回结果
        if(this.checkResponse(responseObj)) return responseObj;
        return null;
    }

    /**
     * 校验http返回结果
     *
     * @param responseObj
     * @return
     */
    protected boolean checkResponse(JSONObject responseObj){
        if(null != responseObj.get("errors")){
            return false;
        }
        return true;
    }

}
