package acs.Utils;

import java.util.regex.Pattern;

public class StringUtil {
	public static boolean isNullOrEmpty(String str)
	{
		if (str == null)
			return true;
		if (str.trim().isEmpty())
			return true;
		return false;
	}
	public static boolean isEmailGood(String email)
	{
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$"; 
                  
        Pattern pat = Pattern.compile(emailRegex); 
        if (StringUtil.isNullOrEmpty(email))
        	return false;
        return pat.matcher(email).matches(); 
	}
}
