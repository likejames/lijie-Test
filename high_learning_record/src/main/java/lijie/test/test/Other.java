package lijie.test.test;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Other {

    static List func(String str) {
        boolean code=true;
        List returnList=new ArrayList();
        StringBuffer result = new StringBuffer();
        List list=new ArrayList();
        if (str.length() == 1) {
            System.out.println("请输入合法字符串");
            return null;
        } else { // string length >= 2
            
            result.append(str.charAt(0));
            int c = 1;
            
            for (int i = 0; i < str.length() - 1; i++) {
                if (str.charAt(i) == str.charAt(i + 1)) {
                    if (code){
                        list.add(i);
                        code=false;
                    }
                    c++;
                } else {
                    if (c>=3){
                        list.add(i);
                        returnList.add(list);
                    }
                    list=new ArrayList();
                    result.append(Integer.toString(c).toCharArray());
                    result.append(str.charAt(i+1));
                    c = 1;
                    code=true;
                }
            }
            result.append(Integer.toString(c).toCharArray());
            return returnList;
        }
    }

    public static void main(String[] args) {
        String str = "叽叽汪汪汪喵喵喵喵喳喳";
        System.out.println(JSONObject.toJSONString(func(str)));
    }

}