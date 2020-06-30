package srv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This utility class is the home for several convenient server side 
 * error checking/handling methods for parameters passed to our controllers. 
 * 
 * @author Lydia House
 *
 */
public class ParamUtil {
	
	public static String matchingString(String anyStr, String pattern, int grp, String errMsg) throws Exception {
		
			Pattern p = Pattern.compile(pattern);
			
			Matcher m = p.matcher(anyStr);
			
			if (!m.matches()) throw new Exception(String.format("[%s] does not match required pattern [%s].", anyStr, pattern));
			
			return m.group(grp);
			
	}
	

	public static String requiredNonEmptyString(String paramStr, String errMsg) throws Exception {
		
			// First check to see if null, if so return error message
			if (paramStr != null) {
				
				paramStr = paramStr.trim();

				// cannot be an empty string
				if (paramStr.length() == 0)
					throw new Exception(errMsg);
				
				return paramStr;
			}
			else {
				throw new Exception(errMsg);
			}

	}
	
	public static String optionalString(String paramStr, String errMsg) throws Exception {
		
		if (paramStr == null) return null;
		
		paramStr = paramStr.trim();
		
		if (paramStr.isEmpty()) throw new Exception(errMsg);
		
		return paramStr;
		
	}
	
	
	public static Date requiredDateLike(String dateStr, String pattern, String error) throws ParseException, Exception {
		if(dateStr != null) {
			dateStr = dateStr.trim();
			if(dateStr.length()>0) {
				Date tempDate = new SimpleDateFormat(pattern).parse(dateStr);
				return tempDate;
			} else {
				throw new Exception("Invalid Date Format.  Should be "+pattern);
			}
		} else {
			throw new Exception(error);
		}
	}
	
	/**
	 * Method for when a parameter is of type Double and can be optional (in other words it's okay
	 * for it to be null). Verifies that the parameter passed in is a numeric type. Returns error
	 * message if non-numeric, otherwise returns the Double value of the string.
	 * 
	 * @param paramStr
	 * @param errMsg
	 * @return
	 * @throws Exception
	 */
	public static Double optionalDoubleParam(String paramStr, String errMsg) throws Exception {
		
		Double param = null;
		
		try {
			
			// First check to see if null, if so just return the null value
			if (paramStr != null) {
				
				paramStr = paramStr.trim();
				
				// then verify that after trimming there is still something in the string
				// if not then throw error message
				if (paramStr.length() > 0)
					param = Double.valueOf(paramStr);
			}
		} catch(Exception e) {
			throw new Exception(errMsg);
		}
		
		return param;
	}
	 
	/**
	 * Method for when a parameter is of type Double but is required (in other words it's not
	 * okay for it to be null). Also verifies that the parameter passes in is a numeric type.
	 * Returns error message if null or non-numeric, otherwise returns the Double value of the
	 * string.
	 * 
	 * @param paramStr
	 * @param errMsg
	 * @return
	 * @throws Exception
	 */
	public static Double requiredDoubleParam(String paramStr, String errMsg) throws Exception {
		
		Double param = null;
		
		try {
			
			// First check to see if null, if so return error message
			if (paramStr != null) {
				
				paramStr = paramStr.trim();
				
				// then check to see if there is still something in the string after trimming,
				// if not then throw error message
				if (paramStr.length() > 0)
					param = Double.valueOf(paramStr);
				else
					throw new Exception(errMsg);
			}
			else {
				throw new Exception(errMsg);
			}
		} catch(Exception e) {
			throw new Exception(errMsg);
		}
		return param;
	}
	
	/**
	 * TODO returns error message if not 0/1 or true/false??
	 * 
	 * Method for when a parameter is of type Boolean and can be optional (in other words it's okay
	 * for it to be null). Verifies that the parameter passed in is a TODO? type. Returns error
	 * message if TODO ??, otherwise returns the Boolean value of the string.
	 * 
	 * @param paramStr
	 * @param errMsg
	 * @return
	 * @throws Exception
	 */
	public static Boolean optionalBooleanParam(String paramStr, String errMsg) throws Exception {
		
		Boolean param = null;
		
		try {
			
			// First check to see if null, if so just return the null value
			if (paramStr != null) {
				
				paramStr = paramStr.trim();
				
				// then verify that after trimming there is still something in the string
				// if not then throw error message
				if (paramStr.length() > 0)
					param = Boolean.valueOf(paramStr);
			}
		} catch(Exception e) {
			throw new Exception(errMsg);
		}
		
		return param;
	}
	 
	/**
	 * TODO returns error message if not 0/1 or true/false??
	 * 
	 * Method for when a parameter is of type Boolean but is required (in other words it's not
	 * okay for it to be null). Also verifies that the parameter passes in is a TODO?? type.
	 * Returns error message if null or TODO??, otherwise returns the Boolean value of the
	 * string.
	 * 
	 * @param paramStr
	 * @param errMsg
	 * @return
	 * @throws Exception
	 */
	public static Boolean requiredBooleanParam(String paramStr, String errMsg) throws Exception {
		
		Boolean param = null;
		
		try {
			
			// First check to see if null, if so return error message
			if (paramStr != null) {
				
				paramStr = paramStr.trim();
				
				// then check to see if there is still something in the string after trimming,
				// if not then throw error message
				if (paramStr.length() > 0)
					param = Boolean.valueOf(paramStr);
				else
					throw new Exception(errMsg);
			}
			else {
				throw new Exception(errMsg);
			}
		} catch(Exception e) {
			throw new Exception(errMsg);
		}
		return param;
	}
	
	/**
	 * Method for when a parameter is of type Integer and can be optional (in other words it's okay
	 * for it to be null). Verifies that the parameter passed in is a numeric type. Returns error
	 * message if non-numeric, otherwise returns the Integer value of the string.
	 * 
	 * @param paramStr
	 * @param errMsg
	 * @return
	 * @throws Exception
	 */
	public static Integer optionalIntegerParam(String paramStr, String errMsg) throws Exception {
		
		Integer param = null;
		
		try {
			
			// First check to see if null, if so just return the null value
			if (paramStr != null) {
				
				paramStr = paramStr.trim();
				
				// then verify that after trimming there is still something in the string
				// if not then throw error message
				if (paramStr.length() > 0)
					param = Integer.valueOf(paramStr);
			}
		} catch(Exception e) {
			throw new Exception(errMsg);
		}
		
		return param;
	}
	 
	/**
	 * Method for when a parameter is of type Integer but is required (in other words it's not
	 * okay for it to be null). Also verifies that the parameter passes in is a numeric type.
	 * Returns error message if null or non-numeric, otherwise returns the Integer value of the
	 * string.
	 * 
	 * @param paramStr
	 * @param errMsg
	 * @return
	 * @throws Exception
	 */
	public static Integer requiredIntegerParam(String paramStr, String errMsg) throws Exception {
		
		Integer param = null;
		
		try {
			
			// First check to see if null, if so return error message
			if (paramStr != null) {
				
				paramStr = paramStr.trim();
				
				// then check to see if there is still something in the string after trimming,
				// if not then throw error message
				if (paramStr.length() > 0)
					param = Integer.valueOf(paramStr);
				else
					throw new Exception(errMsg);
			}
			else {
				throw new Exception(errMsg);
			}
		} catch(Exception e) {
			throw new Exception(errMsg);
		}
		return param;
	}
	
	
}
