package lijie.test.threadPoolTest;

import java.util.concurrent.Callable;

public abstract class AbstractCat implements Callable {

    protected abstract void doService() throws Exception;

    public void say() {
        System.out.println("我在说话");
    }

    public Object call() throws Exception {
        say();
        doService();
        return null;
    }
}
