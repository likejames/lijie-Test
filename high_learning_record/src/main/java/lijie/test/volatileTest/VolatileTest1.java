package lijie.test.volatileTest;

public class VolatileTest1 {
    public static void main(String[] args) throws InterruptedException {
        Pserson1 pserson = new Pserson1();
        pserson.start();
        Thread.sleep(300);
        System.out.println("主线程名字是：　" + Thread.currentThread().getName() + "\n");
        pserson.isTrue = false;
        System.out.println("我开始结束循环了" + pserson.isTrue);
    }
}

class Pserson1 extends Thread {
     public volatile boolean isTrue = true;

    @Override
    public void run() {
        see();
    }

    public void see() {
        while (isTrue) {

        }
    }
}
