package lijie.test.testSynchronized;

public class TestSynchronized {
    public int number = 10;

    public void minus() {
        synchronized (TestSynchronized.class) {
            int count = 5;
            for (int i = 0; i < 5; i++) {
                number--;
                System.out.println(Thread.currentThread().getName() + " - " + number);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        }
    }

}