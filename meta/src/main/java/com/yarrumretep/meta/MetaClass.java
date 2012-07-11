package com.yarrumretep.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MetaClass
{
	public static final Function<Class<?>, MetaClass> toMetaClass = new Function<Class<?>, MetaClass>()
	{
		public MetaClass apply(Class<?> clazz)
		{
			return lookup(clazz);
		}
	};

	public static MetaClass lookup(Class<?> clazz)
	{
		return lookup(clazz, Collections.<MetaClass> emptyList());
	}

	public static List<MetaClass> lookupAll(Class<?>... classes)
	{
		return Lists.transform(Arrays.asList(classes), toMetaClass);
	}

	public static MetaClass lookup(Class<?> clazz, Class<?>... parameters)
	{
		return lookup(clazz, lookupAll(parameters));
	}

	public static MetaClass lookup(Class<?> clazz, MetaClass... parameters)
	{
		return lookup(clazz, Arrays.asList(parameters));
	}

	public static MetaClass lookup(Class<?> clazz, List<MetaClass> parameters)
	{
		return new MetaClass(clazz, parameters);
	}

	private final Class<?> clazz;
	private final List<MetaClass> parameters;
	private transient final Map<TypeVariable<?>, MetaClass> variables;
	private transient final MetaClass superclass;

	private MetaClass(Class<?> clazz, List<MetaClass> parameters)
	{
		this.clazz = clazz;
		this.parameters = Collections.unmodifiableList(Lists.newArrayList(parameters));
		this.variables = Maps.newHashMapWithExpectedSize(parameters.size());

		TypeVariable<?>[] vars = clazz.getTypeParameters();
		if (vars.length != parameters.size())
			throw new IllegalArgumentException("Incorrect number of parameters for class " + clazz + " was " + parameters.size() + " should have been " + vars.length);

		int ct = 0;
		for (TypeVariable<?> var : vars)
			variables.put(var, parameters.get(ct++));

		this.superclass = resolver.apply(clazz.getGenericSuperclass());
	}

	public Class<?> getRawClass()
	{
		return clazz;
	}

	final transient Function<Type, MetaClass> resolver = new Function<Type, MetaClass>()
	{
		public MetaClass apply(Type type)
		{
			if (type == null)
			{
				return null;
			}
			else if (type instanceof Class<?>)
			{
				return lookup((Class<?>) type);
			}
			else if (type instanceof TypeVariable<?>)
			{
				MetaClass result = variables.get(type);
				if (result == null)
					throw new RuntimeException("Could not resolve type variable: " + type + " in " + this);
				return result;
			}
			else if (type instanceof ParameterizedType)
			{
				ParameterizedType ptype = (ParameterizedType) type;
				List<MetaClass> parameters = Lists.newArrayList();
				for (Type param : ptype.getActualTypeArguments())
					parameters.add(apply(param));
				return lookup((Class<?>) ptype.getRawType(), parameters);
			}
			else if (type instanceof WildcardType)
			{
				WildcardType wtype = (WildcardType) type;
				return apply(wtype.getUpperBounds()[0]);
			}
			else
			{
				throw new RuntimeException("Unrecognized type: " + type);
			}
		}
	};

	public List<MetaClass> getParameters()
	{
		return parameters;
	}

	public List<MetaClass> getInterfaces()
	{
		return Lists.transform(Arrays.asList(clazz.getInterfaces()), resolver);
	}

	public MetaClass getSuperClass()
	{
		return superclass;
	}

	public MetaField getField(String field)
	{
		return getField(field, Scope.Instance, null);
	}

	public MetaField getField(String field, Scope scope, Visibility visibility)
	{
		try
		{
			Field f = clazz.getDeclaredField(field);
			if (check(f, scope, visibility))
				return new MetaField(this, f);
		}
		catch (NoSuchFieldException e)
		{
			if (superclass != null)
				return superclass.getField(field, scope, visibility);
		}
		return null;
	}

	public List<MetaField> getFields()
	{
		return getFields(Scope.Instance, null);
	}

	private boolean check(Member member, Scope scope, Visibility visibility)
	{
		return (scope == null || scope.check(member)) && (visibility == null || visibility.check(member));
	}

	public List<MetaField> getFields(Scope scope, Visibility visibility)
	{
		List<MetaField> list;
		if (superclass != null)
			list = superclass.getFields(scope, visibility);
		else
			list = Lists.newArrayList();
		for (Field f : clazz.getDeclaredFields())
		{
			if (check(f, scope, visibility))
				list.add(new MetaField(this, f));
		}
		return list;
	}

	public MetaMethod getMethod(String method, Class<?>... arguments)
	{
		return getMethod(Scope.Instance, null, method, arguments);
	}

	public MetaMethod getMethod(Scope scope, Visibility visibility, String method, Class<?>... arguments)
	{
		try
		{
			Method m = clazz.getDeclaredMethod(method, arguments);
			if (check(m, scope, visibility))
				return new MetaMethod(this, m);
		}
		catch (NoSuchMethodException e)
		{
			if (superclass != null)
				return superclass.getMethod(scope, visibility, method, arguments);
		}
		return null;
	}

	public List<MetaMethod> getMethods()
	{
		return getMethods(Scope.Instance, null);
	}

	public List<MetaMethod> getMethods(Scope scope, Visibility visibility)
	{
		List<MetaMethod> list;

		if (superclass != null)
			list = superclass.getMethods(scope, visibility);
		else
			list = Lists.newArrayList();

		for (Method method : clazz.getDeclaredMethods())
		{
			if (check(method, scope, visibility))
				list.add(new MetaMethod(this, method));
		}
		return list;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
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
		MetaClass other = (MetaClass) obj;
		if (clazz == null)
		{
			if (other.clazz != null)
				return false;
		}
		else if (!clazz.equals(other.clazz))
			return false;
		if (parameters == null)
		{
			if (other.parameters != null)
				return false;
		}
		else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		if (parameters.size() == 0)
		{
			return clazz.getName();
		}
		else
		{
			StringBuilder builder = new StringBuilder();
			builder.append(clazz.getName());
			builder.append("<");
			boolean first = true;
			for (MetaClass mc : parameters)
			{
				if (!first)
					builder.append(", ");
				builder.append(mc.toString());
			}
			builder.append(">");

			return builder.toString();
		}
	}
}
