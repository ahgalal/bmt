package sys.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class Arrays {
	public static boolean equalsOneOf(final Object mainObj,
			final ArrayList<? extends Object> arr) {
		return equalsOneOf(mainObj, arr.toArray(new Object[0]));
	}

	public static boolean equalsOneOf(final Object mainObj,
			final Iterator<? extends Object> it) {
		for (; it.hasNext();) {
			final Object obj = it.next();
			if (mainObj.equals(obj))
				return true;
		}
		return false;
	}
	public static boolean equalsOneOf(final Object mainObj, final Object[] arr) {
		for (final Object obj : arr) {
			if (mainObj.equals(obj))
				return true;
		}
		return false;
	}
}
