package com.lsq.core.components.beanutil;

import java.lang.reflect.Field;
import java.util.Comparator;

public class LabeledFieldComparitor implements Comparator<Field> {

	@Override
	public int compare(Field f1, Field f2) {
		FieldLabel anno1 = f1.getAnnotation(FieldLabel.class);
		FieldLabel anno2 = f2.getAnnotation(FieldLabel.class);
		String s1 = (anno1 != null) ? anno1.value() : f1.getName();
		String s2 = (anno2 != null) ? anno2.value() : f2.getName();
		int o1 = (anno1 != null) ? anno1.order() : Integer.MAX_VALUE;
		int o2 = (anno2 != null) ? anno2.order() : Integer.MAX_VALUE;
		int first = compareInt(o1, o2);
		if (first != 0)
			return first;
		else
			return s1.compareTo(s2);
	}

	int compareInt(int i, int j) {
		return (i == j) ? 0 : (i > j) ? 1 : -1;
	}
}
