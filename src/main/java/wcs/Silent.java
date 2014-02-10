package wcs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;

public class Silent {

	private static String fix(File file) {
		return file.getAbsolutePath().replace(File.separatorChar, '/');
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		if (args.length != 4) {
			System.out
					.println("usage: <base> <base-ini> <install-ini> <output-ini>");
			System.exit(0);
		}
		Properties baseIni = new Properties();
		File baseFile = new File(args[0]);
		File baseIniFile = new File(args[1]);		
		File installIniFile = new File(args[2]);
		File outputIniFile = new File(args[3]);
		baseIni.load(new FileReader(baseIniFile));
		baseIni.setProperty("CSInstallDBDSN", "csDataSource");
		baseIni.setProperty("CASHostNameActual", "localhost");
		baseIni.setProperty("CSInstallDirectory",
				fix(new File(baseFile, "home")));
		baseIni.setProperty("CSFTAppServerRoot",
				fix(new File(baseFile, "home")));
		baseIni.setProperty("sCgiPath", "/cs/");
		baseIni.setProperty("CSInstallSharedDirectory", 
			    fix(new File(baseFile, "shared")));
		baseIni.setProperty("CSInstallWebServerPort", "8181");
		baseIni.setProperty("CASPortNumberLocal", "8181");
		baseIni.setProperty("CASHostName", "localhost");
		baseIni.setProperty("CSInstallDBDSN", "csDataSource");
		baseIni.setProperty("CASPortNumber", "8181");
		baseIni.setProperty("CASHostNameLocal", "127.0.0.1");
		baseIni.setProperty("CSInstallDatabaseType", "HSQLDB");

		// disable
		baseIni.setProperty("CommerceConnector", "false");
		baseIni.setProperty("Avisports", "false");
		for (Enumeration<?> e = baseIni.propertyNames(); 
			e.hasMoreElements();) {
			String p = e.nextElement().toString();
			if(p.startsWith("FS") || p.indexOf("Sample")!=-1) {
				baseIni.setProperty(p, "false");
				System.out.println(p+"=false");
			}
		}	

		// those are to make happy the configurator
		baseIni.setProperty("CSConnectString", "http://localhost:8181/cs");
		baseIni.setProperty("CSInstallAppName", "fwadmin");
		baseIni.setProperty("CSInstallAppServerPath", fix(baseFile));

		outputIniFile.delete();
		FileWriter ofw = new FileWriter(outputIniFile);
		baseIni.store(ofw, "AgileSites was here");
		ofw.close();
		System.out.println("wrote "+outputIniFile);
		// baseIni.store(new FileWriter(args[4]), "AgileSites was here");
		Properties installIni = new Properties();
		FileReader fr = new FileReader(installIniFile);
		installIni.load(fr);
		fr.close();
		installIni.setProperty("loadfile", outputIniFile.getAbsolutePath());
		FileWriter ifw = new FileWriter(installIniFile);
		installIni.store(ifw, "AgileSites was here");
		ifw.close();
		System.out.println("wrote "+installIniFile);
	}

}
