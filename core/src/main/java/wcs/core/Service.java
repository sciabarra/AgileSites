package wcs.core;

import COM.FutureTense.Interfaces.ICS;

/**
 * Service interface
 * 
 * @author msciab
 * 
 */
public interface Service {

	/**
	 * Starting a service
	 */
	public String start(ICS ics) throws Exception;

	/**
	 * Checking service status
	 */
	public String status(ICS ics) throws Exception;

	/**
	 * Stopping a service
	 */
	public String stop(ICS ics) throws Exception;

}
