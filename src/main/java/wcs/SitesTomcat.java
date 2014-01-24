package wcs;

import org.apache.catalina.startup.Tomcat;
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
		//if(args[i].startsWith("@"))
		//  tomcat.addContext(ctx, path);
		//if(args[i].startsWith("#")) 
		//  tomcat.addWebapp(ctx, path);
		System.out.println("*** "+ctx+" -> "+path);
		tomcat.addWebapp(ctx, path);
  	}

  	tomcat.start();
  	tomcat.getServer().await();

  }

}