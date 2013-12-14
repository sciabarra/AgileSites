package wcs.api;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements an utility class range for 1 to N to iterate elements of a list
 * @author msciab
 *
 */
public class Range implements Iterable<Integer> {

	private final int stop;

	public Range(int stop) {
		this.stop = stop;
	}

	public Iterator<Integer> iterator() {

		return new Iterator<Integer>() {
			private Integer counter = 0;

			public boolean hasNext() {
				return (counter < stop);
			}

			public Integer next() {
				if (counter > stop)
					throw new NoSuchElementException();
				return ++counter;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public String toString() {
		return "1 .. "+stop;
	}
}
