package lijie.test.interruptDemo;//package lijie.test.interruptDemo;
//
//import net.openhft.affinity.Affinity;
//
//public class DaemonDemo {
//    public static void main(String[] args) {
//        Thread daemonThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        System.out.println("i am alive");
//                        Affinity.setAffinity(4);//第7个cpu
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        System.out.println("finally block");
//                    }
//                }
//            }
//        });
////        daemonThread.setDaemon(true);
//        daemonThread.start();
//        //确保main线程结束前能给daemonThread能够分到时间片
//        while (true){
//            System.out.println("ssdssssss");
//        }
//    }
//}
