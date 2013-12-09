package wcs.core;

import java.util.Iterator;

import wcs.api.Call;

/**
 * Return a sequence of Calls intermixed with in a sequence of non-Calls (
 * 
 * This class ease the rendering of the output with a jsp doing tag calls
 * 
 * 
 * For example "first Call(...) second Call(...) third"
 * 
 * The header is the prefix before a call. Note that if there era no more Calls,
 * the header is actually the footer.
 * 
 * The intended usage is
 * 
 * Sequencer seq = new Sequence(encoded);
 * 
 * output seq.header();
 * 
 * while(seq.hasNext()) {
 * 
 * Call call = seq.next();
 * 
 * output call print
 * 
 * seq.header();
 * 
 * }
 * 
 * @author msciab
 * 
 */
public class Sequencer implements Iterator<Call> {

	// separators
	private final static String SEP = Call.SEP;
	private final static String SEP2START = Call.SEP2START;
	private final static String SEP2END = Call.SEP2END;

	private final static int SEPlen = Call.SEP.length();
	private final static int SEP2STARTlen = Call.SEP2START.length();
	private final static int SEP2ENDlen = Call.SEP2END.length();

	private int start = -1;
	private String res = "";

	/**
	 * Build a sequencer decoding embedded calls
	 * 
	 * @param res
	 */
	public Sequencer(String res) {
		this.res = res;
		start = res.indexOf(SEP2START);
	}

	/**
	 * More calls?
	 */
	@Override
	public boolean hasNext() {
		return start != -1;
	}

	/**
	 * Next call
	 */
	@Override
	public Call next() {
		
		int end = res.indexOf(SEP+SEP2END, start + SEP2STARTlen);
		if (end == -1)
			end = res.length();

		String call = res.substring(start+SEP2STARTlen, end);
		res = res.substring(end + SEPlen+SEP2ENDlen);
		start = res.indexOf(SEP2START);

		return Call.decode(call);

	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the head of the current entry Please note that if there is not a
	 * next element the header is actually the footer.
	 * 
	 * @return
	 */
	public String header() {
		// System.out.println(start+":"+res);
		if (start != -1)
			return res.substring(0, start);
		else
			return res;
	}

}
