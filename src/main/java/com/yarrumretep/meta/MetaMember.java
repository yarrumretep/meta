package com.yarrumretep.meta;

import java.lang.reflect.Member;

public abstract class MetaMember
{
	protected final MetaClass clazz;
	protected final Member member;

	protected MetaMember(MetaClass clazz, Member member)
	{
		this.clazz = clazz;
		this.member = member;
	}

	public MetaClass getMetaClass()
	{
		return clazz;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaMember other = (MetaMember) obj;
		if (member == null)
		{
			if (other.member != null)
				return false;
		}
		else if (!member.equals(other.member))
			return false;
		return true;
	}

}
