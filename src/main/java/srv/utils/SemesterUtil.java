package srv.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SemesterUtil {
/**
 * converts from date to string that identifies semester
 * @author Nadia Hannon
 */
	
	
	public SemesterUtil() {
		super();
	}//end of SemesterUtil constructor
	
	public static String term(Date anyDate) {
	/**
	 * Should return term in form FA
	 */
		String justMonth = "mm";
		DateFormat df = new SimpleDateFormat(justMonth);
		String month = df.format(anyDate);
		int monthNum = Integer.parseInt(month);
		
		if (monthNum==1) {
			return "JA";
		} else if (monthNum == 2||monthNum ==3||monthNum ==4||monthNum ==5) {
			return "SP";
		} else if(monthNum == 6||monthNum ==7||monthNum ==8) {
			return "SU";
		} else if(monthNum == 9||monthNum ==10||monthNum ==11||monthNum ==12) {
			return "FA";
		} return null;
		
		//return returnString;
	}//end of term
	
	public static String semesterID(Date anyDate) {
		/**
		 * Should return date in form "2020SP"
		 */
		String returnString = new String();
		
		String idFormat = "yyyy" + term(anyDate);
		DateFormat df = new SimpleDateFormat(idFormat);
	returnString = df.format(anyDate);
		
		return returnString;
	}//end of semesterID
	
	//TODO acadYear
	
	public static String acadYear(Date anyDate) {
		/**
		 * Should return academic year in form "AY2020/2021"
		 */
		
		String term = term(anyDate);
		
		//if term is january 2020
		//then return ay 
		
		String justYear = "yyyy";
		DateFormat df = new SimpleDateFormat(justYear);
		if (term=="JA"||term=="SP"||term=="SU") {
			return "AY"+(Integer.parseInt(df.format(anyDate))-1)+"/"+df.format(anyDate);
			
		}//should return one less than the year of anyDate
		 	if (term=="FA") {
			return "AY"+df.format(anyDate)+"/"+(Integer.parseInt(df.format(anyDate))+1);
			//should return one less than the year of anyDate
		} 
		
		return null;
	}//end of acadYear

	public static String currentAcadYear() {
		/**
		 * Should return current academic year in "AY2020/2021" form
		 */
		//the internet said this would initialize to the current date soooo...
		Date now = new Date(); 
		return acadYear(now);
	}//end of currentAcadYear
	
	public static String currentTerm() {
		/**
		 * Should return the current termID in "FA" form
		 */
		Date now = new Date();
		return semesterID(now);
	}//end of currentTerm
	
	//TODO tests???
	
}//end of class SemesterUtil
