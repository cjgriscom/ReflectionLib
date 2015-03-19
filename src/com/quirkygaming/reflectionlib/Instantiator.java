package com.quirkygaming.reflectionlib;

public interface Instantiator<Superclass> {
	public Superclass instantiate();
	public Instantiator<Superclass> tryClass(
			String conditionClassName, 
			ConstructorDelegate<Superclass> delegate);
	public Instantiator<Superclass> tryClass(
			String[] conditionClassNames, 
			ConstructorDelegate<Superclass> delegate);
}
