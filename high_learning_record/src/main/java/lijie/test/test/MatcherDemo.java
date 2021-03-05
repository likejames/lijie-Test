package lijie.test.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MatcherDemo {
    public static void main(String[] args) {
        String[] strings = matcher("叽叽汪汪汪喵喵喵喵喳喳");
        for (int i = 0; i < strings.length; i++) {
            System.out.println(strings[i]);
        }
    }
    public static String[] matcher(String input) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("(.)\\1*");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        Collections.sort(list, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });
        return list.toArray(new String[0]);
    }
}