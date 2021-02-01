package lijie.test.volatileTest;


import java.util.concurrent.CountDownLatch;

public class VolatileTest {

    public static void main(String[] args) {


    }
}

class Pserson {
    public boolean isTrue = true;

    public void see() {
        while (isTrue) {
            System.out.println("我正在死循环呢");
        }
    }
}
