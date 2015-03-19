package com.quirkygaming.genericslib;

public class Pair<C0, C1> {
	private final C0 obj0;
	private final C1 obj1;
	
	public static <Class0, Class1> Pair<Class0, Class1> pair(Class0 object0, Class1 object1) {
		return new Pair<Class0, Class1>(object0, object1);
	}
	
	private Pair(C0 obj0, C1 obj1) {
		this.obj0 = obj0;
		this.obj1 = obj1;
	}
	
	public C0 getFirst() {return obj0;}
	public C1 getSecond() {return obj1;}
}
