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
	
	public <A extends X> void foo(A foo)
	{
		
	}
	
	public T getSubtypeThing()
	{
		return super.getThing(); 
	}
	
	public X getX()
	{
		return x;
	}
}
