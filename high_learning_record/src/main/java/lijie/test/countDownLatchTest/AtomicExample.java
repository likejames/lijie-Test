package lijie.test.countDownLatchTest;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicExample {
    private AtomicInteger cnt = new AtomicInteger();

    public void add() {
        cnt.incrementAndGet();
    }

    public int get() {
        return cnt.get();
    }
}
