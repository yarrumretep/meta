package com.yarrumretep.meta;

import java.util.Collections;
import java.util.List;

public class Subtype<T extends Number, X> extends Supertype<T>
{
	final X x;
	
	public Subtype(T thing, X x)
	{
		super(thing);
		this.x = x;
	}

	public List<T> getList(X x)
	{
		return Collections.emptyList();
	}
	
	protected <A extends X> List<? extends T> foo(A foo)
	{
		return null;
	}
	
	public T getSubtypeThing()
	{
		return super.getThing(); 
	}
	
	protected X getX()
	{
		return x;
	}
}
