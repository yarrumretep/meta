package com.yarrumretep.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MetaField extends MetaMember
{
	MetaField(MetaClass clazz, Field field)
	{
		super(clazz, field);
	}

	public Field getField()
	{
		return (Field) member;
	}

	public MetaClass getType()
	{
		return clazz.resolver.apply(getField().getGenericType());
	}

	public String toString()
	{
		int mod = member.getModifiers();
		return (((mod == 0) ? "" : (Modifier.toString(mod) + " ")) + getType() + " " + clazz + "." + member.getName());
	}
}
