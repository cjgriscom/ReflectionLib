package com.quirkygaming.reflectionlib;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.quirkygaming.errorlib.ErrorHandler;

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
	
	public <T> T getField(String fieldName) {
		return getField(ErrorHandler.throwAll(), fieldName);
	}
	
	public <T> T getField(PrintStream logger, String fieldName) {
		return getField(ErrorHandler.logAll(logger), fieldName);
	}
	
	@SuppressWarnings("unchecked")
	public <T, Ex extends Exception> T getField(ErrorHandler<Ex> eh, String fieldName) throws Ex {
		try {
			Field m = o.getClass().getField(fieldName);
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
	
	public AnonymousObject getFieldAnon(String fieldName) {
		return getFieldAnon(ErrorHandler.throwAll(), fieldName);
	}
	
	public AnonymousObject getFieldAnon(PrintStream logger, String fieldName) {
		return getFieldAnon(ErrorHandler.logAll(logger), fieldName);
	}
	
	public <Ex extends Exception> AnonymousObject getFieldAnon(ErrorHandler<Ex> eh, String fieldName) throws Ex {
		return AnonymousObject.fromObject(getField(eh, fieldName));
	}
	
	public void setField(String fieldName, Object value) {
		setField(ErrorHandler.throwAll(), fieldName, value);
	}
	
	public void setField(PrintStream logger, String fieldName, Object value) {
		setField(ErrorHandler.logAll(logger), fieldName, value);
	}

	public <Ex extends Exception> void setField(ErrorHandler<Ex> eh, String fieldName, Object value) throws Ex {
		try {
			Field m = o.getClass().getField(fieldName);
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
	
	public <T> T invoke(String methodName, Object... parameters) {
		return invoke(ErrorHandler.throwAll(), methodName, parameters);
	}
	
	public <T> T invoke(PrintStream logger, String methodName, Object... parameters) {
		return invoke(ErrorHandler.logAll(logger), methodName, parameters);
	}
	
	@SuppressWarnings("unchecked")
	public <Ex extends Exception, T> T invoke(ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
		try {
			Method m;
			Class<?>[] classes = getClassesFromParams(parameters);
			m = o.getClass().getMethod(methodName, classes);
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
	
	public AnonymousObject invokeAnon(String methodName, Object... parameters) {
		return fromObject(invoke(methodName, parameters));
	}
	
	public AnonymousObject invokeAnon(PrintStream logger, String methodName, Object... parameters) {
		return fromObject(invoke(logger, methodName, parameters));
	}
	
	public <Ex extends Exception> AnonymousObject invokeAnon(ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
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
