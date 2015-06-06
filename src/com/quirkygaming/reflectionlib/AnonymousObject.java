package com.quirkygaming.reflectionlib;

import com.quirkygaming.errorlib.ErrorHandler;

public class AnonymousObject {
	
	Object o;
	
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
	public <T, Ex extends Exception> T getObject(ErrorHandler<Ex> eh) throws Ex {
		try {
			return (T) o;
		} catch (ClassCastException e) {
			eh.handle(e);
		}
		return null;
	}
	
	public <T> T getField(String fieldName) {
		return getField(ErrorHandler.throwAll(), fieldName);
	}
	
	public <T, Ex extends Exception> T getField(ErrorHandler<Ex> eh, String fieldName) throws Ex {
		return ReflUtil.getField(o.getClass(), o, eh, fieldName);
	}
	
	public AnonymousObject getFieldAnon(String fieldName) {
		return getFieldAnon(ErrorHandler.throwAll(), fieldName);
	}
	
	public <Ex extends Exception> AnonymousObject getFieldAnon(ErrorHandler<Ex> eh, String fieldName) throws Ex {
		return AnonymousObject.fromObject(getField(eh, fieldName));
	}
	
	public void setField(String fieldName, Object value) {
		setField(ErrorHandler.throwAll(), fieldName, value);
	}

	public <Ex extends Exception> void setField(ErrorHandler<Ex> eh, String fieldName, Object value) throws Ex {
		ReflUtil.setField(o.getClass(), o, eh, fieldName, value);
	}
	
	public <T> T invoke(String methodName, Object... parameters) {
		return invoke(ErrorHandler.throwAll(), methodName, parameters);
	}
	
	public <Ex extends Exception, T> T invoke(ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
		return ReflUtil.invoke(o.getClass(), o, eh, methodName, parameters);
	}
	
	public AnonymousObject invokeAnon(String methodName, Object... parameters) {
		return fromObject(invoke(methodName, parameters));
	}
	
	public <Ex extends Exception> AnonymousObject invokeAnon(ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
		return fromObject(invoke(eh, methodName, parameters));
	}
	
}
