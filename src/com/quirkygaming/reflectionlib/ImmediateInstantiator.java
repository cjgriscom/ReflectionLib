package com.quirkygaming.reflectionlib;

import java.io.PrintStream;

import com.quirkygaming.genericslib.Null;

/**
 * This class is intended for applications where multiple libraries are used in different
 * circumstances to provide application behavior.
 * 
 * See com.quirkygaming.reflectionlib.test.Test for an example.
 * 
 * @author Chandler Griscom
 *
 * @param <Superclass> An interface or superclass that generically represents classes that might be loaded.
 */
public class ImmediateInstantiator<Superclass> implements Instantiator<Superclass> {
	
	private ClassLoader classLoader = ImmediateInstantiator.class.getClassLoader();
	private PrintStream messageStream;
	
	private Superclass instance = null;
	
	public ImmediateInstantiator() {this(true);}
	
	public ImmediateInstantiator(boolean log) {
		this(log ? System.out : null);
	}
	
	public ImmediateInstantiator(PrintStream logStream) {
		this.messageStream = logStream;
	}
	
	public ImmediateInstantiator<Superclass> setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		return this;
	}
	
	public ImmediateInstantiator<Superclass> tryClass(
			String conditionClassName, 
			ConstructorDelegate<Superclass> delegate) {
		
		return tryClass(conditionClassName, null, null, delegate);
	}
	
	public ImmediateInstantiator<Superclass> tryClass(
					String conditionClassName, 
					String successMessage, 
					String failureMessage, 
					ConstructorDelegate<Superclass> delegate) {
		
		return tryClass(new String[]{conditionClassName}, 
				successMessage, failureMessage, delegate);
	}
	
	public ImmediateInstantiator<Superclass> tryClass(
			String[] conditionClassNames,
			ConstructorDelegate<Superclass> delegate) {
		return tryClass(conditionClassNames, null, null, delegate);
	}
	
	public ImmediateInstantiator<Superclass> tryClass(
			String[] conditionClassNames, 
			String successMessage, 
			String failureMessage, 
			ConstructorDelegate<Superclass> delegate) {
		
		if (instance != null) return this; // Already instantiated 
		try {
			for (String cName : conditionClassNames) {
				Class.forName(cName, false, classLoader);
			}
			instance = delegate.construct();
			log(successMessage);
		} catch (ClassNotFoundException e) {
			log(failureMessage);
		}
		return this;
	}
	
	public <E extends Exception> Superclass instantiate(E failureException) throws E {
		Superclass attempt = instantiate();
		if (attempt == null) {
			throw failureException;
		}
		return attempt;
	}
	
	public Superclass instantiate() {
		return instantiate(Null.<String>get());
	}
	
	public Superclass instantiate(String noClassFoundMessage) {
		if (instance == null) {
			log(noClassFoundMessage);
		}
		
		return instance;
	}
	
	private void log(String message) {
		if (message != null) {
			messageStream.println(message);
		}
	}
}
