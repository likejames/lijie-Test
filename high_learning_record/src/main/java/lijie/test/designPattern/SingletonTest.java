package lijie.test.designPattern;

/**
 * 设计模式测试
 * 单列模式(饿汉)
 */
public class SingletonTest {

    private static SingletonTest singletonTest = new SingletonTest();

    //不能被外部类实例化
    private SingletonTest() {

    }

    //此方法用于被外部调用获取实例
    private static SingletonTest getInstance() {
        return singletonTest;
    }
}

/**
 * 单列模式(懒汉)
 */
class Singleton {
    private static volatile Singleton singleton = null;

    //构造方法
    private Singleton() {

    }

    public static Singleton getInstance() {
        if (singleton == null) {  //判断为空吗,不为空的话实例化(由于单例模式只需要创建一次实例，如果后面再次调用getInstance方法时，则直接返回之前创建的实例，因此大部分时间不需要执行同步方法里面的代码，大大提高了性能。如果不加第一次校验的话，那跟上面的懒汉模式没什么区别，每次都要去竞争锁。)
            synchronized (Singleton.class) {
                if (singleton == null) {    //第二次判断防止当多线程操作的时候会引发错误(如果没有第二次校验，假设线程t1执行了第一次校验后，判断为null，这时t2也获取了CPU执行权，也执行了第一次校验，判断也为null。接下来t2获得锁，创建实例。这时t1又获得CPU执行权，由于之前已经进行了第一次校验，结果为null（不会再次判断），获得锁后，直接创建实例。结果就会导致创建多个实例。所以需要在同步代码里面进行第二次校验，如果实例为空，则进行创建)
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

}
