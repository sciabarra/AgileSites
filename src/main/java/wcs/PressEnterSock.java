package wcs;
import java.io.*;
import java.net.*;

/**
This class is an helper to control the PressEnter behaviour of a client on the standard output using a socket on localhost.
*/

class PressEnterSock  {

  public static void main(String[] args) throws Exception {
    if(args.length == 0) {
         ServerSocket serv = new ServerSocket(47364);
         Socket sock = serv.accept();
         sock.close();
         serv.close();
         System.out.println();
    } else {
      Socket sock = new Socket("127.0.0.1", 47364);
      sock.close();
    }
  }
}