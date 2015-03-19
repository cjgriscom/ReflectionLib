package com.quirkygaming.reflectionlib;

import java.util.LinkedList;
import java.util.Queue;

import com.quirkygaming.genericslib.Groups;
import com.quirkygaming.genericslib.Groups.Pair;

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
public class QueuedInstantiator<Superclass> implements Instantiator<Superclass> {
	
	private ClassLoader classLoader = QueuedInstantiator.class.getClassLoader();
	private Queue<Pair<ConstructorDelegate<Superclass>, String[]>> queue = new LinkedList<Pair<ConstructorDelegate<Superclass>, String[]>>();
	
	public QueuedInstantiator<Superclass> setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		return this;
	}
	
	public QueuedInstantiator<Superclass> tryClass(
					ConstructorDelegate<Superclass> delegate, 
					String... conditionClassNames) {
		
		queue.add(Groups.group(delegate,conditionClassNames));
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
		Superclass attempt = null;
		while (attempt == null && !queue.isEmpty()){
			Pair<ConstructorDelegate<Superclass>, String[]> info = queue.remove();
			try {
				for (String cName : info.get1()) {
					Class.forName(cName, false, classLoader);
				}
				attempt = info.get0().construct();
			} catch (ClassNotFoundException e) {}
			
		}
		
		return attempt;
	}
}
