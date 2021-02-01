package lijie.test.threadPoolTest;

import lijie.test.testSynchronized.Account;
import lijie.test.testSynchronized.PersonBean;

import java.util.concurrent.Future;

public class Test {
    public static void main(String[] args) {
        final Account account = new Account();
//        BMW bmw = new BMW();
//        Future future = ThreadPool.submit(bmw);
        for (int i = 0; i < 100; i++) {
            Runnable runnable = new Runnable() {
                public void run() {
                    // TODO Auto-generated method stub
                    account.withdraw(1);
//                    account.getBalance();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();

//            ThreadPool.submit(new PersonBean(i));
        }
    }
}
