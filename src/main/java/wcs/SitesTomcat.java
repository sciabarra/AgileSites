package wcs;

import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;

import java.io.*;

class SitesTomcat  {
   
  public static void main(String[] args) throws Exception {

  	if(args.length<2) {
  		System.out.println("usage: <port> <base> [<context>=<webapp>]*");
  		System.exit(0);
  	}

  	Tomcat tomcat = new Tomcat();

    tomcat.setPort(Integer.parseInt(args[0]));
    tomcat.setBaseDir(args[1]);
    tomcat.enableNaming();

  	for(int i = 2; i< args.length; i++) {
  		int p = args[i].indexOf("=");
  		if(p<0) {
  		  System.out.println("invalid "+args[i]);
  		  continue;
  	  } 
   		String ctx = args[i].substring(0,p);
  		if(!ctx.startsWith("/") && !ctx.equals(""))
  			ctx = "/"+ctx;
  		String path = args[i].substring(p+1);
   		System.out.println("*** "+ctx+" -> "+path);
		  Context context = tomcat.addWebapp(ctx, path);
      File config = new File(new File(new File(path), "META-INF"), "context.xml");
      if(config.exists()) {
         java.net.URL url = config.toURI().toURL();
         context.setConfigFile(url);
         System.out.println("FOUND "+ctx+" with "+url);
      } else {        
         System.out.println("not found "+ctx+" with "+config);
      }
  	}

  	tomcat.start();
  	tomcat.getServer().await();

  }

}