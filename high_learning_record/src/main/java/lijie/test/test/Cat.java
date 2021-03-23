package lijie.test.test;

import java.util.*;

abstract class Animal{

abstract void say();

}
public class Cat extends Animal{


public Cat(){
System.out.printf("I am a cat");
}
    @Override
    void say() {

    }

    public static void main(String[] args) {
    String xx[]={"s","ss"};
    List<String> strings = Arrays.asList(xx);
    Iterator<String> iterable=strings.iterator();
    while (iterable.hasNext()){
        System.out.println(iterable.next());
    }
        xx = (String[]) strings.toArray();

        List<String> strings1 = Arrays.asList(xx);
        Iterator<String> iterable1=strings1.iterator();
        while (iterable1.hasNext()){
            System.out.println(iterable1.next());
        }
    }

    public static String reverse(String originStr) {
        if(originStr == null || originStr.length() <= 1){
            return originStr;
        }else {
            String xx=originStr.substring(1);
            char yy=originStr.charAt(0);
            String cc=reverse(originStr.substring(1));
            String vv=cc + originStr.charAt(0);
            return vv;
        }
    }

}