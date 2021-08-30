import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import tcpclient.TCPClient;

public class HTTPAsk {
    public static void main( String[] args) {

      /* Get HTTP GET request and store in String request */
      try {
        // To handle request
        String request;
        String decodedString;
        int portnr = Integer.parseInt(args[0]);
        ServerSocket welcomeSocket = new ServerSocket(portnr);

        // initialize size of buffer to 1024 bytes
        int BUFFERSIZE = 1024;


        while(true) {
          Socket connectionSocket = welcomeSocket.accept();
          connectionSocket.setSoTimeout(3000);
          StringBuilder builder_req = new StringBuilder();
          byte[] fromClientBuffer = new byte[BUFFERSIZE];
          int fromClientLength;

          try{
            while((fromClientLength = connectionSocket.getInputStream().read(fromClientBuffer)) > 0) {
              decodedString = new String(fromClientBuffer, 0, fromClientLength, StandardCharsets.UTF_8);
              builder_req.append(decodedString);
            }
          } catch(IOException e) {
          }

          /* This is our HTTP GET request */
          request = builder_req.toString();
          System.out.println(request + "\n");
          /* Parse out hostname, portnr and  */
          String[] requestArr = request.split(" ");

          // To build response
          String response = "";
          String statusLine;
          byte[] toClientBuffer = new byte[BUFFERSIZE];
          StringBuilder builder_res = new StringBuilder();

          // Is this a GET request?
          if(!requestArr[0].equals("GET") || !(requestArr[1].split("\\?")[0].equals("/ask"))
          || !(requestArr[2].split("\r")[0].equals("HTTP/1.1")) || !(requestArr[1].split("=")[0].split("\\?")[1].equals("hostname"))
          || !(requestArr[1].split("&")[1].split("=")[0].equals("port"))) {
            //System.out.println(requestArr[1].split("\\?")[0]);
            statusLine = "HTTP/1.1 400 Bad Request\r\n";

          } else {
            // else, we parse out portnr, hostname and data
            Integer port_res = Integer.parseInt(requestArr[1].split("&")[1].split("=")[1]);
            String hostname_res = requestArr[1].split("=")[1].split("&")[0];
            String data = null;
            if(requestArr[1].split("&").length >= 3) {
              if(requestArr[1].split("&")[2].split("=")[0].equals("string")) {
                data = requestArr[1].split("&")[2].split("=")[1] + "\n";
              } else {
                statusLine = "HTTP/1.1 400 Bad Request\r\n";
              }
            }
            if(data == null) {
              try {
                //System.out.println("1");
                statusLine = "HTTP/1.1 200 OK\r\n\r\n";
                response = TCPClient.askServer(hostname_res, port_res);
              } catch(IOException e) {
                System.out.println("2");
                statusLine = "HTTP/1.1 404 Not Found\r\n";
              }
            } else {
              try {
                //System.out.println("3");
                statusLine = "HTTP/1.1 200 OK\r\n\r\n";
                response = TCPClient.askServer(hostname_res, port_res, data);
              } catch(IOException e) {
                //System.out.println("4");
                statusLine = "HTTP/1.1 404 Not Found\r\n";
              }
            }
          }
          response = statusLine + response;
          System.out.println(response);

          toClientBuffer = response.getBytes(StandardCharsets.UTF_8);
          connectionSocket.getOutputStream().write(toClientBuffer);

          connectionSocket.close();

        }
      } catch(IOException ex) {
          System.err.println(ex);
          System.exit(1);
      }

    }
}
