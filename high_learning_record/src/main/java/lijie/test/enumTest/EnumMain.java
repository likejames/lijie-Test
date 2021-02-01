package lijie.test.enumTest;

/**
 * @author : lj
 * @since : 2021/2/1
 */
public class EnumMain {
    public static void main(String[] args) {
        String name = EnumTest.valueOf("RED").judge("小明");
        System.out.println(name);

    }
}
