package setup;

import static java.lang.System.out;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class DetectConfig {

	Properties iniFile = null;
	String webApp = null;
	String version = null;
	String jarFile = null;
	String url = null;
	String user = "fwadmin";
	String password = "xceladmin";

	private void scan(File file) {
		for (File son : file.listFiles()) {
			// out.println(""+son);
			if (son.isDirectory()) {
				out.print(".");
				scan(son);
			} else if (son.getName().equals("omii.ini")) {
				out.print("#");
				iniFile = new Properties();
				try {
					iniFile.load(new FileReader(son.getAbsolutePath()));

					if (iniFile.getProperty("CSInstallAppServerType")
							.startsWith("tomcat")) {
						webApp = iniFile.getProperty("CSInstallAppServerPath");
						if (!webApp.endsWith("/"))
							webApp = webApp + "/";
						webApp = webApp + "webapps"
								+ iniFile.getProperty("sCgiPath");
					}

					url = iniFile.getProperty("CSConnectString");
					if (url != null && url.endsWith("/"))
						url = url.substring(0, url.length() - 1);

				} catch (Exception e) {
					iniFile = null;
					e.printStackTrace();
				}
			} else if (son.getName().equals("omproduct.ini")) {
				out.print("#");
				try {
					Properties productFile = new Properties();
					productFile.load(new FileReader(son.getAbsolutePath()));
					version = productFile.getProperty("Version0");
				} catch (Exception e) {
					iniFile = null;
					e.printStackTrace();
				}
			} else if (son.getName().endsWith(".jar")
					&& son.getName().startsWith("csdt-client")) {
				out.print("#");
				jarFile = son.getAbsolutePath();
				// heuristic to choose the right version
				if (jarFile.endsWith("-1.2.2.jar")
						|| jarFile.endsWith("-1.2.jar"))
					version = "11.1.1.6.0";
				if (jarFile.endsWith("-11.1.1.8.0.jar"))
					version = "11.1.1.8.0";
			}
		}
	}

	private void configure() {

		Properties prp = new Properties();
		try {
			prp.setProperty("sites.url", url);
			prp.setProperty("sites.user",
					iniFile.getProperty("CSInstallAppName"));
			prp.setProperty("sites.password", password);
			prp.setProperty("sites.home",
					iniFile.getProperty("CSFTAppServerRoot"));
			prp.setProperty("sites.shared",
					iniFile.getProperty("CSInstallSharedDirectory"));
			prp.setProperty("sites.port",
					iniFile.getProperty("CSInstallWebServerPort"));
			prp.setProperty("sites.host", iniFile.getProperty("hostname"));
			prp.setProperty("sites.webapp", webApp);
			prp.setProperty("sites.csdt.jar", jarFile);
			prp.setProperty("sites.version", version);

			FileWriter fw = new FileWriter("agilesites.properties");
			prp.store(fw, "Detected by AgileSites installer on "
					+ new java.util.Date());
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println();
		for (java.util.Map.Entry<Object, Object> e : prp.entrySet())
			System.out.println(e.toString());
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("usage: <site home directory>");
		} else {
			File home = new File(args[0]);
			DetectConfig dc = new DetectConfig();
			dc.scan(home);
			dc.configure();
		}
	}

}
