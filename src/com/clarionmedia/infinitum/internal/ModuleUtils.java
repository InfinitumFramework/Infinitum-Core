package com.clarionmedia.infinitum.internal;

public class ModuleUtils {
	
	private static final String ORM_MARKER_CLASS = "com.clarionmedia.infinitum.orm.context.OrmContext";
	
	public static boolean hasOrm() {
		return hasClass(ORM_MARKER_CLASS);
	}
	
	private static boolean hasClass(String name) {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(name);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

}
