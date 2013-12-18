package wcs.core.test.b;

import java.io.IOException;
import java.nio.CharBuffer;

//this class is used to build the test b.jar under resources 

public class B implements Readable {

	@Override
	public int read(CharBuffer cb) throws IOException {
		return 'b';
	}

	

}
