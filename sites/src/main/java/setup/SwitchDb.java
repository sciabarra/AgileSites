package setup;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class SwitchDb {
	public static void main(String args[]) throws Exception {

		if (args.length != 2) {
			System.out.println("usage: <sites-home> <db-type>");
		} else {
			File dir = new File(args[0]);
			File ini = new File(dir, "futuretense.ini");
			FileReader fr = new FileReader(ini);
			Properties prp = new Properties();
			prp.load(fr);
			fr.close();
			if (args[1].equals("HSQLDB")) {
				prp.setProperty("cc.bigint", "BIGINT");
				prp.setProperty("cc.bigtext", "LONGVARCHAR");
				prp.setProperty("cc.blob", "LONGVARBINARY");
				prp.setProperty("cc.datetime", "TIMESTAMP");
				prp.setProperty("cc.double", "FLOAT");
				prp.setProperty("cc.integer", "INTEGER");
				prp.setProperty("cc.maxvarcharsize", "2147483647");
				prp.setProperty("cc.null", "");
				prp.setProperty("cc.numeric", "NUMERIC");
				prp.setProperty("cc.primary", "PRIMARY KEY");
				prp.setProperty("cc.rename", "ALTER TABLE %1 RENAME TO %2");
				prp.setProperty("cc.smallint", "SMALLINT");
				prp.setProperty("cc.unique", "");
				prp.setProperty("cs.dbtype", "HSQLDB");
			} else {
				throw new Exception("unknown database type");
			}
			FileWriter fw = new FileWriter(ini);
			prp.store(fw, "");
			fw.close();
		}
	}

}
