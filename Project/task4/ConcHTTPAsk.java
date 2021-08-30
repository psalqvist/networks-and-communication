import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class ConcHTTPAsk {
    public static void main( String[] args) {

      try {

        int portnr = Integer.parseInt(args[0]);
        ServerSocket welcomeSocket = new ServerSocket(portnr);

        while(true) {
          Socket connectionSocket = welcomeSocket.accept();
          Thread t = new Thread(new MyRunnable(connectionSocket));
          t.start();
        }
      } catch(IOException ex) {
          System.err.println(ex);
          System.exit(1);
      }

    }
}
