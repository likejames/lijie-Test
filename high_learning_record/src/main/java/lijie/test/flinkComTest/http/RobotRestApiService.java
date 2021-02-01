package lijie.test.flinkComTest.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * 流程服务robot相关信息接口
 *
 */
public class RobotRestApiService extends AbstractRobotRestApiService {

    private String request;

    private String token;

    public RobotRestApiService(String host, String request, String token) {
        super(host);
        this.request = request;
        this.token = token;
    }

    /**
     * 请求jar包信息
     *
     * @return
     * @throws IOException
     */
    public Long getTotalRobotCnt() throws IOException{
        // 构建请求URL
        StringBuilder urlSb = new StringBuilder();
        urlSb.append("http://").append(host).append(this.request);
        // 执行http请求
        HttpPost httpPost = new HttpPost(urlSb.toString());
        JSONObject submitRequestParam = new JSONObject();
        submitRequestParam.put("token", this.token);
        StringEntity payload = new StringEntity(submitRequestParam.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(payload);
        JSONObject responseObj = exeHttpPost(httpPost);
        if(null == responseObj){
            return null;
        }
        // 返回结果
        return responseObj.getInteger("total").longValue();
    }

}
