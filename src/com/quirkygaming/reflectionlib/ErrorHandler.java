package com.quirkygaming.reflectionlib;

import java.lang.reflect.InvocationTargetException;

public abstract class ErrorHandler<T extends Throwable> {
	
	public static final ExceptionBypass bypass = new ExceptionBypass();
	public static final InvocationTargetExceptionForwarder forwardITE = new InvocationTargetExceptionForwarder();
	public static final AllExceptionForwarder forwardAll = new AllExceptionForwarder();
	
	public abstract void onReflectionException(Exception e) throws T;
	public abstract void onInvocationTargetException(InvocationTargetException e) throws T;
	
	public static class ExceptionBypass extends ErrorHandler<RuntimeException> {
		@Override
		public void onReflectionException(Exception e) {
			throw new RuntimeException(e);
		}

		@Override
		public void onInvocationTargetException(InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class InvocationTargetExceptionForwarder extends ErrorHandler<InvocationTargetException> {
		@Override
		public void onReflectionException(Exception e) {
			throw new RuntimeException(e);
		}

		@Override
		public void onInvocationTargetException(InvocationTargetException e) throws InvocationTargetException {
			throw e;
		}
	}
	
	public static class AllExceptionForwarder extends ErrorHandler<Exception> {
		@Override
		public void onReflectionException(Exception e) throws Exception {
			throw e;
		}

		@Override
		public void onInvocationTargetException(InvocationTargetException e) throws InvocationTargetException {
			throw e;
		}
	}
}
