package lijie.test.testSynchronized;

import java.util.*;

public class Test {
    private static HashMap<String, Integer> hashMap = new HashMap();

    public static void main(String[] args) {
        String s = "[2019-01-01 01:04:11] 456 https://www.xx.com/xxx/c"; //数据源
        String[] strings = s.split(" ");
        Test.put(strings[3]);
        Test.sort(hashMap);
    }

    public static void put(String strings) {
        String key = strings;
        if (hashMap.containsKey(key)) {  //判断是否有此key
            hashMap.put(key, hashMap.get(key) + 1);
        } else {
            hashMap.put(key, 1);
        }
    }

    //排序
    public static void sort(HashMap map) {
        int recode = 1;
        //注意 ArrayList<>() 括号里要传入map.entrySet()
        List<Map.Entry<String, Integer>> list = new ArrayList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();

            }
        });

        //注意这里遍历的是list，也就是我们将map.Entry放进了list，排序后的集合
        for (Map.Entry s : list) {
            if (recode == 3) {
                return;
            } else {
                recode = recode + 1;
                System.out.println("网站: " + s.getKey() + "  " + "网站的访问量:　" + s.getValue() + "\n");
            }
        }

    }

}
