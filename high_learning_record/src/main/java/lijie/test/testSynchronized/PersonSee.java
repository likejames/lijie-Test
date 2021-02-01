package lijie.test.testSynchronized;

public class PersonSee {

    public static synchronized void see(int i) throws InterruptedException {
        System.out.println("我正在看！: " + i);
        Thread.sleep(2000);
        System.out.println("我在睡觉了: " + i);
    }
}
