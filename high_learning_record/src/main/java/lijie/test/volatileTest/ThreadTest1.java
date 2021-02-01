package lijie.test.volatileTest;

public class ThreadTest1 extends Thread {
    Thread thread;

    public ThreadTest1(Thread th) {
        this.thread = th;
    }

    @Override
    public void run() {
        thread.notify();

    }
}
