package com.yarrumretep.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class TestMetaClass
{
	@Test
	public void testSuperclass()
	{
		MetaClass mclass = MetaClass.lookup(Concrete.class);
		assertEquals("Should be no parameters", 0, mclass.getParameters().size());
		assertEquals("Super should have two", MetaClass.lookupAll(Integer.class, String.class), mclass.getSuperClass().getParameters());
		assertEquals("Super should be Subtype<Integer, String>", MetaClass.lookup(Subtype.class, Integer.class, String.class), mclass.getSuperClass());
		assertEquals("Super super should have one", MetaClass.lookupAll(Integer.class), mclass.getSuperClass().getSuperClass().getParameters());
		assertEquals("Super super should be Supertype<Integer>", MetaClass.lookup(Supertype.class, Integer.class), mclass.getSuperClass().getSuperClass());
	}

	@Test
	public void testMethods()
	{
		MetaClass mclass = MetaClass.lookup(Concrete.class);
		MetaMethod mmethod;

		mmethod = mclass.getMethod("getX");
		assertNotNull("Should have getX()", mmethod);
		assertEquals("Should return String", MetaClass.lookup(String.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getSubtypeThing");
		assertNotNull("Should have getSubtypeThing()", mmethod);
		assertEquals("Should return Integer", MetaClass.lookup(Integer.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getThing");
		assertNotNull("Should have getThing()", mmethod);
		assertEquals("Should return Integer", MetaClass.lookup(Integer.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getList", Object.class);
		assertNotNull("Should have getList(Object)", mmethod);
		assertEquals("Should return List<Integer>", MetaClass.lookup(List.class, Integer.class), mmethod.getReturnType());
		assertEquals("Should have args of String", Collections.singletonList(MetaClass.lookup(String.class)), mmethod.getArguments());
	}

	@Test
	public void testFields()
	{
		MetaClass mclass = MetaClass.lookup(Concrete.class);
		MetaField mfield;

		mfield = mclass.getField("x");
		assertNotNull("Should have x", mfield);
		assertEquals("Should return String", MetaClass.lookup(String.class), mfield.getType());

		mfield = mclass.getField("thing");
		assertNotNull("Should have thing", mfield);
		assertEquals("Should return Integer", MetaClass.lookup(Integer.class), mfield.getType());
	}

	@Test
	public void testMethods2()
	{
		MetaClass mclass = MetaClass.lookup(Subtype.class, Double.class, Date.class);
		MetaMethod mmethod;

		mmethod = mclass.getMethod("getX");
		assertNotNull("Should have getX()", mmethod);
		assertEquals("Should return Date", MetaClass.lookup(Date.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getSubtypeThing");
		assertNotNull("Should have getSubtypeThing()", mmethod);
		assertEquals("Should return Double", MetaClass.lookup(Double.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getThing");
		assertNotNull("Should have getThing()", mmethod);
		assertEquals("Should return Double", MetaClass.lookup(Double.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getList", Object.class);
		assertNotNull("Should have getList(Object)", mmethod);
		assertEquals("Should return List<Double>", MetaClass.lookup(List.class, Double.class), mmethod.getReturnType());
		assertEquals("Should have args of Date", Collections.singletonList(MetaClass.lookup(Date.class)), mmethod.getArguments());
	}

	@Test
	public void testFields2()
	{
		MetaClass mclass = MetaClass.lookup(Subtype.class, Double.class, Date.class);
		MetaField mfield;

		mfield = mclass.getField("x");
		assertNotNull("Should have x", mfield);
		assertEquals("Should return Date", MetaClass.lookup(Date.class), mfield.getType());

		mfield = mclass.getField("thing");
		assertNotNull("Should have thing", mfield);
		assertEquals("Should return Double", MetaClass.lookup(Double.class), mfield.getType());
	}

}
