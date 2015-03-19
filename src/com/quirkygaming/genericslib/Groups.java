package com.quirkygaming.genericslib;

public class Groups {

	public static <C0, C1> Pair<C0, C1> group(C0 object0, C1 object1) {
		return new Pair<C0, C1>(object0, object1);
	}

	public static <C0, C1, C2> Triad<C0, C1, C2> group(C0 object0, C1 object1, C2 object2) {
		return new Triad<C0, C1, C2>(object0, object1, object2);
	}

	public static <C0, C1, C2, C3> Quad<C0, C1, C2, C3> group(C0 object0, C1 object1, C2 object2, C3 object3) {
		return new Quad<C0, C1, C2, C3>(object0, object1, object2, object3);
	}
	
	public static class Pair<C0, C1> {
		private final C0 obj0;
		private final C1 obj1;
		
		public Pair(C0 obj0, C1 obj1) {
			this.obj0 = obj0;
			this.obj1 = obj1;
		}
		
		public C0 get0() {return obj0;}
		public C1 get1() {return obj1;}
	}
	
	public static class Triad<C0, C1, C2> extends Pair<C0, C1> {
		private final C2 obj;
		
		public Triad(C0 obj0, C1 obj1, C2 obj2) {
			super(obj0, obj1);
			obj = obj2;
		}
		
		public C2 get2() {return obj;}
	}

	public static class Quad<C0, C1, C2, C3> extends Triad<C0, C1, C2> {
		private final C3 obj;
		
		public Quad(C0 obj0, C1 obj1, C2 obj2, C3 obj3) {
			super(obj0, obj1, obj2);
			obj = obj3;
		}
		
		public C3 get3() {return obj;}
	}
}
