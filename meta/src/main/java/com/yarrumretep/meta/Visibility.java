package com.yarrumretep.meta;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public enum Visibility
{
	Public(Modifier.PUBLIC), Private(Modifier.PRIVATE), Protected(Modifier.PROTECTED);

	private final int modifier;

	Visibility(int modifier)
	{
		this.modifier = modifier;
	}

	public boolean check(Member target)
	{
		return (target.getModifiers() & modifier) != 0;
	}
}
