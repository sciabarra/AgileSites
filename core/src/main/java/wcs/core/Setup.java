package wcs.core;

/**
 * Setup interface. Dispatched will call this class to perform setup
 * 
 * @author msciab
 *
 */
public interface Setup {
	public String exec(String username, String password);
}
