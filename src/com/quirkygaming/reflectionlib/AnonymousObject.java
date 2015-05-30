package com.quirkygaming.reflectionlib;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnonymousObject {
	
	private Object o;
	
	AnonymousObject(Object o) {
		this.o=o;
	}
	
	public static AnonymousObject fromObject(Object o) {
		return new AnonymousObject(o);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject() throws ClassCastException {
		return (T) o;
	}
	
	@SuppressWarnings("unchecked")
	public <T, Ex extends Throwable> T getField(ErrorHandler<Ex> eh, String fieldName) throws Ex {
		try {
			Field m = o.getClass().getField(fieldName);
			return (T) m.get(o);
		} catch (IllegalArgumentException e) {
			eh.onReflectionException(e);
		} catch (IllegalAccessException e) {
			eh.onReflectionException(e);
		} catch (ClassCastException e) {
			eh.onReflectionException(e);
		} catch (NoSuchFieldException e) {
			eh.onReflectionException(e);
		} catch (SecurityException e) {
			eh.onReflectionException(e);
		}
		return null;
	}
	
	public <Ex extends Throwable> AnonymousObject getFieldAnon(ErrorHandler<Ex> eh, String fieldName) throws Ex {
		return AnonymousObject.fromObject(getField(eh, fieldName));
	}

	public <Ex extends Throwable> void setField(ErrorHandler<Ex> eh, String fieldName, Object value) throws Ex {
		try {
			Field m = o.getClass().getField(fieldName);
			if (value != null && value instanceof AnonymousObject) {
				value = ((AnonymousObject)value).o;
			}
			m.set(o, value);
		} catch (IllegalArgumentException e) {
			eh.onReflectionException(e);
		} catch (IllegalAccessException e) {
			eh.onReflectionException(e);
		} catch (ClassCastException e) {
			eh.onReflectionException(e);
		} catch (NoSuchFieldException e) {
			eh.onReflectionException(e);
		} catch (SecurityException e) {
			eh.onReflectionException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <Ex extends Throwable, T> T invoke(ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
		try {
			Method m;
			Class<?>[] classes = getClassesFromParams(parameters);
			m = o.getClass().getMethod(methodName, classes);
			return (T) m.invoke(o, parameters);
		} catch (IllegalAccessException e) {
			eh.onReflectionException(e);
		} catch (ClassCastException e) {
			eh.onReflectionException(e);
		} catch (NoSuchMethodException e) {
			eh.onReflectionException(e);
		} catch (SecurityException e) {
			eh.onReflectionException(e);
		} catch (IllegalArgumentException e) {
			eh.onReflectionException(e);
		} catch (InvocationTargetException e) {
			eh.onInvocationTargetException(e);
		}
		return null;
	}
	
	public <Ex extends Throwable> AnonymousObject invokeAnon(ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
		return fromObject(invoke(eh, methodName, parameters));
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
