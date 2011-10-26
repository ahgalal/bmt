package utils;

import java.lang.reflect.Field;

public class ReflectUtils
{
	public static Object getField(Class<?> cls, Object obj,String fldName)
	{
		Field fldData;
		Object data = null;
		try
		{
			fldData = cls.getDeclaredField(fldName);
			fldData.setAccessible(true);
			data=fldData.get(obj);
			
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return data;
		
	}
	
	
	public static void setField(Class<?> cls, Object obj,String fldName,Object value)
	{
		Field fldData;
		try
		{
			fldData = cls.getDeclaredField(fldName);
			fldData.setAccessible(true);
			fldData.set(obj, value);
			
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

}
