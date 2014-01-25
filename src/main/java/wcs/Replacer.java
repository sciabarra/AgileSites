package wcs;
import java.io.*;

class Replacer  {
   
  public static void main(String[] args) throws Exception {

    if(args.length !=2) {
      System.out.println("usage: <from> <to>");
      System.exit(0);
    }

    InputStreamReader in = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(in);
    String line = br.readLine();
    while(line!=null) {
      line = line.replace(args[0], args[1]);
      System.out.println(line);
      line = br.readLine();   
    }
  }

}