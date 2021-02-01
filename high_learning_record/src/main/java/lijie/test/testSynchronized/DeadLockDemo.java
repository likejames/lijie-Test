package lijie.test.testSynchronized;

public class DeadLockDemo {
    private static String resource_a = "A";
    private static String resource_b = "B";

    public static void main(String[] args) {
        deadLock();
    }

    public static void deadLock() {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (resource_a) {
                    System.out.println("Aget resource a");
                    try {
                        Thread.sleep(3000);
                        synchronized (resource_b) {
                            System.out.println("Aget resource b");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (resource_b) {
                    System.out.println("Bget resource b");
                    synchronized (resource_a) {
                        System.out.println("Bget resource a");
                    }
                }
            }
        });
        System.out.println("测试改造");
        System.out.println("测试改造");
        threadA.start();
        threadB.start();

    }
}
