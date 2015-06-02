package com.quirkygaming.reflectionlib;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.quirkygaming.errorlib.ErrorHandler;

public class AnonymousClass {
	
	public static ErrorHandler<Exception> classExceptionHandler(Class<?> handledExceptions) {
		return ErrorHandler.forwardHandled(handledExceptions).addExceptionsToUnwrap(InvocationTargetException.class);
	}
	public static ErrorHandler<Exception> classExceptionHandler(PrintStream logger, Class<?> handledExceptions) {
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
	
	public static AnonymousClass get(PrintStream logger, String className) {
		return get(ErrorHandler.logAll(logger), className);
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
	
	public AnonymousObject instantiate(PrintStream logger, Object... parameters) {
		return instantiate(ErrorHandler.throwAll(), parameters);
	}
	
	public <Ex extends Exception> AnonymousObject instantiate(ErrorHandler<Ex> eh, Object... parameters) throws Ex {
		try {
			Object o;
			if (parameters.length == 0) {
				o = type.newInstance();
			} else {
				Constructor<?> c;
				Class<?>[] classes = AnonymousObject.getClassesFromParams(parameters);
				c = type.getConstructor(classes);
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
	
	public static void setClassLoader(ClassLoader classLoader) {
		AnonymousClass.classLoader = classLoader;
	}
	
}
