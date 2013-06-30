package wcs.java.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testRetrieveTable() {
		
		assertEquals(
				Util.retrieveTable("select * from SystemUsers where id=1"),
				"SystemUsers");

		assertEquals(
				Util.retrieveTable("SELECT * from AssetPublication,SystemInfo WHERE id=1 and a=b"),
				"AssetPublication,SystemInfo");
		
		assertEquals(
				Util.retrieveTable("insert into SystemUsers value('fwadmin', '123')"),
				"SystemUsers");

		
		assertEquals(
				Util.retrieveTable(" UPDATE  SYSTEMUSERS   set  a=1   where id=2  "),
				"SYSTEMUSERS");

		
		assertEquals(
				Util.retrieveTable("delete from AssetPublication"),
				"AssetPublication");

		
	}

}
