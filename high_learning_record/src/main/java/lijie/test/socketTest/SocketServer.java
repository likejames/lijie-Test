package lijie.test.socketTest;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) throws Exception {
        //Server起一个插座（套接字）监听一个连接
        ServerSocket serverSocket = new ServerSocket(8888);
        //服务器端总不能停吧，写一个死循环
        while (true) {
            //serverSocket获得同意，再建立一个socket
            Socket socket = serverSocket.accept();
            InputStream inputStream=socket.getInputStream();
            byte [] bytes=new byte[1024];
            inputStream.read(bytes);
            System.out.println(new String(bytes));
            System.out.println("client is connect！！！！！");
        }
    }
}