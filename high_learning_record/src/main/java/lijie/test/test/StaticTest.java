package lijie.test.test;

/**
 * 加载顺序：静态块->构造块->构造方法
 */
public class StaticTest {
    public static void main(String[] args) {
        System.out.println("A");
        new StaticTest();
    }
    public StaticTest() {
 
        System.out.println("B");
 
    }
    {
 
        System.out.println("C");
 
    }
    static {
 
        System.out.println("D");
 
    }
}