package lijie.test.testSynchronized;

import java.util.concurrent.Callable;

public class PersonBean implements Callable {
    private int i;

    public PersonBean(int i) {
        this.i = i;
    }

    public Object call() throws Exception {
        PersonSee.see(i);
        return null;
    }
}
