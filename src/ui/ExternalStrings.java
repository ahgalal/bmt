package ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ExternalStrings
{
	private static final String BUNDLE_NAME = "ui.strings"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private ExternalStrings()
	{
	}

	public static String get(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}
}
