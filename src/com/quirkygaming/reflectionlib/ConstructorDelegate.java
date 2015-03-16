package com.quirkygaming.reflectionlib;

public abstract class ConstructorDelegate<Superclass> {
	String[] classNames;
	String successMessage;
	String failureMessage;
	public abstract Superclass construct();
}
