package wcs.core;

import COM.FutureTense.Interfaces.ICS;

/**
 * Setup interface. Dispatched will call this class to perform setup
 * 
 * @author msciab
 * 
 */
public interface Setup {
	public String exec(ICS ics, String site, String username, String password);
}
