package wcs.hubby;

import wcs.core.Service;
import COM.FutureTense.Interfaces.ICS;

public class ServiceManager implements Service {

	long timestamp = System.currentTimeMillis();

	@Override
	public String start(ICS arg0) throws Exception {
		return "start " + timestamp;
	}

	@Override
	public String status(ICS arg0) throws Exception {
		return "status " + timestamp;
	}

	@Override
	public String stop(ICS arg0) throws Exception {
		return "stop " + timestamp;
	}

}
