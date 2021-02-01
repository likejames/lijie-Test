package lijie.test.reentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Singleton1 {
    private static Lock lock = new ReentrantLock(true);

    public static void test() {
        boolean b = false;
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "获取了锁");
            TimeUnit.MILLISECONDS.sleep(20000);

        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "被中断");
            e.printStackTrace();
        } finally {

            lock.unlock();

        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread("线程A") {
            @Override
            public void run() {
                test();
            }
        };
        Thread thread2 = new Thread("线程B") {
            @Override
            public void run() {
                test();
            }
        };
        thread1.start();
        thread2.start();
        new Thread(()-> System.out.println("ssss")).start();
    }

}
