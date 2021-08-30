import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HTTPEcho {
  // initialize size of buffer to 1024 bytes
  private static int BUFFERSIZE = 16;


  public static void main( String[] args) {

    try {
      int portnr = Integer.parseInt(args[0]);
      ServerSocket welcomeSocket = new ServerSocket(portnr);
      String decodedString;
      String response;

      while(true) {
        Socket connectionSocket = welcomeSocket.accept();
        connectionSocket.setSoTimeout(10000);
        StringBuilder builder = new StringBuilder();
        byte[] fromClientBuffer = new byte[BUFFERSIZE];
        int fromClientLength;

        try{
          while((fromClientLength = connectionSocket.getInputStream().read(fromClientBuffer)) > 0) {
            decodedString = new String(fromClientBuffer, 0, fromClientLength, StandardCharsets.UTF_8);
            builder.append(decodedString);
          }
        } catch(IOException e) {
        }
        response = builder.toString();

        byte[] toClientBuffer = response.getBytes(StandardCharsets.UTF_8);

        connectionSocket.getOutputStream().write(toClientBuffer);
        connectionSocket.close();
      }

    } catch(IOException ex) {
        System.err.println(ex);
        System.exit(1);
    }


  }
}
