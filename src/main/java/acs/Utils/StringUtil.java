package acs.Utils;

public class StringUtil {
	public static boolean isNullOrEmpty(String str)
	{
		return str != null || !str.trim().isEmpty();
	}
}
