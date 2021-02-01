package lijie.test.threadPoolTest;

public class BMW extends AbstractCat {
    @Override
    protected void doService() throws Exception {
        System.out.println("我在线程池里面运行！！");
    }
}
