package agilesites.build.util

import org.apache.catalina.startup.Tomcat
import java.io.File
import javax.naming.Context
import java.net.ServerSocket
import java.net.Socket

class EmbeddedTomcat(port: Int, base: File, contexts: Tuple2[String, String]*) {

  val tomcat = new Tomcat();
  tomcat.setPort(port);
  tomcat.setBaseDir(base.getAbsolutePath());
  tomcat.enableNaming();

  for ((key, path) <- contexts) {

    val ctx = if (key.startsWith("/")) key else "/" + key

    val filepath = new File(path);
    if (filepath.exists()) {
      System.out.println("* " + ctx + " -> " + path);
      val context = tomcat.addWebapp(tomcat.getHost(), ctx, path);
      val config = new File(new File(filepath, "META-INF"), "context.xml");
      if (config.exists()) {
        val url = config.toURI().toURL();
        context.setConfigFile(url);
        System.out.println("** with context.xml");
      }
    }
  }

  def start() = {
    // stopping socket
    val thread = new Thread() {
      override def run() {
        try {
          val serv = new ServerSocket(port + 1);
          val sock = serv.accept();
          tomcat.stop();
          sock.getOutputStream().write('\n');
          sock.close();
        } catch {
          case ex: Exception =>
            ex.printStackTrace();
        }
        System.exit(0);
      }
    }

    thread.start();
    tomcat.start();
    tomcat.getServer().await();
  }

}