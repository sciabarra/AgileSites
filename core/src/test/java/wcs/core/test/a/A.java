package wcs.core.test.a;

import COM.FutureTense.Interfaces.ICS;
import wcs.api.Env;

// this class is used to build the test a.jar under resources 

public class A implements wcs.api.Element {

	@Override
	public String apply(Env e) {		
		return "a";
	}

	@Override
	public String exec(ICS ics) {
		return "a";
	}

}
