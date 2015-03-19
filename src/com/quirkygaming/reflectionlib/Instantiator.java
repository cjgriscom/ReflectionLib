package com.quirkygaming.reflectionlib;

public interface Instantiator<Superclass> {
	public Superclass instantiate();
	public Instantiator<Superclass> tryClass(
			ConstructorDelegate<Superclass> delegate, 
			String... conditionClassNames);
}
