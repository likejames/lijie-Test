package lijie.test.casTest;

import java.util.concurrent.atomic.AtomicInteger;

public class CASTest {
    volatile static AtomicInteger count = new AtomicInteger(0);

    public static void addOne() {
        int newValue = count.get()+1;
        count.compareAndSet(count.get(), newValue);
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    addOne();
                    System.out.println("线程1----" + count);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    addOne();
                    System.out.println("线程2----" + count);
                }
            }
        });
        thread.start();
        thread2.start();
    }
}