import java.io.IOException;
import java.net.*;
import java.sql.*;

public class justInServer {


}

class JustInServerSocket   {

    private int portNum;
    private ServerSocket ourSocket;

    public JustInServerSocket(int port) {
        portNum = port;
        try {
            ourSocket = new ServerSocket(portNum);
        }
        catch (UnknownHostException e) {
            return;
        }
        catch (IOException ioe) {
            return;
        }


        try {
            ourSocket.accept();
        }
        catch (IOException ioe) {
            return;
        }


    }
}