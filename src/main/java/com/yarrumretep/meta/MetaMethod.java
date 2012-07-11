package com.yarrumretep.meta;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class MetaMethod extends MetaMember
{
	MetaMethod(MetaClass clazz, Method method)
	{
		super(clazz, method);
	}

	public Method getMethod()
	{
		return (Method) member;
	}

	public MetaClass getReturnType()
	{
		return clazz.resolver.apply(getMethod().getGenericReturnType());
	}

	public List<MetaClass> getArguments()
	{
		return Lists.transform(Arrays.asList(getMethod().getGenericParameterTypes()), clazz.resolver);
	}

	public List<MetaClass> getExceptions()
	{
		return Lists.transform(Arrays.asList(getMethod().getGenericExceptionTypes()), clazz.resolver);
	}
}
