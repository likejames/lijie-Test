package lijie.test.test;//package lijie.test.test;
//
//import net.openhft.affinity.Affinity;
//import net.openhft.affinity.AffinityLock;
//import net.openhft.affinity.AffinityStrategies;
//import net.openhft.affinity.CpuLayout;
//
//import static net.openhft.affinity.AffinityStrategies.DIFFERENT_CORE;
//import static net.openhft.affinity.AffinityStrategies.DIFFERENT_SOCKET;
//
//// -Djava.library.path=/disk/test/iotest/lib
//public class MyAffinityTest {
//    public static void main(String... args) throws InterruptedException {
//        CpuLayout cpu = AffinityLock.cpuLayout();
//        System.out.println("cpus: " + cpu.cpus());
//        System.out.println("sockets: " + cpu.sockets());
//        System.out.println("coresPerSocket: " + cpu.coresPerSocket());
//        System.out.println("threadsPerCore: " + cpu.threadsPerCore());
//        new Thread(new SleepRunnable1(), "xxx").start();
//
//    }
//
//    static class SleepRunnable1 implements Runnable {
//        @Override
//        public void run() {
//            Affinity.setAffinity(4);//第7个cpu
//            while (true) {
//                System.out.println("11111111");
//            }
//        }
//    }
//}
