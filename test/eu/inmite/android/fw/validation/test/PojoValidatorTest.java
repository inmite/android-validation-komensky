package eu.inmite.android.fw.validation.test;

import eu.inmite.android.fw.validation.PojoValidator;
import eu.inmite.android.fw.validation.exception.PojoValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Tomáš Kypta
 * @since 18/04/2013
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class PojoValidatorTest {

	private static class TestEntity1 {
		@NotNull
		private Integer val;

		private Integer getVal() {
			return val;
		}

		private void setVal(Integer val) {
			this.val = val;
		}
	}
	
	private static class TestEntity2 {
		private TestEntity1 ent;

		private TestEntity1 getEnt() {
			return ent;
		}

		private void setEnt(TestEntity1 ent) {
			this.ent = ent;
		}
	}
	
	private static class TestEntity3 {
		private List<TestEntity1> list;

		private List<TestEntity1> getList() {
			return list;
		}

		private void setList(List<TestEntity1> list) {
			this.list = list;
		}
	}

	private static class TestEntity4 {
		@Size(min = 1, max = 10)
		private String str;

		private String getStr() {
			return str;
		}

		private void setStr(String str) {
			this.str = str;
		}
	}

	private static class TestEntity5 {
		@Min(5L)
		@Max(10L)
		private long val;
		@Min(10L)
		@Max(20L)
		private BigDecimal bd;

		private long getVal() {
			return val;
		}

		private void setVal(long val) {
			this.val = val;
		}

		private BigDecimal getBd() {
			return bd;
		}

		private void setBd(BigDecimal bd) {
			this.bd = bd;
		}
	}
	
	@Test
	public void testValidateNotNull() {
		TestEntity1 entity1 = null;
		try {
			PojoValidator.validateEntity(entity1);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (PojoValidationException e) {
			fail();
		}

		entity1 = new TestEntity1();
		try {
			PojoValidator.validateEntity(entity1);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(1, map.size());
			assertEquals("val", map.keySet().iterator().next());
		}

		entity1.setVal(1);
		try {
			PojoValidator.validateEntity(entity1);
		} catch (PojoValidationException e) {
			fail();
		}
	}

	@Test
	public void testValidateSize() {
		TestEntity4 entity4 = new TestEntity4();
		try {
			PojoValidator.validateEntity(entity4);
		} catch (PojoValidationException e) {
			fail();
		}

		entity4.setStr("");
		try {
			PojoValidator.validateEntity(entity4);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(1, map.size());
			assertEquals("str", map.keySet().iterator().next());
		}

		entity4.setStr("12345678901");
		try {
			PojoValidator.validateEntity(entity4);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(1, map.size());
			assertEquals("str", map.keySet().iterator().next());
		}

		entity4.setStr("abcd");
		try {
			PojoValidator.validateEntity(entity4);
		} catch (PojoValidationException e) {
			fail();
		}
	}

	@Test
	public void testValidateMinMax() {
		TestEntity5 entity5 = new TestEntity5();
		entity5.setVal(1);
		entity5.setBd(BigDecimal.ONE);
		try {
			PojoValidator.validateEntity(entity5);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(2, map.size());
		}

		entity5.setVal(5);
		entity5.setBd(BigDecimal.ONE);
		try {
			PojoValidator.validateEntity(entity5);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(1, map.size());
			assertEquals("bd", map.keySet().iterator().next());
		}

		entity5.setVal(4);
		entity5.setBd(BigDecimal.TEN);
		try {
			PojoValidator.validateEntity(entity5);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(1, map.size());
			assertEquals("val", map.keySet().iterator().next());
		}

		entity5.setVal(11);
		entity5.setBd(BigDecimal.TEN);
		try {
			PojoValidator.validateEntity(entity5);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(1, map.size());
			assertEquals("val", map.keySet().iterator().next());
		}

		entity5.setVal(11);
		entity5.setBd(new BigDecimal(21));
		try {
			PojoValidator.validateEntity(entity5);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(2, map.size());
		}

		entity5.setVal(7);
		entity5.setBd(new BigDecimal(15));
		try {
			PojoValidator.validateEntity(entity5);
		} catch (PojoValidationException e) {
			fail();
		}
	}


	@Test
	public void testValidateSubItem() {
		TestEntity2 entity2 = new TestEntity2();
		try {
			PojoValidator.validateEntity(entity2);
		} catch (PojoValidationException e) {
			fail();
		}

		TestEntity1 ent1 = new TestEntity1();
		entity2.setEnt(ent1);
		try {
			PojoValidator.validateEntity(entity2);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(1, map.size());
			assertTrue(map.keySet().iterator().next().startsWith("ent."));
		}

		ent1.setVal(5);
		try {
			PojoValidator.validateEntity(entity2);
		} catch (PojoValidationException e) {
			fail();
		}
	}

	@Test
	public void testValidateSubItemList() {
		TestEntity3 entity3 = new TestEntity3();
		try {
			PojoValidator.validateEntity(entity3);
		} catch (PojoValidationException e) {
			fail();
		}


		List<TestEntity1> list = new ArrayList<TestEntity1>();
		TestEntity1 ent1 = new TestEntity1();
		list.add(ent1);

		entity3.setList(list);
		try {
			PojoValidator.validateEntity(entity3);
			fail();
		} catch (PojoValidationException e) {
			Map<String, List<String>> map = e.getInvalidFields();
			assertNotNull(map);
			assertEquals(1, map.size());
			assertTrue(map.keySet().iterator().next().startsWith("list[0]."));
		}

		ent1.setVal(2);
		try {
			PojoValidator.validateEntity(entity3);
		} catch (PojoValidationException e) {
			fail();
		}

		TestEntity1 ent1b = new TestEntity1();
		ent1b.setVal(3);
		list.add(ent1b);
		try {
			PojoValidator.validateEntity(entity3);
		} catch (PojoValidationException e) {
			fail();
		}
	}
}
