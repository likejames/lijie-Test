
package lijie.test.reentrantLock;

public class MyThread40 extends Thread {
    public static void main(String[] args) throws Exception {
        ThreadDomain40 td = new ThreadDomain40();
        MyThread40 mt = new MyThread40(td);
        mt.start();
        Thread.sleep(3000);
        td.signal();
    }

    private ThreadDomain40 td;

    public MyThread40(ThreadDomain40 td) {
        this.td = td;
    }

    public void run() {
        td.await();
    }
}