package wcs.core.test.b;

import COM.FutureTense.Interfaces.ICS;
import wcs.api.Env;

//this class is used to build the test b.jar under resources 

public class B implements wcs.api.Element {

	@Override
	public String apply(Env e) {		
		return "b";
	}

	@Override
	public String exec(ICS ics) {
		return "b";
	}

}
