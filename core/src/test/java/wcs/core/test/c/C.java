package wcs.core.test.c;

import COM.FutureTense.Interfaces.ICS;
import wcs.api.Env;

//this class is used to build the test c.jar under resources 

public class C implements wcs.api.Element {

	@Override
	public String apply(Env e) {
		return "c";
	}

	@Override
	public String exec(ICS ics) {
		return "c";
	}

}
