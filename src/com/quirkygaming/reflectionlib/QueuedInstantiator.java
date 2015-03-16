package com.quirkygaming.reflectionlib;

import java.io.PrintStream;
import java.util.LinkedList;

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
public class QueuedInstantiator<Superclass> {
	
	private ClassLoader classLoader = QueuedInstantiator.class.getClassLoader();
	private LinkedList<ConstructorDelegate<Superclass>> queue = new LinkedList<ConstructorDelegate<Superclass>>();
	private PrintStream messageStream;
	
	public QueuedInstantiator() {this(true);}
	
	public QueuedInstantiator(boolean log) {
		this(log ? System.out : null);
	}
	
	public QueuedInstantiator(PrintStream logStream) {
		this.messageStream = logStream;
	}
	
	public QueuedInstantiator<Superclass> setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		return this;
	}
	
	public QueuedInstantiator<Superclass> queueClass(
			String conditionClassName, 
			ConstructorDelegate<Superclass> delegate) {
		
		return queueClass(conditionClassName,
				conditionClassName + " exists!",
				conditionClassName + " not found.",
				delegate);
	}
	
	public QueuedInstantiator<Superclass> queueClass(
					String conditionClassName, 
					String successMessage, 
					String failureMessage, 
					ConstructorDelegate<Superclass> delegate) {
		
		return queueClass(new String[]{conditionClassName}, 
				successMessage, failureMessage, delegate);
	}
	
	public QueuedInstantiator<Superclass> queueClass(
			String[] conditionClassNames, 
			String successMessage, 
			String failureMessage, 
			ConstructorDelegate<Superclass> delegate) {

					delegate.classNames = conditionClassNames;
					delegate.successMessage = successMessage;
					delegate.failureMessage = failureMessage;
					queue.addFirst(delegate);
					return this;
	}
	
	public <E extends Exception> Superclass instantiate(E failureException) throws E {
		String n = null;
		Superclass attempt = instantiate(n);
		if (attempt == null) {
			throw failureException;
		}
		return attempt;
	}
	
	public Superclass instantiate() {
		return instantiate("No classes found.");
	}
	
	public Superclass instantiate(String noClassFoundMessage) {
		Superclass attempt = null;
		while (attempt == null && !queue.isEmpty()){
			ConstructorDelegate<Superclass> delegate = queue.removeLast();
			try {
				for (String cName : delegate.classNames) {
					Class.forName(cName, false, classLoader);
				}
				attempt = delegate.construct();
				log(delegate.successMessage);
			} catch (ClassNotFoundException e) {
				log(delegate.failureMessage);
			}
			
		}
		if (attempt == null) {
			if (noClassFoundMessage == null) log(noClassFoundMessage);
		}
		
		return attempt;
	}
	
	private void log(String message) {
		if (messageStream != null) {
			messageStream.println(message);
		}
	}
}
