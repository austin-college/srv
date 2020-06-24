package srv.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * converts from date to string that identifies semester
 * 
 * @author Nadia Hannon
 */

@Component
public class SemesterUtil {

	/**
	 * Default contructor needed to qualify as a bean.
	 */
	public SemesterUtil() {
		
	}
	
	/**
	 * Derives the semester term (FA, JA, SP, SU) from a date.
	 * 
	 * @param anyDate 
	 * @return one of "FA","JA","SP","SU"
	 */
	public String term(Date anyDate) {
		/**
		 * Should return term in form FA
		 */
		String justMonth = "MM";   // looking for the month
		
		DateFormat df = new SimpleDateFormat(justMonth);
		String month = df.format(anyDate);
		int monthNum = Integer.parseInt(month);

		if (monthNum == 1) {
			return "JA";
		} else if (monthNum == 2 || monthNum == 3 || monthNum == 4 || monthNum == 5) {
			return "SP";
		} else if (monthNum == 6 || monthNum == 7 || monthNum == 8) {
			return "SU";
		} else if (monthNum == 9 || monthNum == 10 || monthNum == 11 || monthNum == 12) {
			return "FA";
		}
		return null;

		// return returnString;
	}// end of term

	
	/**
	 * Should return a unique semester label in form "2020SP"
	 */
	public String semesterID(Date anyDate) {
		String returnString = new String();

		String idFormat = "yyyy";
		DateFormat df = new SimpleDateFormat(idFormat);
		returnString = df.format(anyDate) + term(anyDate);

		return returnString;
	}// end of semesterID


	/**
	 * Given a date, we return a string label identifying the 
	 * corresponding academic year.  Should return academic year 
	 * in form "AY2020/2021" 
	 * 
	 * @param anyDate
	 * @return
	 */
	public String acadYear(Date anyDate) {

		String term = term(anyDate);

		// if term is january 2020
		// then return ay

		String justYear = "yyyy";
		DateFormat df = new SimpleDateFormat(justYear);
		if (term == "JA" || term == "SP" || term == "SU") {
			return "AY" + (Integer.parseInt(df.format(anyDate)) - 1) + "/" + df.format(anyDate);

		} // should return one less than the year of anyDate
		else if (term == "FA") {
			return "AY" + df.format(anyDate) + "/" + (Integer.parseInt(df.format(anyDate)) + 1);
			// should return one less than the year of anyDate
		}

		return null;
	}// end of acadYear

	/**
	 * Should return current academic year in "AY2020/2021" form
	 * 
	 * @return the current academic year based on today's date.
	 */
	public String currentAcadYear() {
		Date now = new Date();
		return acadYear(now);
	}// end of currentAcadYear

	
	/**
	 * Should return the current termID in "2020FA" form.
	 * 
	 * @see srv.utils.SemesterUtil.semesterID(Date);
	 * 
	 * @return the current semester/term based on today's date.
	 */
	public  String currentSemester() {
		Date now = new Date();
		return semesterID(now);
	}// end of currentTerm


}// end of class SemesterUtil
