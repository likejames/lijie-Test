package lijie.test.test;

/**
 * @author : lj
 * @since : 2021/2/1
 */
public class Son extends Person {
    @Override
    public void see() {
        super.see();
        System.out.println("我是子类的方法");
    }

    public static void main(String[] args) {
        Person son=new Son();
        son.see();
    }
}
