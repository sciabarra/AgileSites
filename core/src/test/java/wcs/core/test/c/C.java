package wcs.core.test.c;

import java.io.IOException;
import java.nio.CharBuffer;

//this class is used to build the test b.jar under resources 

public class C implements Readable {

	@Override
	public int read(CharBuffer cb) throws IOException {
		return 'c';
	}

}
