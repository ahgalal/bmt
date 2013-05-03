package utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {
	public static Object callMethod(final Object obj, final String methodName,
			final Object[] params) {
		final Method[] methods = obj.getClass().getMethods();
		Method method = null;
		for (final Method meth : methods) {
			if (meth.getName().equals(methodName)) {
				method = meth;
				break;
			}
		}
		try {
			return method.invoke(obj, params);
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	// TODO: complete implementation
	public static Object getFieldByPath(final Object obj, final String fldPath) {
		String[] instanceNames = fldPath.split("\\");
		Object tmpObj=null;
		for(String instanceName:instanceNames){
			if(tmpObj==null)
				tmpObj=getField(obj, instanceName);
			else
				tmpObj=getField(tmpObj, instanceName);
		}
		return tmpObj;
	}
	public static Object getField(final Object obj, final String fldName) {
		Field fldData;
		Object data = null;
		try {
			fldData = getDeclaredField(obj.getClass(), fldName);
			fldData.setAccessible(true);
			data = fldData.get(obj);

		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final NoSuchFieldException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		return data;

	}

	private static Field getDeclaredField(Class<?> cls, final String fldName)
			throws NoSuchFieldException {
		Field field = null;
		try {
			field= cls.getDeclaredField(fldName);
		} catch (NoSuchFieldException e) {
			if(!cls.getName().equals("Object")){
				// try getting the field declaration from superclass
				field = getDeclaredField(cls.getSuperclass(), fldName);
			}
		}
		return field;
	}

	public static void setField(final Object obj, final String fldName,
			final Object value) {
		Field fldData;
		try {
			fldData = obj.getClass().getDeclaredField(fldName);
			fldData.setAccessible(true);
			fldData.set(obj, value);

		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final NoSuchFieldException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
