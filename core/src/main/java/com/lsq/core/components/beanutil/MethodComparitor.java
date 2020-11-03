package com.lsq.core.components.beanutil;

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodComparitor implements Comparator<Method> {

	@Override
	public int compare(Method arg0, Method arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}

}
