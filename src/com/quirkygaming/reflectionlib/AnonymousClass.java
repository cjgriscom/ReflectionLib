package com.quirkygaming.reflectionlib;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AnonymousClass {
	
	private static ClassLoader classLoader = AnonymousClass.class.getClassLoader();
	
	private final Class<?> type;
	
	private AnonymousClass(Class<?> clazz) {
		type=clazz;
	}
	
	public static <Ex extends Throwable> AnonymousClass get(ErrorHandler<Ex> eh, String className) throws Ex {
		try {
			return new AnonymousClass(Class.forName(className, false, classLoader));
		} catch (ClassNotFoundException e) {
			eh.onReflectionException(e);
			return null;
		}
	}
	
	public <Ex extends Throwable> AnonymousObject instantiate(ErrorHandler<Ex> eh, Object... parameters) throws Ex {
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
			eh.onReflectionException(e);
		} catch (InstantiationException e) {
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
	
	public static void setClassLoader(ClassLoader classLoader) {
		AnonymousClass.classLoader = classLoader;
	}
	
}
