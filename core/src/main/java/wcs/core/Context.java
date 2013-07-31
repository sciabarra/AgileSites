package wcs.core;

/**
 * Created with IntelliJ IDEA.
 * User: jelerak
 * Date: 26/06/13
 * Time: 14.20
 * To change this template use File | Settings | File Templates.
 */
public interface Context {

    public void initContext(ClassLoader cl);

    public Object getBean(String classname);
}
