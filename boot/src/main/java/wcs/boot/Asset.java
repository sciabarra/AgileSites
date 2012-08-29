package wcs.boot;

import COM.FutureTense.Interfaces.FTValList;
import COM.FutureTense.Interfaces.ICS;

public class Asset {
	public static String load(ICS ics, String name, String type, String objectid) {
		FTValList args = new FTValList();

		System.out.print("Asset.Load name="+name+" type="+type+" objectid="+objectid+" => ");
	
		args.setValString("NAME", name);
		args.setValString("OBJECTID", objectid);
		args.setValString("TYPE", type);

		String _output = ics.runTag("ASSET.LOAD", args);
		
		System.out.println(_output);
		
		return _output == null ? "" : _output;
	}

	public static String get(ICS ics, String name, String field, String output) {
		FTValList args = new FTValList();

		System.out.print("Asset.Get name="+name+" field="+field+" output="+output+" => ");

		args.setValString("NAME", name);
		args.setValString("FIELD", field);
		args.setValString("OUTPUT", output);

		String _output = ics.runTag("ASSET.GET", args);
		System.out.println(_output);
		return _output == null ? "" : _output;
	}
}
