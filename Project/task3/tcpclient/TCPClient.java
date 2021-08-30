package tcpclient;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TCPClient {

  // initialize size of buffer to 1024 bytes
  private static int BUFFERSIZE = 1024;

  // with 3rd argument
  public static String askServer(String hostname, int port, String ToServer) throws IOException {
    // allocate buffers
    byte[] fromUserBuffer = new byte[BUFFERSIZE];
    byte[] fromServerBuffer = new byte[BUFFERSIZE];
    StringBuilder result = new StringBuilder();
    String decodedString;

    // create socket that will open a connection to server
    Socket clientSocket = new Socket(hostname, port);
    //clientSocket.setSoTimeout(12000);
    // encode string into bytes
    fromUserBuffer = ToServer.getBytes(StandardCharsets.UTF_8);

    // write to output stream
    clientSocket.getOutputStream().write(fromUserBuffer, 0, fromUserBuffer.length);

    int fromServerLength;

    try{
      while((fromServerLength = clientSocket.getInputStream().read(fromServerBuffer)) > 0) {
        decodedString = new String(fromServerBuffer, 0, fromServerLength, StandardCharsets.UTF_8);
        result.append(decodedString);
      }
    } catch(IOException e) {
      clientSocket.close();
    }


    clientSocket.close();

    return result.toString();
  }

  // without 3rd argument
  public static String askServer(String hostname, int port) throws IOException {
    // allocate buffers
    byte[] fromServerBuffer = new byte[BUFFERSIZE];
    StringBuilder result = new StringBuilder();
    String decodedString;

    // create socket that will open a connection to server
    Socket clientSocket = new Socket(hostname, port);
    //clientSocket.setSoTimeout(2000);

    int fromServerLength;

    try{
      while((fromServerLength = clientSocket.getInputStream().read(fromServerBuffer)) > 0) {
        decodedString = new String(fromServerBuffer, 0, fromServerLength, StandardCharsets.UTF_8);
        result.append(decodedString);
      }
    } catch(IOException e) {
      clientSocket.close();
    }


    clientSocket.close();

    return result.toString();
  }
}
