package com.yarrumretep.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	@Test
	public void testTypeVariableBounds()
	{
		MetaClass mclass = MetaClass.lookup(Subtype.class);
		MetaMethod mmethod;

		mmethod = mclass.getMethod("getX");
		assertNotNull("Should have getX()", mmethod);
		assertEquals("Should return Object", MetaClass.lookup(Object.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getSubtypeThing");
		assertNotNull("Should have getSubtypeThing()", mmethod);
		assertEquals("Should return Number", MetaClass.lookup(Number.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getThing");
		assertNotNull("Should have getThing()", mmethod);
		assertEquals("Should return Number", MetaClass.lookup(Number.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mclass.getMethod("getList", Object.class);
		assertNotNull("Should have getList(Object)", mmethod);
		assertEquals("Should return List<Number>", MetaClass.lookup(List.class, Number.class), mmethod.getReturnType());
		assertEquals("Should have args of Object", Collections.singletonList(MetaClass.lookup(Object.class)), mmethod.getArguments());
	}

	@Test
	public void testParameterizedMethod()
	{
		MetaClass mclass = MetaClass.lookup(Subtype.class, MetaClass.lookup(Float.class), MetaClass.lookup(List.class, String.class));

		MetaMethod mmethod;
		mmethod = mclass.getMethod("getX");
		assertNotNull("Should have getX()", mmethod);
		assertEquals("Should return List<String>", MetaClass.lookup(List.class, String.class), mmethod.getReturnType());
		assertEquals("Should have no args", Collections.emptyList(), mmethod.getArguments());

		mmethod = mmethod.getReturnType().getMethod("get", int.class);
		assertNotNull("Should have get()", mmethod);
		assertEquals("Should return String", MetaClass.lookup(String.class), mmethod.getReturnType());
		assertEquals("Should have int args", MetaClass.lookupAll(int.class), mmethod.getArguments());

		mmethod = mclass.getMethod("foo", Object.class);
		assertNotNull("Should have foo()", mmethod);
		MetaClass arg = mmethod.getArguments().get(0);
		assertEquals("Should be List<String>", MetaClass.lookup(List.class, String.class), arg);
		assertEquals("Should return List<Float>", MetaClass.lookup(List.class, Float.class), mmethod.getReturnType());
	}

	@Test
	public void testNotFinding()
	{
		MetaClass mclass = MetaClass.lookup(Subtype.class, MetaClass.lookup(Float.class), MetaClass.lookup(List.class, String.class));
		assertNull("Should have no field", mclass.getField("notThere"));
		assertNull("Should have no method", mclass.getMethod("notThere"));
		assertNull("Should have no method", mclass.getMethod("foo", Integer.class));
		assertNull("Should have no method", mclass.getMethod(Scope.Static, null, "foo"));
		assertNull("Should have no method", mclass.getMethod(Scope.Instance, Visibility.Private, "foo"));
		assertNull("Should have no method", mclass.getMethod(Scope.Instance, Visibility.Protected, "foo"));
	}

	@Test
	public void testFindingAllFields()
	{
		MetaClass mclass = MetaClass.lookup(Subtype.class, MetaClass.lookup(Float.class), MetaClass.lookup(List.class, String.class));
		assertEquals("Should be 2", 2, mclass.getFields().size());
		assertEquals("Should be 1", 1, mclass.getFields(Scope.Instance, Visibility.Private).size());
	}

	@Test
	public void testFindingAllMethods()
	{
		MetaClass mclass = MetaClass.lookup(Subtype.class, MetaClass.lookup(Float.class), MetaClass.lookup(List.class, String.class));
		assertEquals("Should be 16", 16, mclass.getMethods().size());
		assertEquals("Should be 2", 4, mclass.getMethods(Scope.Instance, Visibility.Protected).size());
	}

	@Test
	public void testToString()
	{
		MetaClass mclass = MetaClass.lookup(Map.class, Integer.class, String.class);
		assertEquals("java.util.Map<java.lang.Integer, java.lang.String>", mclass.toString());
	}
	
	static abstract class MyList implements List<String>
	{
		
	}
	
	@Test
	public void testInterfaces()
	{
		MetaClass mclass = MetaClass.lookup(MyList.class);
		assertEquals("Should be 1", 1, mclass.getInterfaces().size());
		assertEquals("Should be List<String>", MetaClass.lookup(List.class, String.class), mclass.getInterfaces().get(0));
		assertEquals("Should be a List", List.class, mclass.getInterfaces().get(0).getRawClass());
	}
}
