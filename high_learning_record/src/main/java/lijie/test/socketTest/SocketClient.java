package lijie.test.socketTest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author : lj
 * @since : 2020/11/11
 */
public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("127.0.0.1",8080);
        OutputStream os = socket.getOutputStream();
        os.write("吃了嘛".getBytes());
        os.close();
        socket.close();
    }
}
