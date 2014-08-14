package setup;
import java.io.BufferedReader;
import java.io.FileReader;

/**
Scan the log unti it finds that you have to press ENTER.
*/

class WaitUntil  {
  public static void main(String[] args) throws Exception {
     // read input
     FileReader is = new FileReader(args[0]);
     BufferedReader br = new BufferedReader(is);
     String line = "";
     boolean found = false;
     while(! found) {
         line = br.readLine();
         if(line==null) continue;
         if(line.endsWith(args[1])) {
            found = true;
            System.err.println("!"+line);
         } else  {              
            System.err.println(">"+line);
         }
     } 
     is.close();
     System.exit(0);
  }
}