package wcs.core.test.a;

import java.io.IOException;
import java.nio.CharBuffer;

// this class is used to build the test a.jar under resources 

public class A implements Readable {

	@Override
	public int read(CharBuffer arg0) throws IOException {

		return 'a';
	}

}
