
package wcs.java.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import COM.FutureTense.Interfaces.IList;

/**
 * Mocked implementation of an IList useful for adding in the environment for
 * tests
 * 
 * You can build it easily with a List<String>.
 * 
 * TestElement provides a cols(String...args) method so you can set it
 * env().setList("List", cols("a", "1", "2"), cols("b", "3", "4")
 * 
 * @author msciab
 * 
 */
public class MapIList implements IList {

	private String name;
	private Map<String, List<String>> map;
	private int curRow = 0;
	private String[] columns = new String[1];
	private int _numRows = 0;
	private boolean _hasData = false;
	private int _numColumns = 1;

	public MapIList(String name, Map<String, List<String>> map) {
		this.name = name;
		this.map = map;
		if (!map.isEmpty()) {
			columns = map.keySet().toArray(columns);
			Arrays.sort(columns);
			_numRows = Integer.MAX_VALUE;
			for (Object o : map.values()) {
				List<?> l = (List<?>) o;
				_numRows = Math.min(_numRows, l.size());
			}
			_hasData = true;
			_numColumns = columns.length;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasData() {
		return _hasData;
	}

	@Override
	public int numColumns() {
		return _numColumns;
	}

	@Override
	public String getValue(String arg0) throws NoSuchFieldException {
		List<String> list = map.get(arg0);
		String value = null;
		if (list != null)
			value = list.get(curRow);

		if (list == null || value == null)
			throw new NoSuchFieldException(arg0);

		return value;
	}

	@Override
	public Object getObject(String arg0) {
		throw new RuntimeException("getObject not implemented");
	}

	@Override
	public byte[] getFileData(String arg0) {
		throw new RuntimeException("getFileData not implemented");
	}

	@Override
	public String getFileString(String arg0) throws NoSuchFieldException {
		throw new RuntimeException("getFileData not implemented");
	}

	@Override
	public void flush() {
	}

	@Override
	public String getColumnName(int arg0) {
		if (arg0 < 1 || columns.length == 0)
			return null;
		//System.out.println(columns.length);
		return arg0 <= columns.length  ? columns[arg0 - 1] : null;
	}

	@Override
	public int numRows() {
		return _numRows;
	}

	// note rows are numbered from 1 to lastRow
	@Override
	public boolean moveTo(int row) {
		if (row <= _numRows) {
			curRow = row - 1;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean atEnd() {
		return curRow == _numRows;
	}

	@Override
	public boolean moveToRow(int arg0, int arg1) {
		throw new RuntimeException("moveToRow not implemented");
	}

	@Override
	public int numIndirectColumns() {
		return 0;
	}

	@Override
	public String getIndirectColumnName(int arg0) {
		return null;
	}

	@Override
	public IList clone(String arg0) {
		throw new RuntimeException("clone not implemented");
	}

	@Override
	public void rename(String arg0) {
		throw new RuntimeException("rename not implemented");
	}

	@Override
	public boolean stringInList(String arg0) {
		throw new RuntimeException("stringInList not implemented");
	}

	@Override
	public String toString() {
		return String.format("StubIList(%s : %s", name, map.toString());
	}

	@Override
	public int currentRow() {
		return curRow;
	}

}
