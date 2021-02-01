package lijie.test.jwtTest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;

import java.util.Map;

public class Test {

    public static void main(String[] args) {
        try {
            String token = JwtUtil.createToken("12345", "wangbo");
            System.out.println("token=" + token);
            //Thread.sleep(5000);
            Map<String, Claim> map = JwtUtil.verifyToken(token);
            //Map<String, Claim> map = JwtUtil.parseToken(token);
            //遍历
            for (Map.Entry<String, Claim> entry : map.entrySet()) {
                if (entry.getValue().asString() != null) {
                    System.out.println(entry.getKey() + "===" + entry.getValue().asString());
                } else {
                    System.out.println(entry.getKey() + "===" + entry.getValue().asDate());
                }
            }
            //项目
            System.out.println(JWT.decode(token).getAudience().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
