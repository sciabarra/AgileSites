package setup;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

class SitesServer {

	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			System.out.println("usage: <port> <base> [<context>=<webapp>]*");
			System.exit(0);
		}

		final int port = Integer.parseInt(args[0]);
		final Tomcat tomcat = new Tomcat();
		String base = args[1];

		tomcat.setPort(port);
		tomcat.setBaseDir(base);
		tomcat.enableNaming();

		for (int i = 2; i < args.length; i++) {
			int p = args[i].indexOf("=");
			if (p < 0) {
				System.out.println("invalid " + args[i]);
				continue;
			}
			String ctx = args[i].substring(0, p);
			if (!ctx.startsWith("/") && !ctx.equals(""))
				ctx = "/" + ctx;
			String path = args[i].substring(p + 1);
			File filepath = new File(path);
			if (!filepath.exists())
				continue;
			System.out.println("* " + ctx + " -> " + path);
			Context context = tomcat.addWebapp(tomcat.getHost(), ctx, path);
			File config = new File(new File(filepath, "META-INF"),
					"context.xml");
			if (config.exists()) {
				java.net.URL url = config.toURI().toURL();
				context.setConfigFile(url);
				System.out.println("** with context.xml");
			}
		}

		// stopping socket
		new Thread() {
			public void run() {
				ServerSocket serv = null;
				try {
					serv = new ServerSocket(port + 1);
					Socket sock = serv.accept();
					tomcat.stop();
					sock.getOutputStream().write('\n');
					sock.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						serv.close();
					} catch (Exception ex) {
					}
				}
				System.exit(0);
			}
		}.start();
		tomcat.start();
		tomcat.getServer().await();
	}

}