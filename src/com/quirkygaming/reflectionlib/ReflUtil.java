package com.quirkygaming.reflectionlib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.quirkygaming.errorlib.ErrorHandler;

class ReflUtil {
	
	static <Ex extends Exception> AnonymousObject instantiate(Class<?> clazz, ErrorHandler<Ex> eh, Object... parameters) throws Ex {
		try {
			Object o;
			if (parameters.length == 0) {
				o = clazz.newInstance();
			} else {
				Constructor<?> c;
				Class<?>[] classes = getClassesFromParams(parameters);
				c = clazz.getConstructor(classes);
				o = c.newInstance(parameters);
			}
			return new AnonymousObject(o);
			
		} catch (IllegalAccessException e) {
			eh.handle(e);
		} catch (InstantiationException e) {
			eh.handle(e);
		} catch (NoSuchMethodException e) {
			eh.handle(e);
		} catch (SecurityException e) {
			eh.handle(e);
		} catch (IllegalArgumentException e) {
			eh.handle(e);
		} catch (InvocationTargetException e) {
			eh.handle(e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	static <T, Ex extends Exception> T getField(Class<?> clazz, Object o, ErrorHandler<Ex> eh, String fieldName) throws Ex {
		try {
			Field m = clazz.getField(fieldName);
			return (T) m.get(o);
		} catch (IllegalArgumentException e) {
			eh.handle(e);
		} catch (IllegalAccessException e) {
			eh.handle(e);
		} catch (ClassCastException e) {
			eh.handle(e);
		} catch (NoSuchFieldException e) {
			eh.handle(e);
		} catch (SecurityException e) {
			eh.handle(e);
		}
		return null;
	}
	
	static <Ex extends Exception> void setField(Class<?> clazz, Object o, ErrorHandler<Ex> eh, String fieldName, Object value) throws Ex {
		try {
			Field m = clazz.getField(fieldName);
			if (value != null && value instanceof AnonymousObject) {
				value = ((AnonymousObject)value).o;
			}
			m.set(o, value);
		} catch (IllegalArgumentException e) {
			eh.handle(e);
		} catch (IllegalAccessException e) {
			eh.handle(e);
		} catch (ClassCastException e) {
			eh.handle(e);
		} catch (NoSuchFieldException e) {
			eh.handle(e);
		} catch (SecurityException e) {
			eh.handle(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	static <Ex extends Exception, T> T invoke(Class<?> clazz, Object o, ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
		try {
			Method m = null;
			Class<?>[] classes = getClassesFromParams(parameters);
			try {
				m = clazz.getMethod(methodName, classes);
			} catch (NoSuchMethodException e) {
				nextMethod: for (Method match : clazz.getDeclaredMethods()) {
					if (match.getName().equals(methodName) && match.getParameterCount() == classes.length) {
						for (int i = 0; i < classes.length; i++) {
							Class<?>[] params = match.getParameterTypes();
							if (! (classes[i] == null || params[i].isAssignableFrom(classes[i])) ) {
								continue nextMethod;
							}
						}
						m = match;
						break;
					}
				}
				if (m == null) throw e;
			}
			return (T) m.invoke(o, parameters);
		} catch (IllegalAccessException e) {
			eh.handle(e);
		} catch (ClassCastException e) {
			eh.handle(e);
		} catch (NoSuchMethodException e) {
			eh.handle(e);
		} catch (SecurityException e) {
			eh.handle(e);
		} catch (IllegalArgumentException e) {
			eh.handle(e);
		} catch (InvocationTargetException e) {
			eh.handle(e);
		}
		return null;
	}

	static Class<?>[] getClassesFromParams(Object[] params) {
		Class<?>[] classes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			Object param = params[i];
			if (param == null) {
				classes[i] = null;//TODO correct???
				continue;
			} else if (param instanceof AnonymousObject) {
				param = ((AnonymousObject)param).getObject();
				params[i] = param;
			}
			classes[i] = param.getClass();
		}
		return classes;
	}
}
