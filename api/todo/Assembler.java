package wcs.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import com.fatwire.cs.core.uri.Definition;
import com.fatwire.cs.core.uri.Definition.ContainerType;

public class Assembler implements com.fatwire.cs.core.uri.Assembler {

	@Override
	public URI assemble(Definition def) throws URISyntaxException {
		System.out.println("assemble" + def);
		return null;
	}

	@Override
	public Definition disassemble(URI uri, ContainerType ctype)
			throws URISyntaxException {
		System.out.println("disassemble" + uri + "(in " + ctype + ")");
		return null;
	}

	@Override
	public void setProperties(Properties prp) {
		System.out.println(prp);

	}

}
