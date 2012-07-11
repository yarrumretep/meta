package com.yarrumretep.meta;

public class Supertype <T>
{
	private final T thing;
	
	public Supertype(T thing)
	{
		this.thing = thing;
	}
	
	public T getThing()
	{
		return thing;	
	}
}
