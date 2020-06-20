package srv.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import srv.utils.SemesterUtil;

class SemesterUtilTest {

	
	/*
	 * Any date in the fall should return FA
	 */
	@Test
	public void testTerm_whenDateIsFall() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("11/2/1998");

		Assert.assertEquals("FA", SemesterUtil.term(date));
	}//end of test

	/*
	 * Any january date should return JA
	 */
	@Test
	public void testTerm_whenDateIsJan() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("1/2/1998");

		Assert.assertEquals("JA", SemesterUtil.term(date));
	}//end of test

	/*
	 * Any spring date should return SP
	 */
	@Test
	public void testTerm_whenDateIsSpring() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("3/2/1998");

		Assert.assertEquals("SP", SemesterUtil.term(date));
	}//end of test

	/*
	 * Any summer date should return SU
	 */
	@Test
	public void testTerm_whenDateIsSummer() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("6/2/1998");

		Assert.assertEquals("SU", SemesterUtil.term(date));
	}//end of test
	
	
	/*
	 * Semester ID composed of year and term YYYYtt
	 */
	@Test
	public void testSemesterId() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("11/2/1998");

		Assert.assertEquals("1998FA", SemesterUtil.semesterID(date));
		
		
	}//end of test

	
	/*
	 * Academic Year should start with literal "AY" prefix, followed by 
	 * a four digit date, followed by a slash, followed by a four digit date.
	 * And no more.
	 */
	@Test
	public void testAcadYr() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("11/2/1998");

		Matcher matcher = Pattern.compile("^(AY)([0-9]{4})[//]([0-9]{4})$")
				
				.matcher(SemesterUtil.acadYear(date));
		Assert.assertTrue(matcher.matches());
		Assert.assertEquals("AY",matcher.group(1));
		Assert.assertEquals("1998",matcher.group(2));
		Assert.assertEquals("1999",matcher.group(3));
		
	}//end of test
	
	/*
	 * Fall date defines the academic year.  All other semester terms (JA,SP,SU)
	 * belongs to the FA's academic year.
	 */
	@Test
	public void testAcadYr_whenDateIsFall() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("11/2/1998");

		Assert.assertEquals("AY1998/1999", SemesterUtil.acadYear(date));
	}//end of test

	@Test
	public void testAcadYr_whenDateIsJan() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("1/2/1998");

		Assert.assertEquals("AY1997/1998", SemesterUtil.acadYear(date));
	}//end of test

	@Test
	public void testAcadYr_whenDateIsSpring() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("3/2/1998");

		Assert.assertEquals("AY1997/1998", SemesterUtil.acadYear(date));
	}//end of test

	@Test
	public void testAcadYr_whenDateIsSummer() throws Exception {

		Date date =new SimpleDateFormat("MM/dd/yyyy").parse("6/2/1998");

		Assert.assertEquals("AY1997/1998", SemesterUtil.acadYear(date));
	}//end of test

}//end of class
