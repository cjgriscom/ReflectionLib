package com.quirkygaming.reflectionlib;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import com.quirkygaming.errorlib.ErrorHandler;

public class AnonymousClass {
	
	public static ErrorHandler<Exception> classExceptionHandler(Class<?> handledExceptions) {
		return ErrorHandler.forwardHandled(handledExceptions).addExceptionsToUnwrap(InvocationTargetException.class);
	}
	public static ErrorHandler<Exception> classExceptionHandler(PrintStream logger, Class<?> handledExceptions) {
		return ErrorHandler.forwardHandled(logger, handledExceptions).addExceptionsToUnwrap(InvocationTargetException.class);
	}
	public static ErrorHandler<Exception> classExceptionHandler(Logger logger, Class<?> handledExceptions) {
		return ErrorHandler.forwardHandled(logger, handledExceptions).addExceptionsToUnwrap(InvocationTargetException.class);
	}
	
	private static ClassLoader classLoader = AnonymousClass.class.getClassLoader();
	
	private final Class<?> type;
	
	private AnonymousClass(Class<?> clazz) {
		type=clazz;
	}
	
	public static AnonymousClass get(String className) {
		return get(ErrorHandler.throwAll(), className);
	}
	
	public static <Ex extends Exception> AnonymousClass get(ErrorHandler<Ex> eh, String className) throws Ex {
		try {
			return new AnonymousClass(Class.forName(className, false, classLoader));
		} catch (ClassNotFoundException e) {
			eh.handle(e);
			return null;
		}
	}
	
	public AnonymousObject instantiate(Object... parameters) {
		return instantiate(ErrorHandler.throwAll(), parameters);
	}
	
	public <Ex extends Exception> AnonymousObject instantiate(ErrorHandler<Ex> eh, Object... parameters) throws Ex {
		return ReflUtil.instantiate(type, eh, parameters);
	}
	
	public <T> T getField(String fieldName) {
		return getField(ErrorHandler.throwAll(), fieldName);
	}
	
	public <T, Ex extends Exception> T getField(ErrorHandler<Ex> eh, String fieldName) throws Ex {
		return ReflUtil.getField(type, null, eh, fieldName);
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
		ReflUtil.setField(type, null, eh, fieldName, value);
	}
	
	public <T> T invoke(String methodName, Object... parameters) {
		return invoke(ErrorHandler.throwAll(), methodName, parameters);
	}
	
	public <Ex extends Exception, T> T invoke(ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
		return ReflUtil.invoke(type, null, eh, methodName, parameters);
	}
	
	public AnonymousObject invokeAnon(String methodName, Object... parameters) {
		return AnonymousObject.fromObject(invoke(methodName, parameters));
	}
	
	public <Ex extends Exception> AnonymousObject invokeAnon(ErrorHandler<Ex> eh, String methodName, Object... parameters) throws Ex {
		return AnonymousObject.fromObject(invoke(eh, methodName, parameters));
	}
	
	public static void setClassLoader(ClassLoader classLoader) {
		AnonymousClass.classLoader = classLoader;
	}
	
}
