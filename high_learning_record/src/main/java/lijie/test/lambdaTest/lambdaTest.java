package lijie.test.lambdaTest;

import java.util.Comparator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * @author : lj
 * @since : 2021/1/5
 *
 * ->　左边lambda　表达式的参数列表
 * ->　右边lambda　表达式中所需执行的功能也叫lambda体
 *
 * 语法格式一：　无参。无返回值
 * ()-> System.out.println(“你好”);
 *
 * 语法格式二：　有一个参。无返回值
 *  (x)-> System.out.println(x);
 *
 * 语法格式三：　若只有一个参。小括号可以不写
 *  x-> System.out.println(“你好”);
 *
 *
 * 语法格式四：　有两个以上参。有返回值,并且Lambda 体中有多条语句
 *   Comparator<Integer> comparator=(x,y)->{
 *             System.out.println("你好");
 *             return Integer.compare(x,y);
 *         };
 *
 * 语法格式五：　若Lambda 体中只有一条语句　return 和大括号都可以不写。
 *    Comparator<Integer> comparator=(x,y)->Integer.compare(x,y);
 *
 *
 * 语法格式五：　Lambda表达式的参数的数据类型可以不写,java编译器可以根据上下文推出，即"类型推断"
 *    Comparator<Integer> comparator=(Integer x,Integer y)->Integer.compare(x,y);
 *
 *
 * 左右遇一可以省
 * 左侧推断类型省
 *
 * lambda表达式需要函数式接口的支持
 * 函数式接口：接口中只有一个抽象方法的接口叫函数式接口。可以使用@FunctionalInterface 修饰　可以检查是否是函数式接口
 *
 *
 */

public class lambdaTest {
    public static void main(String[] args) {
        synchronized (lambdaTest.class){

        }
        Lock lock=new ReentrantLock();
//       Runnable runnable=new Runnable() {
//           @Override
//           public void run() {
//               System.out.println("你好");
//           }
//       };
//
        System.out.println("------------------------------------");

        Runnable runnable1=()-> System.out.println("你好");
        Thread thread=new Thread(runnable1);
        thread.start();


    }

//    public static void main(String[] args) {
//        Consumer<String> consumer=(x)-> System.out.println(x);
//        consumer.accept("你好");
//    }


//    public static void main(String[] args) {
//        Comparator<Integer> comparator=(x,y)->{
//            System.out.println("你好");
//            return Integer.compare(x,y);
//        };
//    }

//public static void main(String[] args) {
//    Comparator<Integer> comparator=(x,y)->Integer.compare(x,y);
//}


//    public static void main(String[] args) {
//       int num= operation(100,(x)->x*x);
//        System.out.println(num);
//    }
//
//    public static Integer operation(Integer num, Myfaun myfaun){
//        return myfaun.getValue(num);
//    }
}
