package com.yarrumretep.meta;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public enum Scope
{
	Instance(true), Static(false);

	private final boolean instance;

	Scope(boolean instance)
	{
		this.instance = instance;
	}

	public boolean check(Member target)
	{
		return ((target.getModifiers() & Modifier.STATIC) == 0) == instance;
	}
}
