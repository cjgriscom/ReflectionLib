package com.quirkygaming.genericslib;

public class Groups {

	public static <Class0, Class1> Pair<Class0, Class1> group(Class0 object0, Class1 object1) {
		return new Pair<Class0, Class1>(object0, object1);
	}

	public static <Class0, Class1, Class2> Triad<Class0, Class1, Class2> group(Class0 object0, Class1 object1, Class2 object2) {
		return new Triad<Class0, Class1, Class2>(object0, object1, object2);
	}
	
	public static class Pair<C0, C1> {
		private final C0 obj0;
		private final C1 obj1;
		
		public Pair(C0 obj0, C1 obj1) {
			this.obj0 = obj0;
			this.obj1 = obj1;
		}
		
		public C0 getFirst() {return obj0;}
		public C1 getSecond() {return obj1;}
	}
	
	public static class Triad<C0, C1, C2> extends Pair<C0, C1> {
		private final C2 obj;
		
		public Triad(C0 obj0, C1 obj1, C2 obj2) {
			super(obj0, obj1);
			obj = obj2;
		}
		
		public C2 getThird() {return obj;}
	}

}
